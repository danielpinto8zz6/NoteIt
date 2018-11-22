package io.github.danielpinto8zz6.noteit;

import android.app.Application;

import java.util.ArrayList;

import io.github.danielpinto8zz6.noteit.model.Note;

public class NoteIt extends Application {
    private ArrayList<Note> notes;

    public NoteIt() {
        notes = new ArrayList<>();
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public boolean addNote(Note note) {
        return notes.add(note);
    }

    public boolean removeNote(Note note) {
        return notes.remove(note);
    }
}
