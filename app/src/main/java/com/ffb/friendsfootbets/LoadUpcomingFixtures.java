package com.ffb.friendsfootbets;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ffb.friendsfootbets.activities.AddMatchToTournament;
import com.ffb.friendsfootbets.models.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mehdimand on 07/06/2017.
 */

public class LoadUpcomingFixtures {

    private LoadFixturesListener listener;
    public HashMap<String, Match> fixtures_list;

    public LoadUpcomingFixtures() {
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
        public void onFixturesLoaded(HashMap<String, Match> fixtures_list);
    }

    private String TAG = AddMatchToTournament.class.getSimpleName();

    private ProgressDialog pDialog;

    private String url;
    private LoadFixturesA loadFixturesA;


    public void loadFixtures(String url){
        this.url = url;
        fixtures_list = new HashMap<String, Match>();
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

                        JSONObject links = f.getJSONObject("_links");
                        JSONObject self = links.getJSONObject("self");
                        String url = self.getString("href");
                        String id = url.split("fixtures/")[1];

                        Integer goalsHome;
                        Integer goalsAway;
                        String home = "";
                        String away = "";
                        String date= "";
                        String heure = "";

                        // timed or scheduled pour les matchs à venir
                        if (status.equals("TIMED") || status.equals("SCHEDULED")) {

                            min++;

                            home = f.getString("homeTeamName");
                            away = f.getString("awayTeamName");
                            String dateetheure = f.getString("date");

                            SimpleDateFormat formatdate =
                                    new SimpleDateFormat("EEE d MMM ");

                            SimpleDateFormat formatheure =
                                    new SimpleDateFormat("H:mm");

                            SimpleDateFormat dt =
                                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                            try {
                                Date dateobject = dt.parse(dateetheure);
                                date = formatdate.format(dateobject);
                                heure = formatheure.format(dateobject);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        if (status.equals("FINISHED")){
                            JSONObject result = f.getJSONObject("result");
                            goalsHome = result.getInt("goalsHomeTeam");
                            goalsAway = result.getInt("goalsAwayTeam");
                        }
                        else {
                            goalsHome = -1;
                            goalsAway = -1;
                        }

                        Match match = new Match(id,home,away,date,heure,goalsHome,goalsAway);

                        fixtures_list.put(id,match);
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
