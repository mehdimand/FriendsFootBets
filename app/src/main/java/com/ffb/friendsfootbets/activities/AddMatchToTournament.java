package com.ffb.friendsfootbets.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ffb.friendsfootbets.adapters.CustomList;
import com.ffb.friendsfootbets.R;

public class AddMatchToTournament extends AppCompatActivity {

    ListView list;
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

    Integer[] imageUrl = {
            R.string.url_ldc,
            R.string.url_pl,
            R.string.url_liga,
            R.string.url_l1,
            R.string.url_facup,
            R.string.url_bundesliga,
            R.string.url_seriea
    };

    public final static String Url_fixtures = "matches" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match_to_tournament);

        CustomList adapter = new
                CustomList (AddMatchToTournament.this, txt, imageId);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AddMatchToTournament.this, "You Clicked at " + txt[+position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddMatchToTournament.this, AddMatchToTournament2.class);
                intent.putExtra(Url_fixtures,imageUrl[position]);
                startActivity(intent);
            }
        });

    }

}