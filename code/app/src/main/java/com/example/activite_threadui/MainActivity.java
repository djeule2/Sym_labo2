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
                        symComManager.sendRequest("{\"query\": \"{author(id: 23){id first_name last_name posts{title content}}}\"}" , " http://sym.iict.ch/api/graphql");

            }
        });

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
