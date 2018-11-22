package io.github.danielpinto8zz6.noteit.model;

import android.graphics.Color;
import android.media.Image;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Note implements Serializable {
    private String title;
    private String description;
    private Date createDate;
    private Date notifyDate;
    private Color color;
    private Status status;
    private Image image;

    public Note() {
        this.status = Status.ACTIVE;
        this.createDate = Calendar.getInstance().getTime();
    }

    public Note(String title, String description) {
        this.status = Status.ACTIVE;
        this.createDate = Calendar.getInstance().getTime();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum Status {
        ACTIVE, ARCHIVED, DELETED
    }
}
