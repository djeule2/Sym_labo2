package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class GraphQlActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);
        //Intent intent = getIntent();
       ListView mListView = (ListView) findViewById(R.id.listView);
    }
}
