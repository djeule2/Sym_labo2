package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_dropdown_item;

public class GraphQLActivity extends AppCompatActivity implements OnItemSelectedListener {
    private final String URL = "http://sym.iict.ch/api/graphql";

    private SymComManager scmAuthors;
    private SymComManager scmPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);

        scmAuthors = new SymComManager(getApplicationContext());

        scmAuthors.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                Spinner spinner = findViewById(R.id.spinnerAuteurs);
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), simple_spinner_dropdown_item, getItemsFromString(response));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), simple_spinner_dropdown_item, new String[]{"Test"});
                spinner.setAdapter(adapter);
                return true;
            }
        });

        scmPosts.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                return true;
            }
        });

        String json = "{\"query\":\"{allAuthors{id last_name first_name}}\"}";
        try {
            scmAuthors.sendRequest(json, URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getItemsFromString(String response) {
        List<String> authors = new ArrayList<>();
        System.out.println(response);
        try {
            JSONObject json = new JSONObject(response);
            JSONArray jsonAuthors = json.getJSONObject("data").getJSONArray("allAuthors");
            for (int i = 0; i < jsonAuthors.length(); i++) {
                JSONObject jsonAuthor = jsonAuthors.getJSONObject(i);
                authors.add(jsonAuthor.getString("first_name")+" "+ jsonAuthor.getString("last_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }
        return authors.toArray(new String[0]);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // int idAuthor = authorsIds.get(pos);
        int idAuthor = 0;
        try {
            scmPosts.sendRequest("{\"query\":\"{author(id: "+ id +"){posts{title content}}}\"", URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
