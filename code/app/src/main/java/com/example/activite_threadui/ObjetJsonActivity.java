/*
Olivier & Matthieu
Compression Activity
 */
package com.example.activite_threadui;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import model.Person;
import util.CommunicationEventListener;
import util.Serializer;
import util.SymComManager;

public class ObjetJsonActivity extends AppCompatActivity {
    private SymComManager symComManager;
    private  TextView textView = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objet_json);

        symComManager = new SymComManager(getApplicationContext(), SymComManager.ENCODE_TYPE.JSON, false);

        // Mise à jour de l'interface Ã  la réponse du serveur
        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean handleServerResponse(String reponse) {

                textView = (TextView) findViewById(R.id.objet_json_response);
                textView.setText(reponse);


               //désérialisation apres reception de la réponse du serveur
                if(textView.getText()!=null) {
                    Person person = Serializer.deserializeJson(Person.class, textView.getText().toString());
                    Log.i("name: ", person.toString());
                }
                return true;
            }

        });

        // Envoie de la requête
        try {
            Person person = new Person("Matthieu", "olivier", "M", 3);
            String serializer_person = new Serializer().serializeJson(person);
            symComManager.sendRequest(serializer_person, "http://sym.iict.ch/rest/json");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
