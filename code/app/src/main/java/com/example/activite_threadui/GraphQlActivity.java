/* Autheurs: Olivier et Matthieu
Activité GraphQL
 */
package com.example.activite_threadui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.CommunicationEventListener;
import util.SymComManager;

import static android.R.layout.simple_spinner_dropdown_item;

public class GraphQlActivity extends AppCompatActivity implements OnItemSelectedListener {
    private final String URL = "http://sym.iict.ch/api/graphql";

    private SymComManager scmAuthors; // Permet l'envoi et la réception des requêtes pour avoir les auteurs
    private SymComManager scmPosts; // Permet l'envoi et la réception des requêtes pour avoir les titres des livres
    private List<Integer> authorsIds = new ArrayList<>();
    private List<String> posts = new ArrayList<>();

    private Spinner spinner;
    private ArrayAdapter<String> selectListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ql);

        spinner = findViewById(R.id.spinnerAuteurs);
        spinner.setOnItemSelectedListener(this);
        selectListAdapter = new ArrayAdapter<>(getApplicationContext(), simple_spinner_dropdown_item, new ArrayList<String>());
        spinner.setAdapter(selectListAdapter);

        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new RecyclerViewAdapter(posts);
        recyclerView.setAdapter(myAdapter);

        scmAuthors = new SymComManager(getApplicationContext());
        scmPosts = new SymComManager(getApplicationContext());

        // On met à jour la liste déroulante avec la liste des auteurs reçue
        scmAuthors.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                selectListAdapter.clear();
                selectListAdapter.addAll(getItemsFromString(response));
                selectListAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // On met à jour la liste des livres
        scmPosts.setCommunicationEventListener(new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(String response) {
                //print results
                System.out.println(response);
                parseAndUpdateDataset(response);
                myAdapter.notifyDataSetChanged();
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

    // On parse le JSON reçu pour la liste des titres
    private void parseAndUpdateDataset(String response) {
        posts.clear();
        try {
            JSONObject json = new JSONObject(response);
            JSONArray jsonPosts = json.getJSONObject("data").getJSONArray("allPostByAuthor");
            for (int i = 0; i < jsonPosts.length(); i++) {
                JSONObject post = jsonPosts.getJSONObject(i);
                posts.add(post.getString("title"));
            }
            Log.d("Posts size()", ""+posts.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // On parse le JSON pour la liste des auteurs
    private List<String> getItemsFromString(String response) {
        List<String> authors = new ArrayList<>();
        System.out.println(response);
        try {
            JSONObject json = new JSONObject(response);
            JSONArray jsonAuthors = json.getJSONObject("data").getJSONArray("allAuthors");
            for (int i = 0; i < jsonAuthors.length(); i++) {
                JSONObject jsonAuthor = jsonAuthors.getJSONObject(i);
                authorsIds.add(Integer.valueOf(jsonAuthor.getString("id")));
                authors.add(jsonAuthor.getString("first_name") + " " + jsonAuthor.getString("last_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    // On envoie une requête au serveur pour avoir la liste des titres de l'auteur sélectionné
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        int idAuthor = authorsIds.get(pos);
        Log.d("idAuthor", ""+ idAuthor);
        try {
            scmPosts.sendRequest("{\"query\":\"{allPostByAuthor(authorId: " + idAuthor + "){title}}\"}", URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Nothing to do
    }

}

// L'adapteur utilisé pour le RecyclerView
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.recyclerViewText);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
