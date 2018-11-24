package io.github.danielpinto8zz6.noteit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.github.danielpinto8zz6.noteit.model.Note;

public class CreateNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        addNote();

        super.onBackPressed();
    }

    public void addNote() {
        TextView titleTv = findViewById(R.id.note_title_input);
        TextView descriptionTv = findViewById(R.id.note_description_input);

        String title = titleTv.getText().toString();
        String description = descriptionTv.getText().toString();

        if (title.isEmpty())
            return;

        NoteIt noteIt = (NoteIt) getApplication();
        noteIt.addNote(new Note(title, description));

        Log.d("NoteIt", "Note added!");
    }

    public void addNoteTest(String title, String description) {
        if (title.isEmpty())
            return;

        NoteIt noteIt = (NoteIt) getApplication();
        noteIt.addNote(new Note(title, description));
    }
}
