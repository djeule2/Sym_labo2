package com.example.activite_threadui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class SymComManagerCompression {
    private Context context ;

    private List<CommunicationEventListener> thelisteners = new LinkedList<>();
    private List<Pair<String, String>> waitingRequest= new LinkedList<>();

    SymComManagerCompression(Context context) {
        this.context = context;

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable()) {
                    sendRequestQueu();
                }
            }
        }, new IntentFilter(ConnectivityManager.EXTRA_NETWORK));
    }

    /*
    Permet d'envoyer un document request vers le serveur désigné par url
    s'il n'y a pas de connexion internet on ajoute la requêt dans une file d'attente
     */

    public void sendRequest(String request, String url) throws Exception { // TODO exception perso?
        if(isNetworkAvailable()){
            new AsynTaskSendRequest().execute(url, request);
        }else {
            Toast.makeText (context, "Network is not available", Toast.LENGTH_LONG);
            waitingRequest.add(new Pair(url, request));

        }

    }

    public void sendRequestQueu(){
        for (Pair<String, String> requestQueu: waitingRequest){
            if(isNetworkAvailable()){
                new AsynTaskSendRequest().execute(requestQueu.first, requestQueu.second);
            }
        }
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    /*
    Permet de définir un listener listener qui sera invoqué lorsque la réponse parviendra au client
     */
    public void setCommunicationEventListener (CommunicationEventListener listener){
        if (!thelisteners.contains(listener))
            thelisteners.add(listener);
    }


    private class AsynTaskSendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String doInBackground (String... strings){
            String my_url = strings[0];
            String my_data = strings[1];
            InflaterInputStream inputStream = null;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(my_url);

                conn = (HttpURLConnection) url.openConnection();

                // setting the request methode Type
                conn.setRequestMethod("POST");

                //adding the headers for request
                conn.setRequestProperty("Content-Type", "application/json, charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("X-Content-Encoding", "deflate");
                conn.setRequestProperty("X-Network", "CSD");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Deflater deflater = new Deflater(9, true);
                DeflaterOutputStream dos = new DeflaterOutputStream(conn.getOutputStream(), deflater);
                OutputStreamWriter writer = new OutputStreamWriter(dos);
                writer.write(my_data);
                writer.flush();
                writer.close();
                dos.close();

                conn.connect();
                int response = conn.getResponseCode();
                if (response != HttpURLConnection.HTTP_OK) {
                    return "" + conn.getResponseCode();
                }

                Inflater inflater = new Inflater(true);
                inputStream = new InflaterInputStream(conn.getInputStream(), inflater);
                if (inputStream == null) {
                    return null;
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }

                return new String(buffer);

            } catch (IOException e){
                return (e.getMessage());
            } finally {
                {
                    if (conn != null){
                        conn.disconnect();
                    }
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        }catch (IOException e){
                            return e.getMessage();
                        }
                    }
                }
            }
        }

        protected void onPostExecute (String s){
            super.onPostExecute(s);
            for (CommunicationEventListener listener: thelisteners){
                listener.handleServerResponse(s);
            }
        }
    }
}
