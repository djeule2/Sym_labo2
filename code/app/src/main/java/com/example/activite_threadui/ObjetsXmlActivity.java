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


public class ObjetsXmlActivity extends AppCompatActivity {

    private SymComManager symComManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objets_xml);
        symComManager = new SymComManager(getApplicationContext(), SymComManager.ENCODE_TYPE.XML, false);

        // Mise à  jour de l'interface à  la réponse du serveur
        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.objet_xml_response);
                textView.setText(reponse);

                System.out.println(textView.getText().toString());
                //deserialisation

                if(textView.getText()!=null) {
                    Person person = Serializer.deserilizeXml(textView.getText().toString());
                    Log.i("name: ", person.toString());

                }

                return true;
            }
        });

        // Envoie de la requète
        try {
            Person person = new Person("djeulezeck", "olivier", "M", 3);

            //serialisation
            String serializer_person = new Serializer().serializeXml(person);

            symComManager.sendRequest(serializer_person, "http://sym.iict.ch/rest/xml ");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
