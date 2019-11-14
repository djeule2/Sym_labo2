/*
Olivier & Matthieu
Activité "Transmission Asynchrone"
 */
package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import util.CommunicationEventListener;
import util.SymComManager;

public class AsyncActivity extends AppCompatActivity {
    private SymComManager symComManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        symComManager = new SymComManager(getApplicationContext());

        // Mise à jour de l'interface à la réponse du serveur
        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = findViewById(R.id.async_response);
                textView.setText(reponse);
                return true;
            }
        });

        // Envoie de la requête
        try {
            String json = "{\"test\":\"goal\"}";
            symComManager.sendRequest(json, "http://sym.iict.ch/rest/txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
