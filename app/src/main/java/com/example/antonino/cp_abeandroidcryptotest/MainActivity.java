package com.example.antonino.cp_abeandroidcryptotest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;
import sg.edu.ntu.sce.sands.crypto.dcpabe.DCPABE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,"Attendere","Inizializzazione ambiente in corso");
        progressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalParametersHandler globalParametersHandler = GlobalParametersHandler.getInstance(MainActivity.this);
                if (globalParametersHandler.ReadCurveParameters()==null)
                {
                    sg.edu.ntu.sce.sands.crypto.dcpabe.GlobalParameters globalParameters = DCPABE.globalSetup(160);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(baos);
                        out.writeObject(globalParameters);
                        globalParametersHandler.WriteCurveParameters(Base64.encodeBytes(baos.toByteArray()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        }).start();

        Button authority = (Button)findViewById(R.id.authority);
        Button user = (Button)findViewById(R.id.user);
        Button contents = (Button)findViewById(R.id.contents);
        authority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Authorities.class);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Utenti.class);
                startActivity(intent);
            }
        });
        contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Contenuti.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.quit);
            alertDialogBuilder.setMessage(R.string.really_quit)
                    .setCancelable(false).
                    setPositiveButton(R.string.yes,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogInterface,int id)
                        {
                            finish();
                            moveTaskToBack(true);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
