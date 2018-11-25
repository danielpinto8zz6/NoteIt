package io.github.danielpinto8zz6.noteit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;

public class EditNoteActivity extends AppCompatActivity {
    private int id;
    private boolean editing = false;
    private TextView titleTv;
    private TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleTv = findViewById(R.id.note_title_input);
        contentTv = findViewById(R.id.note_content_input);

        Intent intent = getIntent(); // gets the previously created intent
        id = intent.getIntExtra("id", -1);

        if (id != -1) {
            editing = true;

            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");

            titleTv.setText(title);
            contentTv.setText(content);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (editing) {
            updateNote();
        } else {
            addNote();
        }

        super.onBackPressed();
    }

    private void addNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        if (title.isEmpty() && content.isEmpty())
            return;

        Note note = new Note ();
        note.setTitle(title);
        note.setContent(content);

        NoteDao.insertRecord(note);
    }

    private void updateNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        Note note = NoteDao.loadRecordById(id);
        note.setTitle(title);
        note.setContent(content);

        NoteDao.updateRecord(note);

        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
    }
}
