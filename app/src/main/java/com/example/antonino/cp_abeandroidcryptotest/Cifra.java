package com.example.antonino.cp_abeandroidcryptotest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.*;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.AccessStructure;

/**
 * Created by antonino on 04/04/16.
 */
public class Cifra extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.cifra);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        Intent intent = getIntent();
        final int position = intent.getIntExtra("Position", 0);
        UtentiDatabase utentiDatabase = new UtentiDatabase(Cifra.this);
        final Cursor utente = utentiDatabase.LeggiUser();
        utente.moveToPosition(position);
        final String Utente = utente.getString(1);
        final AttributesHandler handler = new AttributesHandler(Cifra.this);
        Button richiedi_attributi = (Button)findViewById(R.id.richiedi_attributi);
        Button cifra = (Button)findViewById(R.id.cifra);
        Button decifra = (Button)findViewById(R.id.decifra);
        richiedi_attributi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cursor attributi = handler.LeggiAttributi();
                final int[] posizione = new int[attributi.getCount()];
                 ListView lista_attributi = (ListView)findViewById(R.id.lista_attributi);
                lista_attributi.setVisibility(View.VISIBLE);
                if (attributi.moveToFirst())
                {
                    int i =0;
                    adapter.clear();
                    if (attributi.getString(6).equals("false"))
                    {
                        posizione[i]=attributi.getPosition();
                        adapter.add(attributi.getString(1)+ " : " +attributi.getString(3));
                        i++;
                    }
                    while (attributi.moveToNext())
                    {
                        if (attributi.getString(6).equals("false"))
                        {
                            posizione[i]=attributi.getPosition();
                            adapter.add(attributi.getString(1)+ " : " +attributi.getString(3));
                            i++;
                        }
                    }
                }
                lista_attributi.setAdapter(adapter);
                lista_attributi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Posizionediposition",String.valueOf(posizione[position]));
                        attributi.moveToPosition(posizione[position]);
                        Log.d("PosizioneScelta",attributi.getString(3));
                        handler.UpdateAttributi(attributi.getString(3),attributi.getString(1), Utente,"true");
                        Intent intent1 = new Intent(Cifra.this,MainActivity.class);
                        startActivity(intent1);
                    }
                });
            }
        });
        cifra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //textview scegliere gli attributi per la policy
                Button enter = (Button)findViewById(R.id.enter_4);
                enter.setVisibility(View.VISIBLE);
                TextView textView = (TextView)findViewById(R.id.textView);
                textView.setVisibility(View.VISIBLE);
                final ListView lista_attributi = (ListView)findViewById(R.id.lista_attributi);
                lista_attributi.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter_attributi = new ArrayAdapter<String>(Cifra.this,R.layout.simple_list_item_multichoice,android.R.id.text1);
                final ArrayAdapter<String> adapter_authority = new ArrayAdapter<String>(Cifra.this,android.R.layout.simple_list_item_1);
                final Cursor attributi = handler.LeggiAttributi();
                if (attributi.moveToFirst())
                {
                    adapter_attributi.clear();
                    if (attributi.getString(2).equals(Utente)&&attributi.getString(6).equals("true")&&attributi.getString(7).equals("true"))
                    {
                    adapter_authority.add(attributi.getString(1));
                    adapter_attributi.add(attributi.getString(3));
                    }
                    while (attributi.moveToNext())
                    {
                        if (attributi.getString(2).equals(Utente)&&attributi.getString(6).equals("true")&&attributi.getString(7).equals("true"))
                        {
                            adapter_authority.add(attributi.getString(1));
                            adapter_attributi.add(attributi.getString(3));
                        }
                    }
                }


                //attributi.moveToFirst();
                //final String authority = adapter_authority.getItem(0);


                lista_attributi.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lista_attributi.setAdapter(adapter_attributi);
                final SparseBooleanArray sparseBooleanArray =  lista_attributi.getCheckedItemPositions();
                final ArrayList<String> policy = new ArrayList<String>();
                //creare la policy con and e or
                final String[] authority = {new String()};
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < lista_attributi.getCount(); i++) {

                            if (sparseBooleanArray.get(i)) {
                                policy.add(lista_attributi.getItemAtPosition(i).toString());
                                authority[0] = adapter_authority.getItem(i);
                            }
                        }
                        Intent intent1 = new Intent(Cifra.this,Cipher.class);
                        intent1.putStringArrayListExtra("Policy", policy);
                        intent1.putExtra("Utente", Utente);
                        intent1.putExtra("Authority", authority[0]);
                        startActivity(intent1);
                        //cifrare
                    }
                });
            }
        });
        decifra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //recupera ciphertext e policy
                final ProgressDialog progressDialog = ProgressDialog.show(Cifra.this,"Attendere","Decifratura in corso");
                progressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DashBoardHandler dashBoardHandler = new DashBoardHandler(Cifra.this);
                        GlobalParametersHandler globalParametersHandler = GlobalParametersHandler.getInstance(Cifra.this);
                        String baos64 = globalParametersHandler.ReadCurveParameters();
                        sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters globalParameters = null;
                        try {
                            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(baos64)));
                            globalParameters = (sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters)in.readObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Cursor policy_dec = dashBoardHandler.getPolicy(Utente);
                        policy_dec.moveToFirst();
                        final int COUNT = dashBoardHandler.getCount(Utente);
                        String[] Messaggi = new String[COUNT];
                        try {
                            HashMap<String,byte[]> ALL_C0 = dashBoardHandler.ReadALLC0(Utente);
                            HashMap<String,byte[]> ALL_C1 = dashBoardHandler.ReadALLC1(Utente);
                            HashMap<String,byte[]> ALL_C2 = dashBoardHandler.ReadALLC2(Utente);
                            HashMap<String,byte[]> ALL_C3 = dashBoardHandler.ReadALLC3(Utente);
                            HashMap<String,byte[]> ALL_AES_KEYS = dashBoardHandler.READ_ALL_AES_KEY(Utente);
                            for (int i=0;i<COUNT;i++)
                            {
                                byte[] C0 = ALL_C0.get(String.valueOf(i));
                                Log.d("C0", Base64.encodeBytes(C0));
                                byte[] C1 = ALL_C1.get(String.valueOf(i));
                                Log.d("C1", Base64.encodeBytes(C1));
                                byte[] C2 = ALL_C2.get(String.valueOf(i));
                                Log.d("C2", Base64.encodeBytes(C2));
                                byte[] C3 = ALL_C3.get(String.valueOf(i));
                                Log.d("C3", Base64.encodeBytes(C3));
                                byte[] AES_KEY = ALL_AES_KEYS.get(String.valueOf(i));
                                Log.d("Aes", Base64.encodeBytes(AES_KEY));
                                Ciphertext ciphertext = new Ciphertext();
                                ciphertext.setC0(C0);
                                ciphertext.setC1(C1);
                                ciphertext.setC2(C2);
                                ciphertext.setC3(C3);
                                AccessStructure accessStructure = AccessStructure.buildFromPolicy(policy_dec.getString(3));
                                Log.d("Policy",policy_dec.getString(3));
                                Cursor cursor = dashBoardHandler.getAuthority(policy_dec.getString(3));
                                cursor.moveToFirst();
                                String authority = cursor.getString(1);
                                Log.d("Authority",authority);
                                AuthoritiesDBHelper authoritiesDBHelper = new AuthoritiesDBHelper(Cifra.this,authority+".db");
                                PersonalKeys personalKeys = authoritiesDBHelper.getPersonalKey(Utente);
                                policy_dec.moveToNext();
                                ciphertext.setAccessStructure(accessStructure);
                                //recuperare personalkeys e authority name
                                Message message = DCPABE.decrypt(ciphertext, personalKeys, globalParameters);
                                byte[] aes_pt = decrypt(sha256(message.m), AES_KEY);
                                String Messaggio = new String(aes_pt);
                                Messaggi[i] = Messaggio;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent1 = new Intent(Cifra.this,Decifra.class);
                        intent1.putExtra("Messaggi",Messaggi);
                        intent1.putExtra("COUNT",COUNT);
                        startActivity(intent1);
                        progressDialog.dismiss();
                    }
                }).start();
                    //recuper chiavi
                    //decifra
            }
        });
    }
    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
    public static byte[] sha256(byte[] text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text);

        return md.digest();
    }
}
