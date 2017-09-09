package com.example.antonino.cp_abeandroidcryptotest;

import android.provider.BaseColumns;

/**
 * Created by antonino on 11/04/16.
 */
public interface DashBoard extends BaseColumns {
    String TABLE_NAME="DashBoard";
    String INDICE="Indice";
    String USER_NAME="Utente";
    String ACCESS_STRUCTURE="AccessStructure";
    String C0="C0";
    String C1="C1";
    String C2="C2";
    String C3="C3";
    String AES_KEY="AES_KEY";
    String[] COLUMNS = new String[]{_ID,INDICE,USER_NAME,ACCESS_STRUCTURE,C0,C1,C2,C3,AES_KEY};
}
