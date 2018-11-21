package io.github.danielpinto8zz6.noteit.model;

import java.util.ArrayList;

public class Task extends Note {
    private ArrayList<String>  tasks;

    public Task() {
        tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    public ArrayList<String> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }
}
