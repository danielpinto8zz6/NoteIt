package io.github.danielpinto8zz6.noteit.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

import io.github.danielpinto8zz6.noteit.ColorUtil;
import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.Utils;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.notification.NotificationService;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;

public class EditNoteActivity extends AppCompatActivity implements ColorDialog.OnColorSelectedListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "NoteIt";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final int LOAD_IMAGE_RESULTS = 1234;

    private Note note;
    private int hashCode;

    private boolean edit = false;
    private boolean force = false;
    private boolean hasAlarm = false;

    private TextView titleEt;
    private ImageView imageView;
    private TextView contentEt;
    private TextView dateTv;

    private SwitchDateTimeDialogFragment dateTimeFragment;

    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEt = findViewById(R.id.note_title);
        imageView = findViewById(R.id.note_image);
        contentEt = findViewById(R.id.note_content);
        dateTv = findViewById(R.id.note_date);

        dateTv.setText(Utils.getCurrentDateTime());

        dateTv.setText(Utils.getCurrentDateTime());

        loadActivity(true);
    }

    private void loadActivity(boolean initial) {
        Intent intent = getIntent(); // gets the previously created intent
        note = intent.getParcelableExtra("note");

        if (note != null) {
            edit = true;
        } else {
            note = new Note();
        }

        if (edit) {
            hasAlarm = (note.getNotify_date() != null && !note.getNotify_date().isEmpty());
            titleEt.setText(note.getTitle());
            contentEt.setText(note.getContent());

            String editedDate = note.getEdited_date();
            if (editedDate != null && !editedDate.isEmpty())
                dateTv.setText(editedDate);
            else
                dateTv.setText(note.getCreate_date());

            String color = note.getColor();
            if (color != null && !color.isEmpty()) {
                setToolbarColor(Color.parseColor(color));
            }

            byte[] image = note.getImage();
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            }
            hashCode = note.hashCode();
        }
        if (!initial) {
            invalidateOptionsMenu();
        }

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }

        dateTimeFragment.setTimeZone(TimeZone.getDefault());
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setMinimumDateTime(Calendar.getInstance().getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                note.setNotify_date(Utils.getDateTime(date));

                if (!edit) {
                    saveNote();
                }

                Intent i = new Intent(EditNoteActivity.this, NotificationService.class);
                i.putExtra("note_id", note.getId());

                PendingIntent pi = PendingIntent.getService(EditNoteActivity.this, note.getId(), i, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

                hasAlarm = true;

                MenuItem item = mOptionsMenu.findItem(R.id.action_add_alert);
                item.setIcon(R.drawable.ic_remove_alert);
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icons_edit_note, menu);
        mOptionsMenu = menu;

        if (note != null && note.getStatus() != STATUS_ACTIVE) {
            menu.findItem(R.id.action_archive).setVisible(false);
            menu.findItem(R.id.action_add_alert).setVisible(false);
        } else {
            menu.findItem(R.id.action_unarchive).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_archive) {
            note.setStatus(STATUS_ARCHIVED);
            force = true;
            NoteDao.updateRecord(note);
            finish();
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_confirmation))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            NoteDao.deleteRecord(note);
                            force = true;
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else if (id == R.id.action_add_alert) {
            if (hasAlarm) {
                //ask whether to delete or update the current alarm
                PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.action_add_alert));
                popupMenu.inflate(R.menu.reminder);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            } else {
                dateTimeFragment.startAtCalendarView();
                dateTimeFragment.setDefaultDateTime(Calendar.getInstance().getTime());
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
            return true;
        } else if (id == R.id.action_unarchive) {
            note.setStatus(STATUS_ACTIVE);
            force = true;
            NoteDao.updateRecord(note);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reminder_edit) {
            dateTimeFragment.startAtCalendarView();
            dateTimeFragment.setDefaultDateTime(Calendar.getInstance().getTime());
            dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            return true;
        } else if (id == R.id.action_reminder_delete) {
            cancelNotification();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (!force) {
            if (edit) {
                updateNote();
            } else {
                saveNote();
            }
        }

        super.onPause();
    }

    private void saveNote() {
        note.setTitle(titleEt.getText().toString());
        note.setContent(contentEt.getText().toString());

        if (note.isEmpty())
            return;

        note.setId((int) NoteDao.insertRecord(note));

        edit = true;
        hashCode = note.hashCode();
    }

    private void updateNote() {
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();

        int hash = note.hashCode();

        note.setTitle(title);
        note.setContent(content);

        if (hash != note.hashCode()) {
            note.setEdited_date(Utils.getCurrentDateTime());
        }

        NoteDao.updateRecord(note);

        hashCode = note.hashCode();
    }

    @Override
    public void onColorSelected(int i, String s) {
        String color = Utils.getColorHex(i);
        note.setColor(color);

        setToolbarColor(i);
    }

    private void setToolbarColor(int c) {
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(c));

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        int colorDark = ColorUtil.darken(c, 12);
        getWindow().setStatusBarColor(colorDark);

    }

    public void attachImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(photoPickerIntent, LOAD_IMAGE_RESULTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            File file = new File(getPath(uri));
            try {
                Bitmap bitmap = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(file.getAbsolutePath()), 512, 512);
                byte[] image = Utils.getBitmapAsByteArray(bitmap);
                BitmapFactory.decodeByteArray(image, 0, image.length);
                note.setImage(image);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static Bitmap getThumbnail(ContentResolver cr, String path) throws Exception {

        Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (ca != null && ca.moveToFirst()) {
            int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
            ca.close();
            return MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        }

        ca.close();
        return null;

    }

    public void changeColor(View view) {
        new ColorDialog.Builder(this)
                .setColorShape(ColorShape.CIRCLE) //CIRCLE or SQUARE
                .setColorChoices(R.array.color_choices) //an array of colors
                .setSelectedColor(Color.GREEN) //the checked color
                .setTag("TAG") // tags can be useful when multiple components use the picker within an activity
                .show();
    }

    private void cancelNotification() {
        //Create the intent that would be fired by AlarmManager
        Intent i = new Intent(this, NotificationService.class);
        i.putExtra("note_id", note.getId());

        PendingIntent pi = PendingIntent.getService(this, note.getId(), i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);

        note.setNotify_date("");

        hasAlarm = false;

        MenuItem item = mOptionsMenu.findItem(R.id.action_add_alert);
        item.setIcon(R.drawable.ic_add_alert);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add_alert);
        if (hasAlarm) {
            item.setIcon(R.drawable.ic_remove_alert);
        }
        return super.onPrepareOptionsMenu(menu);
    }

}
