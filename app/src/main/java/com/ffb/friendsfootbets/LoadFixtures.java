package com.ffb.friendsfootbets;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ffb.friendsfootbets.activities.AddMatchToTournament2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mehdimand on 07/06/2017.
 */

public class LoadFixtures {

    private LoadFixturesListener listener;
    public ArrayList<HashMap<String, String>> fixtures_list;

    public LoadFixtures() {
        this.listener = null;
        this.loadFixturesA = new LoadFixturesA();
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoadFixturesListener(LoadFixturesListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface LoadFixturesListener {
        // This trigger will be used every time we finish loading the tournaments related to a user
        public void onFixturesLoaded(ArrayList<HashMap<String, String>> fixtures_list);
    }

    private String TAG = AddMatchToTournament2.class.getSimpleName();

    private ProgressDialog pDialog;

    private String url;
    private LoadFixturesA loadFixturesA;


    public void loadFixtures(String url){
        this.url = url;
        fixtures_list = new ArrayList<>();
        System.out.println("aaa"+this.url);
        loadFixturesA.execute();
    }


    private class LoadFixturesA extends AsyncTask<Void, Void, Void>{


        protected void onPreExecute () {
            super.onPreExecute();
            // Showing progress dialog
        }

        protected Void doInBackground (Void...arg0){
            System.out.println("aaawaw");
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            // faut récuperer l'url des matchs pour chaque compétition
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray fixtures = jsonObj.getJSONArray("fixtures");
                    int min = 0;
                    // looping through All the table
                    for (int i = 0; i < fixtures.length() && min < 12; i++) {
                        JSONObject f = fixtures.getJSONObject(i);

                        String status = f.getString("status");
                        // timed or scheduled pour les matchs à venir
                        if (status.equals("TIMED") || status.equals("FINISHED")) {

                            min++;

                            String home = f.getString("homeTeamName");
                            String away = f.getString("awayTeamName");
                            String dateetheure = f.getString("date");

                            // tmp hash map for single fixture
                            HashMap<String, String> fixture = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fixture.put("home", home);
                            fixture.put("away", away);

                            SimpleDateFormat formatdate =
                                    new SimpleDateFormat("EEE d MMM ");

                            SimpleDateFormat formatheure =
                                    new SimpleDateFormat("H:mm");

                            SimpleDateFormat dt =
                                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                            try {
                                Date dateobject = dt.parse(dateetheure);
                                String date = formatdate.format(dateobject);
                                String heure = formatheure.format(dateobject);
                                fixture.put("date", date);
                                fixture.put("heure", heure);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // adding team to teamTable
                            fixtures_list.add(fixture);
                        }
                        System.out.println("aaaaaaa"+fixtures_list);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }

            return null;

        }

        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);

            if (listener != null){
                listener.onFixturesLoaded(fixtures_list);
            }

        }
    }

}
