package io.github.danielpinto8zz6.noteit.model;

import android.graphics.Color;
import android.media.Image;

import java.util.Date;

public class Note {
    private String title;
    private String text;
    private Date createDate;
    private Date notifyDate;
    private Color color;
    private Status status;
    private Image image;

    public Note() {
        this.status = Status.ACTIVE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public enum Status {
        ACTIVE, ARCHIVED, DELETED
    }
}
