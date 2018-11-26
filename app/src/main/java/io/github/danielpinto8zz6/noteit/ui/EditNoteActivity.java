package io.github.danielpinto8zz6.noteit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.Utils;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_DELETED;

public class EditNoteActivity extends AppCompatActivity {
    private Note note;
    private boolean editing = false;
    private TextView titleTv;
    private TextView contentTv;
    private TextView dateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

        titleTv = findViewById(R.id.note_title_input);
        contentTv = findViewById(R.id.note_content_input);
        dateTv = findViewById(R.id.note_date);

        dateTv.setText(Utils.getCurrentDateTime());

        Intent intent = getIntent(); // gets the previously created intent
        note = (Note) intent.getParcelableExtra("note");

        if (note != null) {
            editing = true;

            titleTv.setText(note.getTitle());
            contentTv.setText(note.getContent());
            String editedDate = note.getEdited_date();
            if (editedDate != null)
                dateTv.setText(editedDate);
            else
                dateTv.setText(note.getCreate_date());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icons_edit_note, menu);

        if (note != null && note.getStatus() != STATUS_ACTIVE) {
            menu.findItem(R.id.action_archive).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_archive) {
            // TODO: archive, delete
            note.setStatus(STATUS_ARCHIVED);
        } else if (id == R.id.action_delete) {
            note.setStatus(STATUS_DELETED);
        } else if (id == R.id.action_add_alert) {
        }

        return super.onOptionsItemSelected(item);
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
        if (editing) {
            updateNote();
        } else {
            addNote();
            editing = true;
        }

        super.onPause();
    }

    private void addNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        if (title.isEmpty() && content.isEmpty())
            return;

        note = new Note();
        note.setTitle(title);
        note.setContent(content);

        NoteDao.insertRecord(note);
    }

    private void updateNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        String orig = note.toString();
        note.setTitle(title);
        note.setContent(content);

        if (!orig.equals(note.toString())) {
            note.setEdited_date(Utils.getCurrentDateTime());
            NoteDao.updateRecord(note);
        }
    }
}
