package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antonino on 05/04/16.
 */
public class AuthoritiesDatabase extends SQLiteOpenHelper {
    private static AuthoritiesDatabase sInstance;
    private static final String DATABASE_NAME = "Authorities.db";
    private static final int SCHEMA_VERSION = 1;
    private static final String TABLE_NAME = "Authorities";
    private static final String COLUMN_ID ="_ID";
    private static final String AUTH_NAME = "AuthorityName";
    public static synchronized AuthoritiesDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AuthoritiesDatabase(context.getApplicationContext());
        }
        return sInstance;
    }
    public void InsertAuthority(String authority)
    {
        ContentValues values = new ContentValues();
        values.put("AuthorityName",authority);
        getWritableDatabase().insert(TABLE_NAME,null,values);
    }
    public Cursor LeggiAuthority()
    {
        return getReadableDatabase().query(TABLE_NAME,null,null,null,null,null,null);
    }
    public AuthoritiesDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" integer primary key autoincrement, "+AUTH_NAME+" text not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
