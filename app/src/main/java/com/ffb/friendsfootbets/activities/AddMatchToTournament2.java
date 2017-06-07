package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ffb.friendsfootbets.HttpHandler;
import com.ffb.friendsfootbets.LoadFixtures;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.databaselink.SaveTournament;
import com.ffb.friendsfootbets.models.Tournament;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.AsyncTask.execute;

public class AddMatchToTournament2 extends AppCompatActivity {

    private String TAG = AddMatchToTournament2.class.getSimpleName();
    private Tournament currentTournament;

    private ProgressDialog pDialog;
    private ListView lv;

    private int url;
    private ArrayList<HashMap<String, String>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_to_tournament2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        url = extras.getInt(AddMatchToTournament.Url_fixtures);


        lv = (ListView) findViewById(R.id.list2);

        LoadFixtures loadFixtures = new LoadFixtures();
        loadFixtures.setLoadFixturesListener(new LoadFixtures.LoadFixturesListener() {

            @Override
            public void onFixturesLoaded(ArrayList<HashMap<String, String>> fixturesMapList) {
                getFixtures(fixturesMapList);
                list = fixturesMapList;
                System.out.println("aaaa"+list);
            }
        });
        loadFixtures.loadFixtures(url);

<<<<<<< HEAD
=======
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentTournament = (Tournament) extras.getSerializable("tournament");
    }

    // TODO: mettre tout ce qui suit dans une classe GetFixtures1

    private class GetFixtures extends AsyncTask<Void, Void, Void> {

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
            String jsonStr = sh.makeServiceCall(getString(url));

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

                            JSONObject links = f.getJSONObject("_links");
                            JSONObject self = links.getJSONObject("self");
                            String id = self.getString("href");

                            fixture.put("id",id);

                            // adding team to teamTable
                            fixtures_list.add(fixture);
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
                Intent intent = new Intent(AddMatchToTournament2.this, ErrorServer.class);
                startActivity(intent);
>>>>>>> master


    }

    private void getFixtures(final ArrayList<HashMap<String, String>> fixturesMaplist) {

        final ListAdapter adapter = new SimpleAdapter(
                AddMatchToTournament2.this, fixturesMaplist,
                R.layout.list_fixtures_to_add, new String[]{"home", "away",
                "date", "heure"}, new int[]{R.id.hteam, R.id.ateam, R.id.date, R.id.heure});

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

<<<<<<< HEAD
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddMatchToTournament2.this, "Match added", Toast.LENGTH_SHORT).show();
=======
                    /*Intent intent = new Intent(AddMatchToTournament2.this, AddMatchToTournament.class);*/
>>>>>>> master

                Intent intent = new Intent(AddMatchToTournament2.this, AddMatchToTournament.class);

                HashMap<String, String> fixtureadded = fixturesMaplist.get(position);

<<<<<<< HEAD

                String idmatch = fixtureadded.get("id");
                intent.putExtra("idmatch", idmatch);

                // TODO: add the match id to the database
=======
                    String idmatch = fixtureadded.get("id");
                    /*intent.putExtra("idmatch",idmatch);*/

                    // TODO: add the match id to the database
                    SaveTournament saveTournament = new SaveTournament();
                    saveTournament.addMatchtoTournament(currentTournament, idmatch.split("fixtures/")[1]);
                    System.out.println(idmatch.split("fixtures/")[1]);
                    /*startActivity(intent);*/
                }
            });
>>>>>>> master

                startActivity(intent);
            }
        });

    }

}
