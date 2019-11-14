/*
Olivier & Matthieu
Activité "Transmission différée"
 */

package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import util.CommunicationEventListener;
import util.SymComManager;

public class DelayedActivity extends AppCompatActivity {
    private SymComManager scm;
    private boolean responseReceived = false;
    private List<String> messages = new ArrayList<>();
    private TextView field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed);
        field = findViewById(R.id.fieldDelayed);

        scm = new SymComManager(getApplicationContext());

        // Quand une réponse est reçue, on met à jour le contenu de l'activité
        scm.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.delayedResponse);
                textView.setText(reponse);
                responseReceived = true;
                return true;
            }
        });

        // Un thread s'occupe de contacter le serveur toutes les 10 secondes tant que celui-ci n'a pas répondu
        new Thread() {
            public void run() {
                while (!responseReceived) {
                    try {
                        Thread.sleep(10000);
                        scm.sendRequest(messages.toString(), "http://sym.iict.ch/rest/txt");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // A l'appui du bouton Send, on garde en mémoire ce qui a été écrit
    public void addText(View view) {
        String text = field.getText().toString();
        if (text != null) {
            field.setText("");
            messages.add(text);
        }
    }
}
