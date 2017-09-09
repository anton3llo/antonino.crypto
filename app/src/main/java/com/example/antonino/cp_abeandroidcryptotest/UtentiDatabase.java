package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by antonino on 05/04/16.
 */
public class UtentiDatabase extends SQLiteOpenHelper {
    private static UtentiDatabase sInstance;
    private static final String DATABASE_NAME = "Utenti.db";
    private static final int SCHEMA_VERSION = 1;
    private static final String TABLE_NAME = "Utenti";
    private static final String COLUMN_ID ="_ID";
    private static final String USER_NAME = "UserName";
    public static synchronized UtentiDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UtentiDatabase(context.getApplicationContext());
        }
        return sInstance;
    }
    public void InsertUser(String utente)
    {
        ContentValues values = new ContentValues();
        values.put("UserName",utente);
        getWritableDatabase().insert(TABLE_NAME,null,values);
    }
    public Cursor LeggiUser()
    {
        return getReadableDatabase().query(TABLE_NAME,null,null,null,null,null,null);
    }
    public UtentiDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" integer primary key autoincrement, "+USER_NAME+" text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
