package com.example.antonino.cp_abeandroidcryptotest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by antonino on 03/04/16.
 */
public class Utenti extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.utenti);
        final UtentiDatabase utentiDatabase = new UtentiDatabase(Utenti.this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        Button seleziona_utente = (Button)findViewById(R.id.seleziona_utente_2);
        Button crea_utente = (Button)findViewById(R.id.nuovo_utente);
        seleziona_utente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lista_utenti = (ListView)findViewById(R.id.lista_utenti);
                lista_utenti.setVisibility(View.VISIBLE);
                Cursor utenti = utentiDatabase.LeggiUser();
                if (utenti.moveToFirst())
                {
                    adapter.add(utenti.getString(1));
                    while (utenti.moveToNext())
                    {
                        adapter.add(utenti.getString(1));
                    }
                }
                lista_utenti.setAdapter(adapter);
                lista_utenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Utenti.this,Cifra.class);
                        intent.putExtra("Position",position);
                        startActivity(intent);
                    }
                });
            }
        });
        crea_utente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inserisci_utente = (EditText)findViewById(R.id.inserisci_nuovo_utente);
                Button enter = (Button)findViewById(R.id.enter_3);
                inserisci_utente.setVisibility(View.VISIBLE);
                enter.setVisibility(View.VISIBLE);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nuovo_utente = inserisci_utente.getText().toString();
                        UtentiDBHelper utentiDBHandler = new UtentiDBHelper(Utenti.this,nuovo_utente+".db");
                        utentiDBHandler.InserisciNewUtente(nuovo_utente);
                        utentiDBHandler.close();
                        utentiDatabase.InsertUser(nuovo_utente);
                        inserisci_utente.setText(null);
                        Intent intent = new Intent(Utenti.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
