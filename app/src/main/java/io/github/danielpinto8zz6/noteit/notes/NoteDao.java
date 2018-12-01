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
    }

    private static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    private static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Note loadRecordById(int mid) {
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Note.TABLE_NAME, allColumns, "id = ?", new String[]{String.valueOf(mid)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note();
        note = cursorToNote(Objects.requireNonNull(cursor));

        cursor.close();
        database_close();

        return note;
    }

    public static ArrayList<Note> loadAllRecords() {
        ArrayList<Note> noteList = new ArrayList<>();
        database_open();

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
        database_close();
        return noteList;
    }

    // Please always use the typed column names (Table_Note) when passing arguments.
    // Example: Table_Note.Column_Name
    public static ArrayList<Note> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        database_open();

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
            Note note = cursorToNote(cursor);
            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return noteList;
    }

    public static long insertRecord(Note note) {
        ContentValues values = new ContentValues();
        values = getNoteValues(note);
        database_open();
        long insertId = database.insert(DbSchema.Table_Note.TABLE_NAME, null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Note note) {
        ContentValues values = new ContentValues();
        values = getNoteValues(note);
        database_open();
        String[] where = new String[]{String.valueOf(note.getId())};
        int updatedId = database.update(DbSchema.Table_Note.TABLE_NAME, values, DbSchema.Table_Note.COL_ID + " = ? ", where);
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Note note) {
        database_open();
        String[] where = new String[]{String.valueOf(note.getId())};
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, DbSchema.Table_Note.COL_ID + " = ? ", where);
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[]{id};
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, DbSchema.Table_Note.COL_ID + " = ? ", where);
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Note.TABLE_NAME, null, null);
        database_close();
        return deletedCount;
    }

    private static ContentValues getNoteValues(Note note) {
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

