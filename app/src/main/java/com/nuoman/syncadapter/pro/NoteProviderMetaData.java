package com.nuoman.syncadapter.pro;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * AUTHOR: Alex
 * DATE: 14/8/2016 11:38
 */
public class NoteProviderMetaData {

    public static final String AUTHORITY = "com.nuoman.syncadapter.pro.provider";

    public static final String DATABASE_NAME = "note.db";

    public static final int DATABASE_VERSION = 1;

    public static final class NoteTableMetaData implements BaseColumns {

        public static final String TABLE_NAME = "notes";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nuoman.note";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nuoman.note";

        public static final String NOTE_TITLE = "title";

        public static final String NOTE_CONTENT = "content";

        public static final String CREATE_DATE = "create_date";

        public static final String DEFAULT_ORDERBY = "create_date DESC";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + NOTE_TITLE + " VARCHAR(50),"
                + NOTE_CONTENT + " TEXT,"
                + CREATE_DATE + " INTEGER"
                + ");";


    }

}
