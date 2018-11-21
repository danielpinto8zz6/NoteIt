package io.github.danielpinto8zz6.noteit.model;

import java.util.ArrayList;

public class NoteIt {
    private ArrayList<Note> notes;
    private ArrayList<Task> tasks;

    public NoteIt() {
        notes = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean addNote(Note note) {
        return notes.add(note);
    }

    public boolean removeNote(Note note) {
        return notes.remove(note);
    }

    public boolean addTask(Task task) {
        return tasks.add(task);
    }

    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

}
