package com.example.antonino.cp_abeandroidcryptotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.text.MessageFormat;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PublicKey;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.Attribute;

/**
 * Created by antonino on 03/04/16.
 */
public class AttributesHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Attributes.db";
    private static final int SCHEMA_VERSION = 1;
    public AttributesHandler(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT," + " {2} TEXT,{3} TEXT,{4} TEXT,{5} TEXT,{6} TEXT,{7} TEXT,{8} TEXT);";
        db.execSQL(MessageFormat.format(sql, Attributes.TABLE_NAME, Attributes._ID, Attributes.AUTHORITY, Attributes.USER_NAME, Attributes.ATTRIBUTE, Attributes.PUBLIC_KEY_1, Attributes.PUBLIC_KEY_2, Attributes.FLAG_1, Attributes.FLAG_2));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void ScriviAttributi(String attributo,String authority,String utente,String PublicKey_1,String PublicKey_2,String flag,String flag_2)
    {
        ContentValues values = new ContentValues();
        values.put("Authority",authority);
        values.put("Attribute",attributo);
        values.put("Utente",utente);
        values.put("PublicKey_1",PublicKey_1);
        values.put("PublicKey_2",PublicKey_2);
        values.put("Flag_1",flag);
        values.put("Flag_2",flag_2);
        getWritableDatabase().insert(Attributes.TABLE_NAME,null,values);
    }
    public void UpdateAttributi(String attributo,String authority,String utente,String flag)
    {
        ContentValues values = new ContentValues();
        values.put("Authority",authority);
        values.put("Utente",utente);
        values.put("Flag_1",flag);
        getWritableDatabase().update(Attributes.TABLE_NAME, values, Attributes.ATTRIBUTE + "= '" + attributo + "'", null);
    }
    public void RilasciaAttributo(String attributo,String flag_2)
    {
        ContentValues values = new ContentValues();
        values.put("Flag_2",flag_2);
        getWritableDatabase().update(Attributes.TABLE_NAME,values,Attributes.ATTRIBUTE + "= '" + attributo + "'", null);
    }
    public Cursor LeggiAttributi()
    {
        Cursor attributo = getReadableDatabase().query(Attributes.TABLE_NAME,null,null,null,null,null,null);
        return attributo;
    }
    public PublicKey getPK(String attributo) throws IOException {
        Cursor public_key = getReadableDatabase().query(Attributes.TABLE_NAME,Attributes.COLUMNS,Attributes.ATTRIBUTE+ "='"+attributo +"'",null,null,null,null);
        byte[] key1=new byte[0];
        byte[] key2=new byte[0];
        if (public_key.moveToFirst())
        {
            key1 = Base64.decode(public_key.getString(4));
            key2 = Base64.decode(public_key.getString(5));
        }
        PublicKey publicKey = new PublicKey(key1,key2);
        return publicKey;
    }
}
