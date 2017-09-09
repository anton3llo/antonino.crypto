package com.example.antonino.cp_abeandroidcryptotest;

import android.provider.BaseColumns;

/**
 * Created by antonino on 07/03/16.
 */
public interface GlobalParameters extends BaseColumns {
    String TABLE_NAME="GlobalParameters";
    String GLOB_PARAMETERS="GlobParameters";
    String[] COLUMNS=new String[]{_ID,GLOB_PARAMETERS};
}
