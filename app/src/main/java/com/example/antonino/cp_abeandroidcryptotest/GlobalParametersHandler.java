package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.MessageFormat;

/**
 * Created by antonino on 07/03/16.
 */
public class GlobalParametersHandler extends SQLiteOpenHelper
{
    private static GlobalParametersHandler sInstance;
    private static final String DATABASE_NAME = "GlobParams.db";
    private static final int SCHEMA_VERSION = 1;
    public static synchronized GlobalParametersHandler getInstance(Context context) {
            if (sInstance == null) {
            sInstance = new GlobalParametersHandler(context.getApplicationContext());
        }
        return sInstance;
    }
    private GlobalParametersHandler(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT," + " {2} TEXT);";
        db.execSQL(MessageFormat.format(sql, GlobalParameters.TABLE_NAME, GlobalParameters._ID, GlobalParameters.GLOB_PARAMETERS));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS GlobalParameters");
        onCreate(db);
    }

    public void WriteCurveParameters(String GlobParams)
    {
        ContentValues values = new ContentValues();
        values.put(GlobalParameters.GLOB_PARAMETERS, GlobParams);
        getWritableDatabase().insert(GlobalParameters.TABLE_NAME, null, values);
    }
    public String ReadCurveParameters()
    {
        Cursor cursor = getReadableDatabase().query(GlobalParameters.TABLE_NAME,GlobalParameters.COLUMNS,null,null,null,null,GlobalParameters.GLOB_PARAMETERS);
        String curveparams = null;
        if (cursor.moveToFirst())
        {
            curveparams = cursor.getString(1);
        }
        cursor.close();
        return curveparams;
    }
}
