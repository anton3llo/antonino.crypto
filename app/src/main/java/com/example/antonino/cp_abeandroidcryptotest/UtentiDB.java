package com.example.antonino.cp_abeandroidcryptotest;

import android.provider.BaseColumns;

/**
 * Created by antonino on 04/04/16.
 */
public interface UtentiDB extends BaseColumns {
    String TABLE_NAME = "UtentiTale";
    String USER_NAME = "NomeUtente";
    String ATTRIBUTE = "Attributo";
    String PUBLIC_KEY_1 = "PublicKey_1";
    String PUBLIC_KEY_2 = "PublicKey_2";
    String[] COLUMNS = new String[]{_ID,USER_NAME,ATTRIBUTE,PUBLIC_KEY_1,PUBLIC_KEY_2};
}
