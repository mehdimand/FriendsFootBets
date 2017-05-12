package com.ffb.friendsfootbets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AddMatchToTournament extends Activity {

    ListView list;
    String[] txt = {
            "Ligue des Champions",
            "Premier League",
            "Liga",
            "Ligue 1",
            "Ligue Europa",
            "Bundesliga",
            "Serie A"
    };
    Integer[] imageId = {
            R.drawable.logo_ldc,
            R.drawable.logo_pl,
            R.drawable.logo_liga,
            R.drawable.logo_ligue1,
            R.drawable.logo_europa,
            R.drawable.logo_bundesliga,
            R.drawable.logo_seriea
    };


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

            }
        });

    }

}