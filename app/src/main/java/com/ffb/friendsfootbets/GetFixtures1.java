package com.ffb.friendsfootbets;

/**
 * Created by mehdimand on 02/06/2017.
 **/

import android.app.Activity;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ffb.friendsfootbets.activities.AddMatchToTournament2;
import com.ffb.friendsfootbets.activities.ErrorServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;

public class GetFixtures1 extends AsyncTask<Void, Void, Void> {

    private String TAG = AddMatchToTournament2.class.getSimpleName();

    private ProgressDialog pDialog;

    private Integer url;

    // TODO : activity a qui doit être AddMatchToTournament2
    Activity a;

    ArrayList<HashMap<String, String>> fixtures_list;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog


        pDialog = new ProgressDialog(a);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        // faut récuperer l'url des matchs pour chaque compétition
        String jsonStr = sh.makeServiceCall(Integer.toString(url));

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

                        min = min + 1;

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
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(a.getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(a.getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
            Intent intent = new Intent(a, ErrorServer.class);
            a.startActivity(intent);

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

    }
}