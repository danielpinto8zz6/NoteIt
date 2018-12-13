/*
Class Note is a class use to define each note (content, title, color, creation date...)

Author: Daniel Pinto

Creation date: ‎25‎/11/‎2018

Last modification: 02/12/2018
 */

package danielpinto8zz6.github.io.noteitdesktop.notes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import danielpinto8zz6.github.io.noteitdesktop.Utils;

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

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
        // Empty constructor
    }

    public Note(Integer id, String title, String content, String create_date, String notify_date, String edited_date,
            String color, Integer status, byte[] image, Integer type) {
        // Constructor filling all the attributes of a note
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

    public Note(String title, String content, String color) {
        this.title = title;
        this.content = content;
        this.create_date = Utils.getCurrentDateTime();
        this.color = color;
    }

    /*
     * This part are getters and setters from the Note attributes
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Note))
            return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(title, note.title) && Objects.equals(content, note.content)
                && Objects.equals(create_date, note.create_date) && Objects.equals(notify_date, note.notify_date)
                && Objects.equals(edited_date, note.edited_date) && Objects.equals(color, note.color)
                && Objects.equals(status, note.status) && Arrays.equals(image, note.image)
                && Objects.equals(type, note.type);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, title, content, create_date, notify_date, edited_date, color, status, type);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        String noteStr = "<font color=" + color + "><b>Title</b> [" + title + "] <b>Content</b> [" + content
                + "] <b>CreateDate</b> [" + create_date + "]</font>";
        return noteStr;
    }
}