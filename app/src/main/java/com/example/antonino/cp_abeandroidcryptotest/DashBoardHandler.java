package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

/**
 * Created by antonino on 11/04/16.
 */
public class DashBoardHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DashBoard.db";
    private static final int SCHEMA_VERSION = 1;
    public DashBoardHandler(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT," + " {2} TEXT,{3} TEXT,{4} TEXT,{5} TEXT,{6} TEXT,{7} TEXT,{8} TEXT,{9} TEXT);";
        db.execSQL(MessageFormat.format(sql, DashBoard.TABLE_NAME, DashBoard._ID, DashBoard.INDICE, DashBoard.USER_NAME, DashBoard.ACCESS_STRUCTURE, DashBoard.C0, DashBoard.C1, DashBoard.C2, DashBoard.C3, DashBoard.AES_KEY));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void ScriviCiphertext(String indice,String utente,String accessStructure,String C0,String C1,String C2,String C3,String AES_KEY)
    {
        ContentValues values = new ContentValues();
        values.put("Indice",indice);
        values.put("Utente",utente);
        values.put("AccessStructure",accessStructure);
        values.put("C0",C0);
        values.put("C1",C1);
        values.put("C2",C2);
        values.put("C3",C3);
        values.put("AES_KEY",AES_KEY);
        getReadableDatabase().insert(DashBoard.TABLE_NAME,null,values);
    }
    public Cursor LeggiCiphertext(String utente)
    {
        Cursor cursor = getReadableDatabase().query(DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"= '"+utente+"'",null,null,null,DashBoard._ID,null);
        return cursor;
    }
    public HashMap<String,byte[]> ReadALLC0(String user) throws IOException
    {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME + "='" + user + "'", null,null,null,DashBoard._ID,null);
        HashMap<String,byte[]> ALLC0 = new HashMap<>();
        byte[] C0 = new byte[0];
        byte[] C0_1 = new byte[0];
        if (cursor.moveToFirst())
        {
            Log.d("C0 decrypt",cursor.getString(4));
            String c_0 = cursor.getString(4);
            C0 = Base64.decode(c_0);
            ALLC0.put(String.valueOf(cursor.getPosition()),C0);

            while (cursor.moveToNext())
            {
                String c_0_i = cursor.getString(4);
                C0_1 = Base64.decode(c_0_i);
                ALLC0.put(String.valueOf(cursor.getPosition()),C0_1);
            }
        }
        cursor.close();
        return ALLC0;
    }
    public HashMap<String,byte[]> ReadALLC1(String user) throws IOException {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"= '"+user+"'",null,null,null,DashBoard._ID,null);
        byte[] C1 = new byte[0];
        byte[] C1_1 = new byte[0];
        HashMap<String,byte[]> ALLC1 = new HashMap<>();
        if (cursor.moveToFirst())
        {
            String c_1 = cursor.getString(5);
            C1 = Base64.decode(c_1);
            ALLC1.put(String.valueOf(cursor.getPosition()),C1);
            while (cursor.moveToNext())
            {
                String c_1_i = cursor.getString(5);
                C1_1 = Base64.decode(c_1_i);
                ALLC1.put(String.valueOf(cursor.getPosition()),C1_1);
            }

        }
        cursor.close();
        return ALLC1;
    }
    public HashMap<String,byte[]> ReadALLC2(String user) throws IOException {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"= '"+user+"'",null,null,null,DashBoard._ID,null);
        byte[] C2 = new byte[0];
        byte[] C2_1 = new byte[0];
        HashMap<String,byte[]> ALLC2 = new HashMap<>();
        if (cursor.moveToFirst())
        {
            String c_1 = cursor.getString(6);
            C2 = Base64.decode(c_1);
            ALLC2.put(String.valueOf(cursor.getPosition()),C2);
            while (cursor.moveToNext())
            {
                String c_1_i = cursor.getString(6);
                C2_1 = Base64.decode(c_1_i);
                ALLC2.put(String.valueOf(cursor.getPosition()),C2_1);
            }
        }
        cursor.close();
        return ALLC2;
    }
    public HashMap<String,byte[]> ReadALLC3(String user) throws IOException {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"= '"+user+"'",null,null,null,DashBoard._ID,null);
        byte[] C3 = new byte[0];
        byte[] C3_1 = new byte[0];
        HashMap<String,byte[]> ALLC3 = new HashMap<>();
        if (cursor.moveToFirst())
        {
            String c_1 = cursor.getString(7);
            C3 = Base64.decode(c_1);
            ALLC3.put(String.valueOf(cursor.getPosition()),C3);
            while (cursor.moveToNext())
            {
                String c_1_i = cursor.getString(7);
                C3_1 = Base64.decode(c_1_i);
                ALLC3.put(String.valueOf(cursor.getPosition()),C3_1);
            }

        }
        cursor.close();
        return ALLC3;
    }
    public HashMap<String,byte[]> READ_ALL_AES_KEY (String user) throws  IOException {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"='"+user+"'",null,null,null,DashBoard._ID,null);
        byte[] aes = new byte[0];
        byte[] aes_i = new byte[0];
        HashMap<String,byte[]> hashMap = new HashMap<>();
        if (cursor.moveToFirst())
        {
            aes = Base64.decode(cursor.getString(8));
            hashMap.put(String.valueOf(cursor.getPosition()),aes);
            while (cursor.moveToNext())
            {
                aes_i = Base64.decode(cursor.getString(8));
                hashMap.put(String.valueOf(cursor.getPosition()),aes_i);
            }
        }
        cursor.close();
        return hashMap;
    }
    public int getCount(String user)
    {
        Cursor cursor = getReadableDatabase().query(DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"='"+user+"'",null,null,null,null);
        int i = 0;
        if (cursor.moveToFirst())
        {
            i = cursor.getCount();
        }
        cursor.close();
        return i;
    }
    public Cursor getPolicy(String utente)
    {
        Cursor cursor = getReadableDatabase().query(true,DashBoard.TABLE_NAME,DashBoard.COLUMNS,DashBoard.USER_NAME+"='"+utente+"'",null,null,null,DashBoard._ID,null);
        return cursor;
    }
    public Cursor getAuthority(String policy)
    {
        Cursor authority = getReadableDatabase().query(DashBoard.TABLE_NAME, DashBoard.COLUMNS, DashBoard.ACCESS_STRUCTURE + "='" + policy + "'", null, null, null,DashBoard.INDICE);
        return authority;
    }
}
