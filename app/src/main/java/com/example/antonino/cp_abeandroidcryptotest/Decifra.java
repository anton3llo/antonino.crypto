package com.example.antonino.cp_abeandroidcryptotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by antonino on 12/04/16.
 */
public class Decifra extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decifra);
        Intent intent = getIntent();
        String[] Messaggio = intent.getStringArrayExtra("Messaggi");
        int COUNT = intent.getIntExtra("COUNT", 0);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ListView listViewView = (ListView)findViewById(R.id.visualizza_messaggio);
        adapter.clear();
        for (int i=0;i<COUNT;i++)
        {
            adapter.add(Messaggio[i]);
        }
        listViewView.setAdapter(adapter);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Decifra.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
