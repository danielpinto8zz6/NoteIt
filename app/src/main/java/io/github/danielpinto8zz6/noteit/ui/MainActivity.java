package io.github.danielpinto8zz6.noteit.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;

import java.util.ArrayList;
import java.util.Objects;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.Utils;
import io.github.danielpinto8zz6.noteit.encryption.AESHelper;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.notes.NotesManager;
import io.github.danielpinto8zz6.noteit.sync.Sync;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActionMode.Callback, ColorDialog.OnColorSelectedListener {
    private static final String TAG = "NoteIt";
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView.LayoutManager listLayout;
    private RecyclerView.LayoutManager gridLayout;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private NotesManager notes;

    private Note recentlyArchivedNote;
    private int recentlyArchivedNotePosition;

    private SharedPreferences preferences;

    private boolean isBackup = true;

    private Sync sync;

    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent createNote = new Intent(getApplicationContext(), EditNoteActivity.class);
            startActivity(createNote);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !drawer.isDrawerOpen(GravityCompat.START)) {
                    if (actionMode != null)
                        actionMode.finish();
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        notes = new NotesManager();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String msg = "123456";
        String keyStr = "abcdef";
        String ivStr = "ABCDEF";

        Log.d("App", "Before Encrypt: " + msg);

        byte[] ans = new byte[0];
        try {
            ans = AESHelper.encrypt(ivStr, keyStr, msg.getBytes());
            Log.d("App", "After Encrypt: " + new String(ans, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ansBase64 = null;
        try {
            ansBase64 = AESHelper.encryptStrAndToBase64(ivStr, keyStr, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("App", "After Encrypt & To Base64: " + ansBase64);

        byte[] deans;
        try {
            deans = AESHelper.decrypt(ivStr, keyStr, ans);
            Log.d("App", "After Decrypt: " + new String(deans, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String deansBase64 = null;
        try {
            deansBase64 = AESHelper.decryptStrAndFromBase64(ivStr, keyStr, Objects.requireNonNull(ansBase64));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("App", "After Decrypt & From Base64: " + deansBase64);

        setUpRecyclerView();

        listLayout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        gridLayout = new GridLayoutManager(this,
                2);

        int layout = preferences.getInt("recycler_layout", 0);

        if (layout == 0)
            recyclerView.setLayoutManager(listLayout);
        else
            recyclerView.setLayoutManager(gridLayout);

        // Getting SwipeContainerLayout
        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(() -> {
            notes.refresh();
            notesAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        });

        sync = new Sync(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        notes.refresh();
        notesAdapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(notes, this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isMultiSelect) {
                            multiSelect(position);
                        } else {
                            Note note = notes.get(position);
                            Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                            intent.putExtra("note", note);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        if (!isMultiSelect) {
                            selectedIds = new ArrayList<>();
                            isMultiSelect = true;

                            if (actionMode == null) {
                                actionMode = startActionMode(MainActivity.this); //show ActionMode.
                            }
                        }

                        multiSelect(position);
                    }
                })
        );
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void multiSelect(int position) {
        Note note = notesAdapter.getItem(position);
        if (note != null) {
            if (actionMode != null) {
                if (selectedIds.contains(note.getId()))
                    selectedIds.remove(note.getId());
                else
                    selectedIds.add(note.getId());

                if (selectedIds.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
                else {
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                notesAdapter.setSelectedIds(selectedIds);

            }
        }
    }

    public void archiveItem(int position) {
        recentlyArchivedNote = notes.get(position);
        recentlyArchivedNotePosition = position;
        Note note = notes.get(position);
        note.setStatus(STATUS_ARCHIVED);
        NoteDao.updateRecord(note);
        notes.refresh();
        notesAdapter.notifyDataSetChanged();
        showUndoSnackbar();
    }

    public void deleteItem(int position) {
        Note note = notes.get(position);
        NoteDao.deleteRecord(note);
        notes.refresh();
        notesAdapter.notifyDataSetChanged();
    }

    private void showUndoSnackbar() {
        View view = findViewById(R.id.drawer_layout);
        final Snackbar snackbar = Snackbar.make(view, R.string.note_archived, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> {
            undoArchive();
            snackbar.dismiss();
        });
        snackbar.show();
    }

    private void undoArchive() {
        recentlyArchivedNote.setStatus(STATUS_ACTIVE);
        NoteDao.updateRecord(recentlyArchivedNote);
        notes.getAll().add(recentlyArchivedNotePosition,
                recentlyArchivedNote);
        notesAdapter.notifyItemInserted(recentlyArchivedNotePosition);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icons_main, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_backup_Drive:
                isBackup = true;
                sync.connectToDrive(true);
                break;
            case R.id.action_import_Drive:
                isBackup = false;
                sync.connectToDrive(false);
                break;
            case R.id.action_change_layout:
                if (recyclerView.getLayoutManager() == listLayout) {
                    recyclerView.setLayoutManager(gridLayout);
                    item.setIcon(R.drawable.ic_grid);
                } else {
                    recyclerView.setLayoutManager(listLayout);
                    item.setIcon(R.drawable.ic_list);
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            notes.setMode(NotesManager.ACTIVE);
            notes.refresh();
            notesAdapter.notifyDataSetChanged();
        } else if (id == R.id.nav_archive) {
            notes.setMode(NotesManager.ARCHIVED);
            notes.refresh();
            notesAdapter.notifyDataSetChanged();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_select, menu);

        if (notes.getMode() == NotesManager.ACTIVE) {
            menu.findItem(R.id.action_unarchive).setVisible(false);
        } else {
            menu.findItem(R.id.action_archive).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_palette:
                new ColorDialog.Builder(this)
                        .setColorShape(ColorShape.CIRCLE) //CIRCLE or SQUARE
                        .setColorChoices(R.array.color_choices) //an array of colors
                        .setSelectedColor(Color.GREEN) //the checked color
                        .setTag("TAG") // tags can be useful when multiple components use the picker within an activity
                        .show();
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.delete_confirmation))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                for (int id : selectedIds) {
                                    NoteDao.deleteRecord(String.valueOf(id));
                                }

                                notes.refresh();
                                mode.finish();
                                selectedIds.clear();
                                notesAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            case R.id.action_archive:
                for (int id : selectedIds) {
                    Note note = NoteDao.loadRecordById(id);
                    note.setStatus(STATUS_ARCHIVED);
                    NoteDao.updateRecord(note);
                }

                notes.refresh();
                mode.finish();
                selectedIds.clear();
                notesAdapter.notifyDataSetChanged();

                return true;
            case R.id.action_unarchive:
                for (int id : selectedIds) {
                    Note note = NoteDao.loadRecordById(id);
                    note.setStatus(STATUS_ACTIVE);
                    NoteDao.updateRecord(note);
                }

                notes.refresh();
                mode.finish();
                selectedIds.clear();
                notesAdapter.notifyDataSetChanged();

                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        notesAdapter.setSelectedIds(new ArrayList<Integer>());
    }

    @Override
    public void onStop() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("recycler_layout", (recyclerView.getLayoutManager() == listLayout) ? 0 : 1);
        editor.apply();

        super.onStop();
    }

    @Override
    public void onColorSelected(int i, String s) {
        String color = Utils.getColorHex(i);

        for (int id : selectedIds) {
            Note note = NoteDao.loadRecordById(id);
            note.setColor(color);
            NoteDao.updateRecord(note);
        }

        notes.refresh();

        if (actionMode != null)
            actionMode.finish();

        selectedIds.clear();
        notesAdapter.notifyDataSetChanged();
    }

    public int getMode() {
        return notes.getMode();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    sync.connectToDrive(isBackup);
                }
                break;

            case REQUEST_CODE_CREATION:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Backup successfully saved.");
                    Toast.makeText(this, getString(R.string.backup_successfully), Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_OPENING:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    sync.mOpenItemTaskSource.setResult(driveId);
                } else {
                    sync.mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }

        }
    }

    public void refresh() {
        notes.refresh();
        notesAdapter.notifyDataSetChanged();
    }

}
