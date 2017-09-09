package com.example.antonino.cp_abeandroidcryptotest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.*;

/**
 * Created by antonino on 03/04/16.
 */
public class Authorities extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.authorities);
        final AuthoritiesDatabase authoritiesDatabase = new AuthoritiesDatabase(Authorities.this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        final Button Enter = (Button)findViewById(R.id.enter);
        final EditText InserisciAuthority = (EditText)findViewById(R.id.inserisci_auth);
        final ListView listView = (ListView)findViewById(R.id.lista_authorities);
        Button amministra_authority = (Button)findViewById(R.id.amministra_authority);
        Button crea_authority = (Button)findViewById(R.id.crea_nuova_auth);
        amministra_authority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                InserisciAuthority.setVisibility(View.INVISIBLE);
                InserisciAuthority.setVisibility(View.INVISIBLE);
                final Cursor authorities = authoritiesDatabase.LeggiAuthority();
                if (authorities.moveToFirst())
                {
                    adapter.clear();
                    adapter.add(authorities.getString(1));
                    try
                    {
                        while (authorities.moveToNext())
                        {
                            adapter.add(authorities.getString(1));
                        }
                    }
                    finally
                    {
                        listView.setAdapter(adapter);
                    }
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        setContentView(R.layout.amministra_authority);
                        authorities.moveToPosition(position);
                        Button CreaAttributi = (Button) findViewById(R.id.creare_attributi);
                        Button RilasciaAttributi = (Button) findViewById(R.id.rilascio);
                        CreaAttributi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final EditText InserisciAttributi = (EditText) findViewById(R.id.inserisci_attributi);
                                Button Enter = (Button) findViewById(R.id.enter_2);
                                InserisciAttributi.setVisibility(View.VISIBLE);
                                Enter.setVisibility(View.VISIBLE);
                                Enter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final ProgressDialog progressDialog = ProgressDialog.show(Authorities.this, "Attendere", "Generazione Secret Key");
                                        progressDialog.setCancelable(true);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String Attributo = InserisciAttributi.getText().toString();
                                                AuthoritiesDBHelper authoritiesDBHandler = new AuthoritiesDBHelper(Authorities.this, authorities.getString(1) + ".db");
                                                AttributesHandler handler = new AttributesHandler(Authorities.this);
                                                GlobalParametersHandler globalParametersHandler = GlobalParametersHandler.getInstance(Authorities.this);
                                                String baos64 = globalParametersHandler.ReadCurveParameters();
                                                sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters globalParameters = null;
                                                try {
                                                    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(baos64)));
                                                    globalParameters = (sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters) in.readObject();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (ClassNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                AuthorityKeys authorityKeys = DCPABE.authoritySetup(authorities.getString(1), globalParameters, Attributo);
                                                PublicKeys publicKeys = new PublicKeys();
                                                publicKeys.subscribeAuthority(authorityKeys.getPublicKeys());
                                                authoritiesDBHandler.InserisciAuthority(authorities.getString(1), "null", Attributo, "null", Base64.encodeBytes(authorityKeys.getSecretKeys().get(Attributo).getAi()), Base64.encodeBytes(authorityKeys.getSecretKeys().get(Attributo).getYi()));
                                                authoritiesDBHandler.close();
                                                handler.ScriviAttributi(Attributo, authorities.getString(1), "null", Base64.encodeBytes(publicKeys.getPK(Attributo).getG1yi()), Base64.encodeBytes(publicKeys.getPK(Attributo).getEg1g1ai()), "false", "false");
                                                handler.close();
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(Authorities.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }).start();
                                    }
                                });
                            }
                        });
                        RilasciaAttributi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AttributesHandler handler = new AttributesHandler(Authorities.this);
                                final Cursor attributi = handler.LeggiAttributi();
                                final int[] posizione = new int[attributi.getCount()];
                                if (attributi.moveToFirst()) {
                                    int i = 0;
                                    adapter.clear();
                                    if (attributi.getString(1).equals(authorities.getString(1))&&attributi.getString(6).equals("true")&&attributi.getString(7).equals("false")) {
                                        posizione[i] = attributi.getPosition();
                                        adapter.add(attributi.getString(3)+ " : " +attributi.getString(2));
                                        i++;
                                    }
                                    while (attributi.moveToNext()) {
                                        if (attributi.getString(1).equals(authorities.getString(1))&&attributi.getString(6).equals("true")&&attributi.getString(7).equals("false")) {
                                            posizione[i] = attributi.getPosition();
                                            adapter.add(attributi.getString(3)+ " : " +attributi.getString(2));
                                            i++;
                                        }
                                    }
                                }
                                ListView lista_attributi_richiesti = (ListView) findViewById(R.id.lista_attributi_richiesti);
                                lista_attributi_richiesti.setVisibility(View.VISIBLE);
                                lista_attributi_richiesti.setAdapter(adapter);
                                lista_attributi_richiesti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                                        final ProgressDialog progressDialog = ProgressDialog.show(Authorities.this, "Attendere", "Rilascio Chiave Pubblica");
                                        progressDialog.setCancelable(true);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                attributi.moveToPosition(posizione[position]);
                                                handler.RilasciaAttributo(attributi.getString(3), "true");
                                                AuthoritiesDBHelper authoritiesDBHandler = new AuthoritiesDBHelper(Authorities.this, attributi.getString(1) + ".db");
                                                UtentiDBHelper utentiDBHelper = new UtentiDBHelper(Authorities.this,attributi.getString(2) + ".db");
                                                    GlobalParametersHandler globalParametersHandler = GlobalParametersHandler.getInstance(Authorities.this);
                                                    String baos64 = globalParametersHandler.ReadCurveParameters();
                                                    sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters globalParameters = null;
                                                    try {
                                                        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(baos64)));
                                                        globalParameters = (sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters) in.readObject();
                                                        SecretKey secretKey = authoritiesDBHandler.getAuthKeys(attributi.getString(3));
                                                        PersonalKey personalKey = DCPABE.keyGen(attributi.getString(2), attributi.getString(3), secretKey, globalParameters);
                                                        authoritiesDBHandler.AggiornaAuthority(attributi.getString(2), attributi.getString(3),Base64.encodeBytes(personalKey.getKey()));
                                                        PublicKey public_key = handler.getPK(attributi.getString(3));
                                                        utentiDBHelper.InserisciUtente(attributi.getString(2), attributi.getString(3), Base64.encodeBytes(public_key.getG1yi()), Base64.encodeBytes(public_key.getEg1g1ai()));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                Intent intent = new Intent(Authorities.this,MainActivity.class);
                                                    startActivity(intent);
                                                    progressDialog.dismiss();

                                            }
                                        }).start();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        crea_authority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter.setVisibility(View.VISIBLE);
                InserisciAuthority.setVisibility(View.VISIBLE);
                Enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String authority = InserisciAuthority.getText().toString();
                        AuthoritiesDBHelper authDBHandler = new AuthoritiesDBHelper(Authorities.this, authority + ".db");
                        authDBHandler.InserisciNewAuthority(authority);
                        authDBHandler.close();
                        authoritiesDatabase.InsertAuthority(authority);
                        InserisciAuthority.setText(null);
                        Intent intent = new Intent(Authorities.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
