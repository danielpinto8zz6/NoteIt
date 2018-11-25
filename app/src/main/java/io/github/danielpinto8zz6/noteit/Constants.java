package io.github.danielpinto8zz6.noteit;

public class Constants {
    public static final String DATABASE_NAME = "SQLiteNoteIt.db";
    public static final String INPUT_TABLE_NAME = "notes";
    public static final String INPUT_COLUMN_ID = "_id";
    public static final String INPUT_COLUMN_TITLE = "title";
    public static final String INPUT_COLUMN_CONTENT = "content";
    public static final String INPUT_COLUMN_CREATE_DATE = "create_date";
    public static final String INPUT_COLUMN_NOTIFY_DATE = "notify_date";
    public static final String INPUT_COLUMN_COLOR = "color";
    public static final String INPUT_COLUMN_STATUS = "status";
    public static final String INPUT_COLUMN_IMAGE = "image";

    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_FILED = 1;
    public static final int STATUS_DELETED = 2;
}