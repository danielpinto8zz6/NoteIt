package io.github.danielpinto8zz6.noteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.danielpinto8zz6.noteit.model.Note;

import static io.github.danielpinto8zz6.noteit.Constants.DATABASE_NAME;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_COLOR;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_CONTENT;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_CREATE_DATE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_ID;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_IMAGE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_NOTIFY_DATE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_STATUS;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_COLUMN_TITLE;
import static io.github.danielpinto8zz6.noteit.Constants.INPUT_TABLE_NAME;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + INPUT_TABLE_NAME + "(" +
                INPUT_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                INPUT_COLUMN_TITLE + " TEXT, " +
                INPUT_COLUMN_CONTENT + " TEXT, " +
                INPUT_COLUMN_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                INPUT_COLUMN_NOTIFY_DATE + " DATETIME, " +
                INPUT_COLUMN_COLOR + " TEXT, " +
                INPUT_COLUMN_STATUS + " INTEGER, " +
                INPUT_COLUMN_IMAGE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INPUT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNote(String title, String text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INPUT_COLUMN_TITLE, title);
        contentValues.put(INPUT_COLUMN_CONTENT, text);
        db.insert(INPUT_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INPUT_COLUMN_TITLE, note.getTitle());
        contentValues.put(INPUT_COLUMN_CONTENT, note.getContent());
        contentValues.put(INPUT_COLUMN_NOTIFY_DATE, note.getNotifyDate());
        contentValues.put(INPUT_COLUMN_COLOR, note.getColor());
        contentValues.put(INPUT_COLUMN_STATUS, note.getStatus());
        contentValues.put(INPUT_COLUMN_IMAGE, note.getImage());
        db.insert(INPUT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + INPUT_TABLE_NAME, null);
        return res;
    }

    public Cursor getNote(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + INPUT_TABLE_NAME + " WHERE " +
                INPUT_COLUMN_ID + "=?", new String[]{id});
        return res;
    }

    public void deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INPUT_TABLE_NAME, INPUT_COLUMN_ID + "=?", new String[]{id});
    }

    public boolean updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(INPUT_COLUMN_TITLE, note.getTitle());
        contentValues.put(INPUT_COLUMN_CONTENT, note.getContent());
        contentValues.put(INPUT_COLUMN_NOTIFY_DATE, note.getNotifyDate());
        contentValues.put(INPUT_COLUMN_COLOR, note.getColor());
        contentValues.put(INPUT_COLUMN_STATUS, note.getStatus());
        contentValues.put(INPUT_COLUMN_IMAGE, note.getImage());

        db.update(INPUT_TABLE_NAME, contentValues, INPUT_COLUMN_ID + " = ? ", new String[]{note.getId()});
        return true;
    }
}
