package com.ffb.friendsfootbets;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.ffb.friendsfootbets.activities.AddMatchToTournament;
import com.ffb.friendsfootbets.models.Match;

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


public class LoadSpecificFixtures {

    private LoadFixturesListener listener;
    public HashMap<String, Match> fixtures_list;

    public LoadSpecificFixtures() {
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

    private ArrayList<String> listUrl;
    private LoadFixturesA loadFixturesA;


    public void loadFixtures(ArrayList<String> listUrl) {
        this.listUrl = listUrl;
        fixtures_list = new HashMap<String, Match>();
        loadFixturesA.execute();
    }


    private class LoadFixturesA extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            for (int i = 0; i < listUrl.size(); i++) {

                String jsonStr = sh.makeServiceCall(
                        "http://api.football-data.org/v1/fixtures/" + listUrl.get(i));
                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        JSONObject f = json.getJSONObject("fixture");


                        String status = f.getString("status");

                        Integer goalsHome;
                        Integer goalsAway;
                        String home = "";
                        String away = "";
                        String date = "";
                        String heure = "";

                        // timed or scheduled pour les matchs à venir
                        if (status.equals("TIMED") || status.equals("FINISHED")) {

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

                        if (status.equals("FINISHED")) {
                            JSONObject result = f.getJSONObject("result");
                            goalsHome = result.getInt("goalsHomeTeam");
                            goalsAway = result.getInt("goalsAwayTeam");
                        } else {
                            goalsHome = -1;
                            goalsAway = -1;
                        }

                        Match match = new Match(listUrl.get(i), home, away, date, heure, goalsHome, goalsAway);

                        fixtures_list.put(listUrl.get(i), match);

                        System.out.println("Match added to list "+fixtures_list.toString());

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (listener != null) {
                listener.onFixturesLoaded(fixtures_list);

            }

        }
    }

}
