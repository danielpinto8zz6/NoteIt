package io.github.danielpinto8zz6.noteit.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.encryption.AESHelper;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.notes.NotesManager;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActionMode.Callback {
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView.LayoutManager listLayout;
    private RecyclerView.LayoutManager gridLayout;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private Toolbar toolbar;

    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private NotesManager notes;

    private Note recentlyArchivedNote;
    private int recentlyArchivedNotePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
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

        recyclerView.setLayoutManager(gridLayout);

        // Getting SwipeContainerLayout
        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(() -> {
            notes.refresh();
            notesAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        });
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
                            intent.putExtra("note", (Note) note);
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
                ItemTouchHelper(new SwipeToDeleteCallback(this, (NotesAdapter) recyclerView.getAdapter()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void multiSelect(int position) {
        Note note = notesAdapter.getItem(position);
        if (note != null) {
            if (actionMode != null) {
                if (selectedIds.contains(note.getId()))
                    selectedIds.remove(Integer.valueOf(note.getId()));
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
        notes.getAll().remove(notes.get(position));
        notesAdapter.notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = findViewById(R.id.drawer_layout);
        final Snackbar snackbar = Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + R.string.note_archived + "</font>"),
                Snackbar.LENGTH_LONG);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_change_layout) {
            if (recyclerView.getLayoutManager() == listLayout) {
                recyclerView.setLayoutManager(gridLayout);
                item.setIcon(R.drawable.ic_grid);
            } else {
                recyclerView.setLayoutManager(listLayout);
                item.setIcon(R.drawable.ic_list);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
}
