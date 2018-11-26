package io.github.danielpinto8zz6.noteit.notes;

import java.util.ArrayList;

public class NotesManager {
    public static final int ACTIVE = 0;
    public static final int ARCHIVED = 1;

    private int mode = 0;

    private ArrayList<Note> notes;
    private ArrayList<Note> active;
    private ArrayList<Note> archived;

    public NotesManager() {
        active = new ArrayList<>();
        notes = new ArrayList<>();
        archived = new ArrayList<>();

        refresh();
    }

    public Note get(int position) {
        switch (mode) {
            case 0:
                return active.get(position);
            case 1:
                return archived.get(position);
        }
        return notes.get(position);
    }

    public int size() {
        switch (mode) {
            case 0:
                return active.size();
            case 1:
                return archived.size();
        }
        return notes.size();
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public ArrayList<Note> getActive() {
        return active;
    }

    public ArrayList<Note> getArchived() {
        return archived;
    }

    public void refresh() {
        notes.clear();
        active.clear();
        archived.clear();

        notes = NoteDao.loadAllRecords();
        active = NoteDao.loadAllRecords("status = ?", new String[]{"0"}, null, null, null);
        archived = NoteDao.loadAllRecords("status = ?", new String[]{"1"}, null, null, null);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
