package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SymComManager symComManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        symComManager = new SymComManager(getApplicationContext());

        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.TAsynOuput);
               textView.setText(reponse);
                return true;
            }
        });

        final Button buttonAsynchrone = (Button) findViewById(R.id.TAsyn);


        buttonAsynchrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symComManager.sendRequest("edddd", "http://sym.iict.ch/" );
            }
        });

        symComManager.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.TAsynObjetOutput);
                textView.setText(reponse);
                return true;
            }
        });


        final Button buttonObjectJson = (Button) findViewById(R.id.TObj);


        buttonObjectJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        symComManager.sendRequest("{\"query\": \"{author(id: 23){id first_name last_name posts{title content}}}\"}" , " http://sym.iict.ch/api/graphql");

            }
        });
    }
}
