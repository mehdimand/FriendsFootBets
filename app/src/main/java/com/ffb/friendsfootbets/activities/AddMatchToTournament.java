package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ffb.friendsfootbets.LoadUpcomingFixtures;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.AddMatchAdapter;
import com.ffb.friendsfootbets.databaselink.SaveTournament;
import com.ffb.friendsfootbets.models.Match;
import com.ffb.friendsfootbets.models.Tournament;

import java.util.ArrayList;
import java.util.HashMap;

public class AddMatchToTournament extends AppCompatActivity {

    private String TAG = AddMatchToTournament.class.getSimpleName();
    private Tournament currentTournament;

    private ProgressDialog pDialog;
    private ListView lv;
    private TextView noFixturesTextView;

    // can be the URL for all the fixtures of one competition or of one team
    private String fixturesURL;
    private HashMap<String, Match> fixturesMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_to_tournament);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        fixturesURL = extras.getString("url");


        lv = (ListView) findViewById(R.id.list2);
        noFixturesTextView = (TextView) findViewById(R.id.no_fixtures);

        LoadUpcomingFixtures loadFixtures = new LoadUpcomingFixtures();
        loadFixtures.setLoadFixturesListener(new LoadUpcomingFixtures.LoadFixturesListener() {

            @Override
            public void onFixturesLoaded(HashMap<String, Match> fixturesMapList) {
                getFixtures(fixturesMapList);
                fixturesMap = fixturesMapList;
            }
        });
        loadFixtures.loadFixtures(fixturesURL);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentTournament = (Tournament) extras.getSerializable("tournament");

        noFixturesTextView.setVisibility(View.GONE);
    }


    private void getFixtures(final HashMap<String, Match> fixturesMaplist) {

        if (fixturesMaplist.size() != 0){
            final ArrayList<Match> fixturesList = new ArrayList<>(fixturesMaplist.values());
            AddMatchAdapter addMatchAdapter = new AddMatchAdapter(getApplicationContext(), fixturesList);

            lv.setAdapter(addMatchAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(AddMatchToTournament.this, "Match added", Toast.LENGTH_SHORT).show();

                    Match fixtureAdded = fixturesList.get(position);

                    String matchId = fixtureAdded.getMatchId();

                    SaveTournament saveTournament = new SaveTournament();
                    saveTournament.addMatchtoTournament(currentTournament, matchId);
                }

            });
        }else {
            noFixturesTextView.setVisibility(View.VISIBLE);
        }
    }

}
