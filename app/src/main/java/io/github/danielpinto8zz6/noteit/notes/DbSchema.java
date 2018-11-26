package io.github.danielpinto8zz6.noteit.notes;

import android.provider.BaseColumns;

class DbSchema {
    private static final String TAG = "DbSchema";

    public static final String DATABASE_NAME = "NoteIt";
    public static final int DATABASE_VERSION = 1;
    private static final String SORT_ASC = " ASC";
    private static final String SORT_DESC = " DESC";
    public static final String[] ORDERS = {SORT_ASC, SORT_DESC};
    public static final int OFF = 0;
    public static final int ON = 1;

    public static final class Table_Note implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "notes";

        // Table Columns
        public static final String COL_ID = "id";
        public static final String COL_TITLE = "title";
        public static final String COL_CONTENT = "content";
        public static final String COL_CREATE_DATE = "create_date";
        public static final String COL_NOTIFY_DATE = "notify_date";
        public static final String COL_EDITED_DATE = "edited_date";
        public static final String COL_COLOR = "color";
        public static final String COL_STATUS = "status";
        public static final String COL_IMAGE = "image";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE notes ( " +
                COL_ID + " INTEGER PRIMARY KEY,  " +
                COL_TITLE + " TEXT," +
                COL_CONTENT + " TEXT," +
                COL_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                COL_NOTIFY_DATE + " DATETIME," +
                COL_EDITED_DATE + " DATETIME," +
                COL_COLOR + " TEXT," +
                COL_STATUS + " INTEGER DEFAULT 0," +
                COL_IMAGE + " TEXT );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS note;";

        // Columns list array
        public static final String[] allColumns = {
                COL_ID,
                COL_TITLE,
                COL_CONTENT,
                COL_CREATE_DATE,
                COL_NOTIFY_DATE,
                COL_EDITED_DATE,
                COL_COLOR,
                COL_STATUS,
                COL_IMAGE};
    }

}
