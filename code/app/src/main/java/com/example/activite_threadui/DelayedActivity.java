package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DelayedActivity extends AppCompatActivity {
    private SymComManager scm;
    private boolean responseReceived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed);

        scm = new SymComManager(getApplicationContext());

        scm.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String reponse) {
                TextView textView = (TextView) findViewById(R.id.delayedResponse);
                textView.setText(reponse);
                responseReceived = true;
                return true;
            }
        });

        new Thread() {
            public void run() {
                while (!responseReceived) {
                    try {
                        Thread.sleep(5000);
                        scm.sendRequest("", "http://sym.iict.ch/rest/txt");
                        //Toast.makeText(DelayedActivity.this, "Transmission différée effectuée", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
