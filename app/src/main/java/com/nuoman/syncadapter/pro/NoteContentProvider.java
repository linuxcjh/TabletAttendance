package com.nuoman.syncadapter.pro;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * AUTHOR: Alex
 * DATE: 14/8/2016 11:29
 */
public class NoteContentProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher;

    private static final int COLLECTION_INDICATOR = 1;

    private static final int SINGLE_INDICATOR = 2;

    private static HashMap<String, String> sNotesProjectionMap;

    private DatabaseHelper mDbHelper;


    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(NoteProviderMetaData.AUTHORITY, "notes", COLLECTION_INDICATOR);
        sUriMatcher.addURI(NoteProviderMetaData.AUTHORITY, "notes/#", SINGLE_INDICATOR);

        sNotesProjectionMap = new HashMap<>();
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData._ID, NoteProviderMetaData.NoteTableMetaData._ID);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.NOTE_TITLE, NoteProviderMetaData.NoteTableMetaData.NOTE_TITLE);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT, NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.CREATE_DATE, NoteProviderMetaData.NoteTableMetaData.CREATE_DATE);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_CARD_NO, NoteProviderMetaData.NoteTableMetaData.PUNCH_CARD_NO);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_IMAGE_PATH, NoteProviderMetaData.NoteTableMetaData.PUNCH_IMAGE_PATH);
        sNotesProjectionMap.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_TIME, NoteProviderMetaData.NoteTableMetaData.PUNCH_TIME);


    }

    @Override
    public boolean onCreate() {

        mDbHelper = new DatabaseHelper(getContext(), NoteProviderMetaData.DATABASE_NAME, null, NoteProviderMetaData.DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        switch (sUriMatcher.match(uri)) {

            case COLLECTION_INDICATOR:
                queryBuilder.setTables(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME);
                queryBuilder.setProjectionMap(sNotesProjectionMap);

                break;

            case SINGLE_INDICATOR:
                queryBuilder.setTables(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME);
                queryBuilder.setProjectionMap(sNotesProjectionMap);
                queryBuilder.appendWhere(NoteProviderMetaData.NoteTableMetaData._ID + "=" + uri.getPathSegments().get(1));

                break;

            default:
                throw new IllegalArgumentException("UnKnow URI: " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = NoteProviderMetaData.NoteTableMetaData.DEFAULT_ORDERBY;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (sUriMatcher.match(uri) != COLLECTION_INDICATOR) {
            throw new IllegalArgumentException("UnKnow URI : " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowID = db.insert(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME, null, values);

        if (rowID > 0) {
            Uri retUri = ContentUris.withAppendedId(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, rowID);
            return retUri;
        }


        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = -1;
        switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                count = db.delete(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME, selection, selectionArgs);
                break;
            case SINGLE_INDICATOR:
                String rowID = uri.getPathSegments().get(1);
                count = db.delete(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME, NoteProviderMetaData.NoteTableMetaData._ID + "=" + rowID, null);
                break;

            default:
                throw new IllegalArgumentException("UnKnow URI: " + uri);
        }

        this.getContext().getContentResolver().notifyChange(uri, null);


        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = -1;

        switch (sUriMatcher.match(uri)) {
            case COLLECTION_INDICATOR:
                count = db.update(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME, values, null, null);
                break;
            case SINGLE_INDICATOR:
                String rowID = uri.getPathSegments().get(1);
                count = db.update(NoteProviderMetaData.NoteTableMetaData.TABLE_NAME, values, NoteProviderMetaData.NoteTableMetaData._ID + "=" + rowID, null);

                break;

            default:
                throw new IllegalArgumentException("UnKnow URI: " + uri);
        }

        this.getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("SYNC", "create table: " + NoteProviderMetaData.NoteTableMetaData.SQL_CREATE_TABLE);
            db.execSQL(NoteProviderMetaData.NoteTableMetaData.SQL_CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + NoteProviderMetaData.NoteTableMetaData.TABLE_NAME);

        }
    }
}
