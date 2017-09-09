package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PublicKey;

/**
 * Created by antonino on 07/04/16.
 */
public class UtentiDBHelper extends SQLiteOpenHelper {
    private static final String PRODUCTS_TABLE_CREATE = "CREATE TABLE "+UtentiDB.TABLE_NAME+" ("+UtentiDB._ID+" integer primary key autoincrement, "+UtentiDB.USER_NAME+" text not null, "+UtentiDB.ATTRIBUTE+" text not null, "+UtentiDB.PUBLIC_KEY_1+" text not null, "+UtentiDB.PUBLIC_KEY_2+" text);";
    private static final int DB_VERSION=1;
    public UtentiDBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InserisciNewUtente(String user_name)
    {
        ContentValues values = new ContentValues();
        values.put("NomeUtente",user_name);
        values.put("Attributo","null");
        values.put("PublicKey_1", "null");
        values.put("PublicKey_2","null");
        getWritableDatabase().insert(UtentiDB.TABLE_NAME, null, values);
    }
    public void InserisciUtente(String utente,String attributo,String public_key,String public_key_2)
    {
        ContentValues values = new ContentValues();
        values.put("NomeUtente",utente);
        values.put("Attributo",attributo);
        values.put("PublicKey_1",public_key);
        values.put("PublicKey_2",public_key_2);
        getWritableDatabase().insert(UtentiDB.TABLE_NAME, null, values);
        getWritableDatabase().delete(UtentiDB.TABLE_NAME, UtentiDB.ATTRIBUTE + "= 'null'", null);
    }
    public PublicKey getKey(String attributo) throws IOException {
        Cursor cursor = getReadableDatabase().query(UtentiDB.TABLE_NAME,UtentiDB.COLUMNS,UtentiDB.ATTRIBUTE+"='"+attributo+"'",null,null,null,null);
        byte[] key1 = new byte[0];
        byte[] key2 = new byte[0];
        if (cursor.moveToFirst())
        {
            key1 = Base64.decode(cursor.getString(3));
            key2 = Base64.decode(cursor.getString(4));
        }
        PublicKey publicKey = new PublicKey(key1,key2);
        return publicKey;
    }
}
