package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ObjetJsonActivity extends AppCompatActivity {
    private SymComManager symComManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objet_json);

        symComManager = new SymComManager(getApplicationContext());

        // Mise à jour de l'interface à la réponse du serveur
        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.objet_json_response);
                textView.setText(reponse);
                return true;
            }
        });

        // Envoie de la requête
        try {
            Person person = new Person("Matthieu", "olivier", "M", 3);
            String serializer_person = new Serializer(person).serializeJson();

            symComManager.sendRequest(serializer_person, "http://sym.iict.ch/rest/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
