package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PersonalKey;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PersonalKeys;
import sg.edu.ntu.sce.sands.crypto.dcpabe.SecretKey;

/**
 * Created by antonino on 07/04/16.
 */
public class AuthoritiesDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE "+AuthoritiesDB.TABLE_NAME+" ("+AuthoritiesDB._ID+" integer primary key autoincrement, "+AuthoritiesDB.AUTHORITY_NAME+" text, "+AuthoritiesDB.USER_NAME+" text, "+AuthoritiesDB.ATTRIBUTE+" text, "+AuthoritiesDB.PERSONAL_KEY+" text, "+AuthoritiesDB.SECRET_KEY_ai+" text, "+AuthoritiesDB.SECRET_KEY_yi+" text);";
    public AuthoritiesDBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InserisciNewAuthority(String AuthName)
    {
        ContentValues values = new ContentValues();
        values.put("AuthorityName",AuthName);
        values.put("Utente","null");
        values.put("Attribute","null");
        values.put("PersonalKey","null");
        values.put("SecretKey", "null");
        values.put("SecretKey_2","null");
        getWritableDatabase().insert(AuthoritiesDB.TABLE_NAME, null, values);
    }
    public void InserisciAuthority(String AuthName,String Utente,String Attribute,String PersonalKey,String SecretKey,String SecretKey_2)
    {
        ContentValues values = new ContentValues();
        values.put("AuthorityName",AuthName);
        values.put("Utente",Utente);
        values.put("Attribute",Attribute);
        values.put("PersonalKey", PersonalKey);
        values.put("SecretKey", SecretKey);
        values.put("SecretKey_2",SecretKey_2);
        getWritableDatabase().delete(AuthoritiesDB.TABLE_NAME, AuthoritiesDB.ATTRIBUTE + "= 'null'", null);
        getWritableDatabase().insert(AuthoritiesDB.TABLE_NAME, null, values);
    }
    public void AggiornaAuthority(String Utente,String Attributo,String PersonalKey)
    {
        ContentValues values = new ContentValues();
        values.put("Utente",Utente);
        values.put("PersonalKey",PersonalKey);
        getWritableDatabase().update(AuthoritiesDB.TABLE_NAME, values, AuthoritiesDB.ATTRIBUTE + " ='" + Attributo + "'", null);
    }
    public SecretKey getAuthKeys(String attributo) throws IOException {
        Cursor keys = getReadableDatabase().query(AuthoritiesDB.TABLE_NAME, AuthoritiesDB.COLUMNS, AuthoritiesDB.ATTRIBUTE + " ='" + attributo + "'", null, null, null, null);
        byte[] chiave = new byte[0];
        byte[] chiave_i = new byte[0];
        if (keys.moveToFirst())
        {
            Log.d("Chiave_1", keys.getString(5));
            Log.d("Chiave_2",keys.getString(6));
            chiave = Base64.decode(keys.getString(5));
            chiave_i = Base64.decode(keys.getString(6));
        }
        SecretKey secretKey = new SecretKey(chiave,chiave_i);
        return secretKey;
    }
    public PersonalKeys getPersonalKey(String user) throws IOException {
        Cursor cursor = getReadableDatabase().query(AuthoritiesDB.TABLE_NAME, AuthoritiesDB.COLUMNS, AuthoritiesDB.USER_NAME + "='" + user + "'", null, null, null, AuthoritiesDB.PERSONAL_KEY);
        PersonalKeys personalKeys = new PersonalKeys(user);
        if (cursor.moveToFirst())
        {
            String chiave1 = cursor.getString(4);
            byte[] key = Base64.decode(chiave1);
            Log.d("PK decrypt",Base64.encodeBytes(key));
            String attributo = cursor.getString(3);
            PersonalKey personalKey = new PersonalKey(attributo,key);
            personalKeys.addKey(personalKey);
            while (cursor.moveToNext())
            {
                String chiave_i = cursor.getString(4);
                byte[] key_i = Base64.decode(chiave_i);
                Log.d("PK decrypt",Base64.encodeBytes(key_i));
                String attributo_i = cursor.getString(3);
                PersonalKey personalKey_i = new PersonalKey(attributo_i,key_i);
                personalKeys.addKey(personalKey_i);
            }
        }
        return personalKeys;

    }
}
