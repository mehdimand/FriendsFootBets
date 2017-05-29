package com.ffb.friendsfootbets;

import android.app.Activity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class AddMatchToTournament2 extends Activity {

    private String TAG = AddMatchToTournament2.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    private static String url = "http://api.football-data.org/v1/competitions/426/fixtures";

    ArrayList<HashMap<String, String>> Fixtures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_to_tournament2);

        Fixtures = new ArrayList<>();


        lv = (ListView) findViewById(R.id.list2);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddMatchToTournament2.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
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
                        if (status.equals("TIMED") || status.equals("SCHEDULED")) {

                            min = min + 1;

                            String home = f.getString("homeTeamName");
                            String away = f.getString("awayTeamName");
                            String date = f.getString("date");

                            // tmp hash map for single contact
                            HashMap<String, String> fixture = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fixture.put("home", home);
                            fixture.put("away", away);
                            fixture.put("date", date);

                            // adding team to teamTable
                            Fixtures.add(fixture);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    AddMatchToTournament2.this, Fixtures,
                    R.layout.list_fixtures, new String[]{"home", "away",
                    "date"}, new int[]{R.id.hteam, R.id.ateam, R.id.date});

            lv.setAdapter(adapter);
        }

    }
}