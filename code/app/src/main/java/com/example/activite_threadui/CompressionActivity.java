/*
Olivier & Matthieu
Compression Activity
 */
package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import util.CommunicationEventListener;
import util.SymComManager;

public class CompressionActivity extends AppCompatActivity {
    SymComManager scm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compression);

        final long timer = System.currentTimeMillis();

        scm = new SymComManager(getApplicationContext(), SymComManager.ENCODE_TYPE.JSON, false);

        // On met à jour le TextView qu'on reçoit la réponse du serveur
        scm.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = findViewById(R.id.compressionResponse);
                textView.setText(reponse);
                Log.i("Timer", ""+ (System.currentTimeMillis() - timer));
                return true;
            }
        });

        String json = readTextFile(R.raw.big_json);
        try {
            scm.sendRequest(json, "http://sym.iict.ch/rest/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readTextFile(int file) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputStream = this.getResources().openRawResource(file);
        try {
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
