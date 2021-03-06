/*
Class Note is a class use to define each note (content, title, color, creation date...)

Author: Daniel Pinto

Creation date: ‎25‎/11/‎2018

Last modification: 02/12/2018
 */

package io.github.danielpinto8zz6.noteit.notes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Objects;

public class Note implements Parcelable {
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";
    private static final String COL_CREATE_DATE = "create_date";
    private static final String COL_NOTIFY_DATE = "notify_date";
    private static final String COL_EDITED_DATE = "notify_date";
    private static final String COL_COLOR = "color";
    private static final String COL_STATUS = "status";
    private static final String COL_IMAGE = "image";
    private static final String COL_TYPE = "type";

    private Integer id;
    private String title = "";

    private String content = "";

    private String create_date = "";
    private String notify_date = "";

    private String edited_date = "";
    private String color = "";
    private Integer status = 0;
    private byte[] image;

    private Integer type = 0;

    public Note() {
        //Empty constructor
    }

    public Note(Integer id, String title, String content, String create_date, String notify_date, String edited_date, String color, Integer status, byte[] image, Integer type) {
        //Constructor filling all the attributes of a note
        this.id = id;
        this.title = title;
        this.content = content;
        this.create_date = create_date;
        this.notify_date = notify_date;
        this.edited_date = edited_date;
        this.color = color;
        this.status = status;
        this.image = image;
        this.type = type;
    }

    public Note(Bundle b) {
        //A constructor filling the attributes of a note with a Bundle
        if (b != null) {
            this.id = b.getInt(COL_ID);
            this.title = b.getString(COL_TITLE);
            this.content = b.getString(COL_CONTENT);
            this.create_date = b.getString(COL_CREATE_DATE);
            this.notify_date = b.getString(COL_NOTIFY_DATE);
            this.edited_date = b.getString(COL_EDITED_DATE);
            this.color = b.getString(COL_COLOR);
            this.status = b.getInt(COL_STATUS);
            this.image = b.getByteArray(COL_IMAGE);
            this.type = b.getInt(COL_TYPE);
        }
    }

    /*
    This part are getters and setters from the Note attributes
     */
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEdited_date() {
        return edited_date;
    }

    public void setEdited_date(String edited_date) {
        this.edited_date = edited_date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return (title.isEmpty() && content.isEmpty() && color.isEmpty() && notify_date.isEmpty());
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putInt(COL_ID, this.id);
        b.putString(COL_TITLE, this.title);
        b.putString(COL_CONTENT, this.content);
        b.putString(COL_CREATE_DATE, this.create_date);
        b.putString(COL_NOTIFY_DATE, this.notify_date);
        b.putString(COL_EDITED_DATE, this.notify_date);
        b.putString(COL_COLOR, this.color);
        b.putInt(COL_STATUS, this.status);
        b.putByteArray(COL_IMAGE, this.image);
        b.putInt(COL_TYPE, this.type);
        return b;
    }

    @NonNull
    @Override
    public String toString() {
        //To string method to print the data
        return "Note{" +
                " mid=" + id +
                ", mtitle='" + title + '\'' +
                ", mcontent='" + content + '\'' +
                ", mcreate_date='" + create_date + '\'' +
                ", mnotify_date='" + notify_date + '\'' +
                ", medited_date='" + edited_date + '\'' +
                ", mcolor='" + color + '\'' +
                ", mstatus=" + status +
                ", mimage='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    /*
    This part was not part of the inspection and its still in development
    Not finish
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.create_date);
        dest.writeString(this.notify_date);
        dest.writeString(this.edited_date);
        dest.writeString(this.color);
        dest.writeValue(this.status);
        dest.writeByteArray(this.image);
        dest.writeValue(this.type);
    }

    protected Note(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.create_date = in.readString();
        this.notify_date = in.readString();
        this.edited_date = in.readString();
        this.color = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.createByteArray();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) &&
                Objects.equals(title, note.title) &&
                Objects.equals(content, note.content) &&
                Objects.equals(create_date, note.create_date) &&
                Objects.equals(notify_date, note.notify_date) &&
                Objects.equals(edited_date, note.edited_date) &&
                Objects.equals(color, note.color) &&
                Objects.equals(status, note.status) &&
                Arrays.equals(image, note.image) &&
                Objects.equals(type, note.type);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, title, content, create_date, notify_date, edited_date, color, status, type);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}