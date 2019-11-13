/*
Olivier & Matthieu
Compression Activity
 */
package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CompressionActivity extends AppCompatActivity {
    SymComManagerCompression scmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compression);

        scmc = new SymComManagerCompression(getApplicationContext());

        // On met à jour le TextView qu'on reçoit la réponse du serveur
        scmc.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.compressionResponse);
                textView.setText(reponse);
                return true;            }
        });

        String json = "{\"test\":\"goal\"}";
        try {
            scmc.sendRequest(json, "http://sym.iict.ch/rest/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
