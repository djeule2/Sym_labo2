package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startAsyncActivity(View view) {
        Intent intent = new Intent(this, AsyncActivity.class);
        startActivity(intent);
    }

    public void startDelayedActivity(View view) {
        Intent intent = new Intent(this, DelayedActivity.class);
        startActivity(intent);
    }

    public void startObjetsXmlActivity (View view){
        Intent intent = new Intent(this, ObjetsXmlActivity.class);
        startActivity(intent);

    }

    public void startObjetsJsonActivity (View view){
        Intent intent = new Intent(this, ObjetJsonActivity.class);
        startActivity(intent);

    }

    public void startCompressionActivity(View view) {
        Intent intent = new Intent(this, CompressionActivity.class);
        startActivity(intent);
    }

    public void startGraphQLActivity(View view) {
        Intent intent = new Intent(this, GraphQlActivity.class);
        startActivity(intent);
    }
}
