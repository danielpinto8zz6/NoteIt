package io.github.danielpinto8zz6.noteit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.github.danielpinto8zz6.noteit.encryption.AESHelper;
import io.github.danielpinto8zz6.noteit.model.Note;

import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_COLOR;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_CONTENT;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_CREATE_DATE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_ID;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_IMAGE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_NOTIFY_DATE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_STATUS;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_TITLE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SwipeRefreshLayout swipeLayout;
    DBHelper db;
    private RecyclerView.LayoutManager listLayout;
    private RecyclerView.LayoutManager gridLayout;
    private RecyclerView recyclerView;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNote = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(createNote);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new DBHelper(getApplicationContext());

        loadNotes();

        String msg = "123456";
        String keyStr = "abcdef";
        String ivStr = "ABCDEF";

        Log.d("App", "Before Encrypt: " + msg);

        byte[] ans = new byte[0];
        try {
            ans = AESHelper.encrypt(ivStr, keyStr, msg.getBytes());
            Log.d("App", "After Encrypt: " + new String(ans, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String deansBase64 = null;
        try {
            deansBase64 = AESHelper.decryptStrAndFromBase64(ivStr, keyStr, ansBase64);
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
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotes();
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadNotes();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new NotesAdapter(notes, this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Note note = notes.get(position);
                        Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                        intent.putExtra("id", note.getId());
                        intent.putExtra("title", note.getTitle());
                        intent.putExtra("content", note.getContent());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback((NotesAdapter) recyclerView.getAdapter()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void loadNotes() {
        notes.clear();
        Cursor cursor = db.getNotes();
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Note note = new Note();

                note.setId(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_CONTENT)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(INPUT_COLUMN_STATUS)));
                note.setCreateDate(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_CREATE_DATE)));
                note.setNotifyDate(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_NOTIFY_DATE)));
                note.setColor(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_COLOR)));
                note.setColor(cursor.getString(cursor.getColumnIndex(INPUT_COLUMN_IMAGE)));

                if (note.getStatus() == STATUS_ACTIVE) {
                    notes.add(note);
                }

                cursor.moveToNext();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
