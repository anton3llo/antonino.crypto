package com.example.antonino.cp_abeandroidcryptotest;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.Ciphertext;
import sg.edu.ntu.sce.sands.crypto.dcpabe.DCPABE;
import sg.edu.ntu.sce.sands.crypto.dcpabe.Message;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PublicKey;
import sg.edu.ntu.sce.sands.crypto.dcpabe.PublicKeys;
import sg.edu.ntu.sce.sands.crypto.dcpabe.ac.AccessStructure;

/**
 * Created by antonino on 08/04/16.
 */
public class Cipher extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.cipher);
        Intent intent = getIntent();
        final ArrayList<String> policy = intent.getStringArrayListExtra("Policy");
        final String utente = intent.getStringExtra("Utente");
        final String authority = intent.getStringExtra("Authority");
        final String politica = android.text.TextUtils.join(" and ", policy);
        final EditText editText = (EditText)findViewById(R.id.inserisci_testo);
        Button enter = (Button)findViewById(R.id.enter_5);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(Cipher.this, "Attendere", "Cifratura in Corso");
                progressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AccessStructure accessStructure = AccessStructure.buildFromPolicy(politica);
                        GlobalParametersHandler globalParametersHandler = GlobalParametersHandler.getInstance(Cipher.this);
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
                        HashMap<String,PublicKey> map = new HashMap<>();
                        for (int i=0;i<policy.size();i++)
                        {
                            UtentiDBHelper utentiDBHelper = new UtentiDBHelper(Cipher.this,utente+".db");
                            try {
                                PublicKey publicKey = utentiDBHelper.getKey(policy.get(i));
                                map.put(policy.get(i),publicKey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        PublicKeys publicKeys = new PublicKeys();
                        publicKeys.subscribeAuthority(map);
                        Message message = new Message();
                        String clear_text = editText.getText().toString();
                        Ciphertext ciphertext = DCPABE.encrypt(message, accessStructure, globalParameters, publicKeys);
                        byte[] aes_ct = null;
                        try {
                            aes_ct = encrypt(sha256(message.m), clear_text.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        DashBoardHandler handler = new DashBoardHandler(Cipher.this);

                        //for (int j=0;j<policy.size();j++)
                        //{
                            handler.ScriviCiphertext(authority, utente, politica, Base64.encodeBytes(ciphertext.getC0()), Base64.encodeBytes(ciphertext.getC1(0)), Base64.encodeBytes(ciphertext.getC2(0)), Base64.encodeBytes(ciphertext.getC3(0)), Base64.encodeBytes(aes_ct));
                        //}
                        Log.d("C0 encrypt",Base64.encodeBytes(ciphertext.getC0()));
                        Log.d("C1 encrypt",Base64.encodeBytes(ciphertext.getC1(0)));
                        Log.d("C2 encrypt",Base64.encodeBytes(ciphertext.getC2(0)));
                        Log.d("C3 encrypt",Base64.encodeBytes(ciphertext.getC3(0)));
                        Log.d("Aes encrypt",Base64.encodeBytes(aes_ct));
                        progressDialog.dismiss();
                        Intent intent1 = new Intent(Cipher.this,MainActivity.class);
                        startActivity(intent1);
                    }
                }).start();
            }
        });
    }
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }
    public static byte[] sha256(byte[] text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text);

        return md.digest();
    }
}
