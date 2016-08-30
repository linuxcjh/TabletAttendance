package com.nuoman;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.nuoman.syncadapter.pro.NoteProviderMetaData;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.BaseActivity;

public class MainActivity extends BaseActivity {


    public static final String ACCOUNT_TYPE = "example.com";

    public static final String ACCOUNT = "dummyaccount";


    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    private Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        mAccount = CreateSyncAccount(this);

        ContentResolver.setSyncAutomatically(mAccount, NoteProviderMetaData.AUTHORITY, true);
        ContentResolver.addPeriodicSync(mAccount, NoteProviderMetaData.AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        requestSync();
//        insert();
//        query();
//        update();
    }


    private void insert() {

        ContentValues values = new ContentValues();

        values.put(NoteProviderMetaData.NoteTableMetaData.NOTE_TITLE, "hello");
        values.put(NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT, "My name is Alex");
        values.put(NoteProviderMetaData.NoteTableMetaData.CREATE_DATE, System.currentTimeMillis());
        Uri uri = this.getContentResolver().insert(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, values);

        Log.d("SYNC", uri.toString());

    }


    private void query() {

        Cursor cursor = this.getContentResolver().query(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData._ID));

            String title = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.NOTE_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT));
            long createDate = cursor.getLong(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.CREATE_DATE));

            Log.d("SYNC", "id: " + id);
            Log.d("SYNC", "title: " + title);
            Log.d("SYNC", "content: " + content);
            Log.d("SYNC", "createDate: " + createDate);


            cursor.moveToNext();
        }

        cursor.close();
    }

    private void update() {

        ContentValues values = new ContentValues();
        values.put(NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT, "name is ChenJianhui");
        int count = this.getContentResolver().update(Uri.parse("content://" + NoteProviderMetaData.AUTHORITY + "/notes" + "/1"), values, null, null);
        Log.d("SYNC", "update:" + count);

        query();


    }


    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d("SYNC", "--SUCCESS--");
        } else {
            Log.d("SYNC", "--ERROR--");
        }

        return newAccount;
    }


    /**
     * 手动触发
     */
    private void requestSync() {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, NoteProviderMetaData.AUTHORITY, b);
    }
}
