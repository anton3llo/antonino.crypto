package com.example.antonino.cp_abeandroidcryptotest;

import android.provider.BaseColumns;

/**
 * Created by antonino on 03/04/16.
 */
public interface AuthoritiesDB extends BaseColumns {
    String TABLE_NAME="AuthoritiesTable";
    String AUTHORITY_NAME="AuthorityName";
    String USER_NAME="Utente";
    String ATTRIBUTE="Attribute";
    String PERSONAL_KEY="PersonalKey";
    String SECRET_KEY_ai="SecretKey";
    String SECRET_KEY_yi="SecretKey_2";
    String[] COLUMNS=new String[]{_ID,AUTHORITY_NAME,USER_NAME,ATTRIBUTE,PERSONAL_KEY,SECRET_KEY_ai,SECRET_KEY_yi};
}
