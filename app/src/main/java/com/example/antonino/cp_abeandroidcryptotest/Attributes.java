package com.example.antonino.cp_abeandroidcryptotest;

import android.provider.BaseColumns;

/**
 * Created by antonino on 03/04/16.
 */
public interface Attributes extends BaseColumns {
    String TABLE_NAME="Attributes";
    String AUTHORITY="Authority";
    String USER_NAME="Utente";
    String ATTRIBUTE="Attribute";
    String PUBLIC_KEY_1="PublicKey_1";
    String PUBLIC_KEY_2 ="PublicKey_2";
    String FLAG_1 = "Flag_1";
    String FLAG_2 = "Flag_2";
    String[] COLUMNS=new String[]{_ID,AUTHORITY,USER_NAME,ATTRIBUTE,PUBLIC_KEY_1,PUBLIC_KEY_2,FLAG_1,FLAG_2};
}
