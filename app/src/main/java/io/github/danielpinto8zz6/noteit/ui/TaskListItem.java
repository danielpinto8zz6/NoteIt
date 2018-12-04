package io.github.danielpinto8zz6.noteit.ui;

public class TaskListItem {
    private boolean isChecked = false;
    private String caption = "";

    public TaskListItem(boolean isChecked, String caption) {
        this.isChecked = isChecked;
        this.caption = caption;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}