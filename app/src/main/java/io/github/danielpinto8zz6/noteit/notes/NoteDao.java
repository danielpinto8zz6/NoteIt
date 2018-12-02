/*
Class NoteDao is a class use to place in the database each note.
DAO means: Data Access Object

Author: Daniel Pinto

Creation date: ‎25‎/11/‎2018

Last modification: 02/12/2018
 */

package io.github.danielpinto8zz6.noteit.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Objects;

public class NoteDao extends DbManager {
    private static final String TAG = "NoteDao";

    private static SQLiteDatabase database;
    private static DbManager mDbManager;
    private static final String[] allColumns = DbSchema.Table_Note.allColumns;

    protected NoteDao() {
        //Empty constructor
    }

    private static void databaseOpen() throws SQLException {
        //Open database
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    private static void databaseClose() throws SQLException{
        //Close database
        //Get the instance is needed to knwo what database close, because mDbManager is not initialized out the method
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Note loadRecordById(int mId) {
        //load a Note from the table by its id
        databaseOpen();
        Cursor cursor = database.query(DbSchema.Table_Note.TABLE_NAME, allColumns, "id = ?", new String[]{String.valueOf(mId)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = cursorToNote(Objects.requireNonNull(cursor));

        cursor.close();
        databaseClose();

        return note;
    }

    public static ArrayList<Note> loadAllRecords() {
        //Load all the records and put them in an Array list of notes
        ArrayList<Note> noteList = new ArrayList<>();
        databaseOpen();

        Cursor cursor = database.query(
                DbSchema.Table_Note.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        databaseClose();
        return noteList;
    }

    // Please always use the typed column names (Table_Note) when passing arguments.
    // Example: Table_Note.Column_Name
    public static ArrayList<Note> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        //Load all the records by some parameters, needed for specific queries
        ArrayList<Note> noteList = new ArrayList<Note>();
        databaseOpen();

        if (TextUtils.isEmpty(selection)) {
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Note.TABLE_NAME,
                allColumns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            noteList.add(cursorToNote(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        databaseClose();
        return noteList;
    }

    public static long insertRecord(Note note) {
        //Insert a note in the database
        ContentValues values = getNoteValues(note);
        databaseOpen();
        long insertId = database.insert(DbSchema.Table_Note.TABLE_NAME, null, values);
        databaseClose();
        return insertId;
    }

    public static int updateRecord(Note note) {
        //Update the note info in the database
        ContentValues values = getNoteValues(note);
        databaseOpen();
        String[] where = new String[]{String.valueOf(note.getId())};
        int updatedId = database.update(DbSchema.Table_Note.TABLE_NAME, values, DbSchema.Table_Note.COL_ID + " = ? ", where);
        databaseClose();
        return updatedId;
    }

    public static int deleteRecord(Note note) {
        //Delete that note from the database
        databaseOpen();
        String[] where = new String[]{String.valueOf(note.getId())};
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, DbSchema.Table_Note.COL_ID + " = ? ", where);
        databaseClose();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        //Delete a note from the database selected by its id
        databaseOpen();
        String[] where = new String[]{id};
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, DbSchema.Table_Note.COL_ID + " = ? ", where);
        databaseClose();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        //Delete all the records from the database
        databaseOpen();
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, null, null);
        databaseClose();
        return deletedCount;
    }

    private static ContentValues getNoteValues(Note note) {
        //Get the values from the database of that note
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Note.COL_ID, note.getId());
        values.put(DbSchema.Table_Note.COL_TITLE, note.getTitle());
        values.put(DbSchema.Table_Note.COL_CONTENT, note.getContent());
//        values.put(DbSchema.Table_Note.COL_CREATE_DATE, note.getCreate_date());
        values.put(DbSchema.Table_Note.COL_NOTIFY_DATE, note.getNotify_date());
        values.put(DbSchema.Table_Note.COL_EDITED_DATE, note.getEdited_date());
        values.put(DbSchema.Table_Note.COL_COLOR, note.getColor());
        values.put(DbSchema.Table_Note.COL_STATUS, note.getStatus());
        values.put(DbSchema.Table_Note.COL_IMAGE, note.getImage());
        values.put(DbSchema.Table_Note.COL_TYPE, note.getType());

        return values;
    }

    private static Note cursorToNote(Cursor cursor) {
        //Get the note having its cursor
        Note note = new Note();

        note.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Note.COL_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_CONTENT)));
        note.setCreate_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_CREATE_DATE)));
        note.setNotify_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_NOTIFY_DATE)));
        note.setEdited_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_EDITED_DATE)));
        note.setColor(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Note.COL_COLOR)));
        note.setStatus(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Note.COL_STATUS)));
        note.setImage(cursor.getBlob(cursor.getColumnIndex(DbSchema.Table_Note.COL_IMAGE)));
        note.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Note.COL_TYPE)));

        return note;
    }


}

