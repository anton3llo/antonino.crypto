package com.example.antonino.cp_abeandroidcryptotest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by antonino on 03/04/16.
 */
public class Contenuti extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.contenuti);
        final DashBoardHandler handler = new DashBoardHandler(Contenuti.this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        //final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        Button seleziona_utente = (Button)findViewById(R.id.seleziona_utente);
        seleziona_utente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //estrarre gli utenti che hanno cifrato e visualizzare contenuto criptato
                ListView listView = (ListView)findViewById(R.id.lista_utenti_2);
                UtentiDatabase database = new UtentiDatabase(Contenuti.this);
                final Cursor utenti = database.LeggiUser();
                if (utenti.moveToFirst())
                {
                    adapter.clear();
                    adapter.add(utenti.getString(1));
                    while (utenti.moveToNext())
                    {
                        adapter.add(utenti.getString(1));
                    }
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setContentView(R.layout.dashboard);
                        utenti.moveToPosition(position);
                        ListView listView1 = (ListView)findViewById(R.id.lista_contenuti);
                        Cursor ciphertext = handler.LeggiCiphertext(utenti.getString(1));
                        if (ciphertext.moveToFirst())
                        {
                            adapter.clear();
                            adapter.add(ciphertext.getString(2) +" : " +ciphertext.getString(4));
                            while (ciphertext.moveToNext())
                            {
                                adapter.add(ciphertext.getString(2)+" : "+ciphertext.getString(4));
                            }
                        }
                        listView1.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
