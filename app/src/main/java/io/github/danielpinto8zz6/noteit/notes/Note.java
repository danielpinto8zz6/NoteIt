package io.github.danielpinto8zz6.noteit.notes;

import android.os.Bundle;

public class Note {

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_CREATE_DATE = "create_date";
    public static final String COL_NOTIFY_DATE = "notify_date";
    public static final String COL_COLOR = "color";
    public static final String COL_STATUS = "status";
    public static final String COL_IMAGE = "image";

    private Integer id;
    private String title;
    private String content;

    private String create_date;
    private String notify_date;
    private String color;
    private Integer status;
    private String image;

    public Note() {
    }

    public Note(Integer id, String title, String content, String create_date, String notify_date, String color, Integer status, String image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.create_date = create_date;
        this.notify_date = notify_date;
        this.color = color;
        this.status = status;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putInt(COL_ID, this.id);
        b.putString(COL_TITLE, this.title);
        b.putString(COL_CONTENT, this.content);
        b.putString(COL_CREATE_DATE, this.create_date);
        b.putString(COL_NOTIFY_DATE, this.notify_date);
        b.putString(COL_COLOR, this.color);
        b.putInt(COL_STATUS, this.status);
        b.putString(COL_IMAGE, this.image);
        return b;
    }

    public Note(Bundle b) {
        if (b != null) {
            this.id = b.getInt(COL_ID);
            this.title = b.getString(COL_TITLE);
            this.content = b.getString(COL_CONTENT);
            this.create_date = b.getString(COL_CREATE_DATE);
            this.notify_date = b.getString(COL_NOTIFY_DATE);
            this.color = b.getString(COL_COLOR);
            this.status = b.getInt(COL_STATUS);
            this.image = b.getString(COL_IMAGE);
        }
    }

    @Override
    public String toString() {
        return "Note{" +
                " mid=" + id +
                ", mtitle='" + title + '\'' +
                ", mcontent='" + content + '\'' +
                ", mcreate_date='" + create_date + '\'' +
                ", mnotify_date='" + notify_date + '\'' +
                ", mcolor='" + color + '\'' +
                ", mstatus=" + status +
                ", mimage='" + image + '\'' +
                '}';
    }


}
