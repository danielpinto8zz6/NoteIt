package io.github.danielpinto8zz6.noteit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import io.github.danielpinto8zz6.noteit.model.Note;

public class EditNoteActivity extends AppCompatActivity {
    DBHelper db;
    private String id;
    private boolean editing = false;
    private TextView titleTv;
    private TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleTv = findViewById(R.id.note_title_input);
        contentTv = findViewById(R.id.note_content_input);

        db = new DBHelper(getApplicationContext());

        Intent intent = getIntent(); // gets the previously created intent
        id = intent.getStringExtra("id");

        /**
         * Editing note
         */
        if (id != null) {
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

    public void addNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        if (title.isEmpty() && content.isEmpty())
            return;

        db.insertNote(title, content);
        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
    }

    public void updateNote() {
        String title = titleTv.getText().toString();
        String content = contentTv.getText().toString();

        db.updateNote(new Note(id, title, content));

        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
    }
}
