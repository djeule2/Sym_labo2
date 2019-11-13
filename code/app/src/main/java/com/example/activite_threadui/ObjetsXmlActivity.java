package com.example.activite_threadui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


public class ObjetsXmlActivity extends AppCompatActivity {

    private SymComManager symComManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objets_xml);
        symComManager = new SymComManager(getApplicationContext());

        // Mise à jour de l'interface à la réponse du serveur
        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.objet_xml_response);
                textView.setText(reponse);
                return true;
            }
        });

        // Envoie de la requête
        try {
            Person person = new Person("djeulezeck", "olivier", "M", 3);
           String serializer_person = new Serializer(person).serializeXml();

            symComManager.sendRequest(serializer_person, "http://sym.iict.ch/rest/xml ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
