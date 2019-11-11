package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


/*

        final Button buttonObjectJson = (Button) findViewById(R.id.TObj);


        buttonObjectJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        symComManager.sendRequest("{\n" +
                                " \"directory\":\n" +
                                "   {\n" +
                                "     \"person\":\n" +
                                "       {\n" +
                                "         \"name\": \"mathieu\",\n" +
                                "         \"firstname\": \"mathieu\",\n" +
                                "         \"gender\": \"m\",\n" +
                                "         \"phone\" : 12133\n" +
                                "        }\n" +
                                "     }\n" +
                                "}" , " http://sym.iict.ch/rest/json ");

            }
        });

<<<<<<< HEAD
        final Button buttonGraphQl = (Button) findViewById(R.id.TGraphQL);


        buttonGraphQl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphQlActivity.class);
                startActivity(intent);
            }
        });

=======
 */
    }

    public void startAsyncActivity(View view) {
        Intent intent = new Intent(this, AsyncActivity.class);
        startActivity(intent);
    }

    public void startDelayedActivity(View view) {
        Intent intent = new Intent(this, DelayedActivity.class);
        startActivity(intent);
    }
}
