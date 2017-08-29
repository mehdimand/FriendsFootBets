package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ffb.friendsfootbets.LoadFixtures;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.AddMatchAdapter;
import com.ffb.friendsfootbets.databaselink.SaveTournament;
import com.ffb.friendsfootbets.models.Match;
import com.ffb.friendsfootbets.models.Tournament;

import java.util.ArrayList;
import java.util.HashMap;

public class AddMatchToTournament2 extends AppCompatActivity {

    private String TAG = AddMatchToTournament2.class.getSimpleName();
    private Tournament currentTournament;

    private ProgressDialog pDialog;
    private ListView lv;

    private String url;
    private HashMap<String, Match> fixturesMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_to_tournament2);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        url = extras.getString("url");


        lv = (ListView) findViewById(R.id.list2);

        LoadFixtures loadFixtures = new LoadFixtures();
        loadFixtures.setLoadFixturesListener(new LoadFixtures.LoadFixturesListener() {

            @Override
            public void onFixturesLoaded(HashMap<String, Match> fixturesMapList) {
                getFixtures(fixturesMapList);
                fixturesMap = fixturesMapList;
            }
        });
        loadFixtures.loadFixtures(url);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentTournament = (Tournament) extras.getSerializable("tournament");
    }


    private void getFixtures(final HashMap<String, Match> fixturesMaplist) {

        final ArrayList<Match> fixturesList = new ArrayList<>(fixturesMaplist.values());
        AddMatchAdapter addMatchAdapter = new AddMatchAdapter(getApplicationContext(), fixturesList);

        lv.setAdapter(addMatchAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddMatchToTournament2.this, "Match added", Toast.LENGTH_SHORT).show();

                Match fixtureAdded = fixturesList.get(position);

                String matchId = fixtureAdded.getMatchId();

                SaveTournament saveTournament = new SaveTournament();
                saveTournament.addMatchtoTournament(currentTournament, matchId);
            }

        });
    }

}
