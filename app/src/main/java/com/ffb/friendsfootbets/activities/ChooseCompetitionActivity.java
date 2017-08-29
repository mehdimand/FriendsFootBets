package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ffb.friendsfootbets.LoadCompetitions;
import com.ffb.friendsfootbets.adapters.CompetitionAdapter;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseCompetitionActivity extends AppCompatActivity {

    Tournament currentTournament;
    User currentUser;
    ListView list;
    /*
    String[] txt = {
            "Ligue des Champions",
            "Premier League",
            "Liga",
            "Ligue 1",
            "FA Cup",
            "Bundesliga",
            "Serie A"
    };
    Integer[] imageId = {
            R.drawable.logo_ldc,
            R.drawable.logo_pl,
            R.drawable.logo_liga,
            R.drawable.logo_ligue1,
            R.drawable.logo_facup,
            R.drawable.logo_bundesliga,
            R.drawable.logo_seriea
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_competition);

        LoadCompetitions loadCompetitions = new LoadCompetitions();
        loadCompetitions.setLoadCompetitionsListener(new LoadCompetitions.LoadCompetitionsListener() {
            @Override
            public void onLoadedCompetitions(HashMap<String, String> competitionsMap) {
                displayCompetitions(competitionsMap);
            }

        });
        loadCompetitions.loadCompetitions(2017, -1);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        currentTournament = (Tournament) extras.getSerializable("tournament");

        System.out.println("add1 "+currentTournament.toString());
    }

    private void displayCompetitions(HashMap<String, String> competitionsMap){
        //TODO : trouver une facon plus propre d'associer keys et noms dans l'adaptater
        final ArrayList<String> competitionNames = new ArrayList<>(competitionsMap.values());
        final ArrayList<String> competitionId = new ArrayList<>(competitionsMap.keySet());
        CompetitionAdapter adapter = new
                CompetitionAdapter(ChooseCompetitionActivity.this, competitionNames);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ChooseCompetitionActivity.this, "You Clicked at " + competitionNames.get(+position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseCompetitionActivity.this, AddMatchToTournament.class);
                intent.putExtra("url","http://api.football-data.org/v1/competitions/"+ competitionId.get(position) +"/fixtures?timeFrame=n7");
                intent.putExtra("tournament", currentTournament);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }
}