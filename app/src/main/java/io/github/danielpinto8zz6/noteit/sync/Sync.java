package io.github.danielpinto8zz6.noteit.sync;

import android.content.IntentSender;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.NoSuchPaddingException;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.encryption.AESHelper;
import io.github.danielpinto8zz6.noteit.ui.MainActivity;

import static io.github.danielpinto8zz6.noteit.ui.MainActivity.REQUEST_CODE_CREATION;
import static io.github.danielpinto8zz6.noteit.ui.MainActivity.REQUEST_CODE_OPENING;
import static io.github.danielpinto8zz6.noteit.ui.MainActivity.REQUEST_CODE_SIGN_IN;

public class Sync {
    private static final String TAG = "Google Drive Activity";

    private static final String PASSWORD = "h5Y*nPaKDKyd%Tgy+3tz7q93ua!j8kAP@cx$S6NQauTvEts&Abpzyfu%W$6=!Ajek8_87SM_xA?LhP?_+@w?r^6Mxn@_84qBcgTNQFR#R#L7jECcPHu*?ahrKUrkjuJc";

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    public TaskCompletionSource<DriveId> mOpenItemTaskSource;

    private MainActivity activity;

    public static final String DATABASE_NAME = "NoteIt";

    public Sync(MainActivity activity) {
        this.activity = activity;
    }

    public void connectToDrive(boolean backup) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account == null) {
            signIn();
        } else {
            //Initialize the drive api
            mDriveClient = Drive.getDriveClient(activity, account);
            // Build a drive resource client.
            mDriveResourceClient = Drive.getDriveResourceClient(activity, account);
            if (backup)
                startDriveBackup();
            else
                startDriveRestore();
        }
    }

    private void signIn() {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        activity.startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(activity, signInOptions);
    }


    private void startDriveBackup() {
        mDriveResourceClient
                .createContents()
                .continueWithTask(
                        task -> createFileIntentSender(Objects.requireNonNull(task.getResult())))
                .addOnFailureListener(
                        e -> Log.w(TAG, "Failed to create new contents.", e));
    }

    private Task<Void> createFileIntentSender(DriveContents driveContents) {

        final String inFileName = activity.getDatabasePath(DATABASE_NAME).toString();
        final String encryptedFileName = inFileName.concat(".crypt");

        try {
            AESHelper.encryptfile(inFileName, PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            File dbFile = new File(encryptedFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            OutputStream outputStream = driveContents.getOutputStream();

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                .setTitle("encrypted_backup.noteit")
                .setMimeType("application/db")
                .build();


        CreateFileActivityOptions createFileActivityOptions =
                new CreateFileActivityOptions.Builder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(driveContents)
                        .build();

        return mDriveClient
                .newCreateFileActivityIntentSender(createFileActivityOptions)
                .continueWith(
                        task -> {
                            activity.startIntentSenderForResult(task.getResult(), REQUEST_CODE_CREATION, null, 0, 0, 0);
                            return null;
                        });
    }

    private void startDriveRestore() {
        pickFile()
                .addOnSuccessListener(activity,
                        driveId -> retrieveContents(driveId.asDriveFile()))
                .addOnFailureListener(activity, e -> {
                    Log.e(TAG, "No file selected", e);
                });
    }

    private void retrieveContents(DriveFile file) {

        //DB Path
        final String inFileName = activity.getDatabasePath(DATABASE_NAME).toString();
        final String inFileNameEncrypted = inFileName.concat(".crypt");

        Task<DriveContents> openFileTask = mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);

        openFileTask
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = Objects.requireNonNull(contents).getParcelFileDescriptor();
                        FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                        // Open the empty db as the output stream
                        OutputStream output = new FileOutputStream(inFileNameEncrypted);

                        // Transfer bytes from the inputfile to the outputfile
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }

                        // Close the streams
                        output.flush();
                        output.close();
                        fileInputStream.close();

                        AESHelper.decrypt(inFileNameEncrypted, PASSWORD, inFileName);

                        Toast.makeText(activity, activity.getString(R.string.import_completed), Toast.LENGTH_SHORT).show();

                        activity.refresh();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, activity.getString(R.string.error_importing), Toast.LENGTH_SHORT).show();
                    }
                    return mDriveResourceClient.discardContents(Objects.requireNonNull(contents));

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Unable to read contents", e);
                    Toast.makeText(activity, activity.getString(R.string.error_importing), Toast.LENGTH_SHORT).show();
                });
    }

    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    activity.startIntentSenderForResult(
                            task.getResult(), REQUEST_CODE_OPENING, null, 0, 0, 0);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }

    private Task<DriveId> pickFile() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "application/db"))
                        .setActivityTitle(activity.getString(R.string.select_file))
                        .build();
        return pickItem(openOptions);
    }
}