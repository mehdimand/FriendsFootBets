package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

public class TournamentActivity extends AppCompatActivity {

    User currentUser;
    Tournament currentTournament;

    private TextView tournamentNameView;
    private TextView tournamentAdminView;
    private ListView usersListView;
    private ListView matchesListView;
    private ImageButton addUserButton;
    private ImageButton addMatchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        tournamentNameView = (TextView) findViewById(R.id.tournamentName);
        tournamentAdminView = (TextView) findViewById(R.id.tournamentAdmin);
        usersListView = (ListView) findViewById(R.id.usersList);
        matchesListView = (ListView) findViewById(R.id.matchesList);

        addUserButton = (ImageButton) findViewById(R.id.add_user_button);
        addMatchButton = (ImageButton) findViewById(R.id.add_match_button);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Bundle extras = getIntent().getExtras();
        currentUser = (User) extras.getSerializable("currentUser");
        currentTournament = (Tournament) extras.getSerializable("tournament");

        tournamentNameView.setText(currentTournament.getTournamentName());
        tournamentAdminView.setText(currentTournament.getTournamentAdminUsername());

        /*if (currentUser.getUsername() != currentTournament.getTournamentAdminUsername()){
            addUserButton.setVisibility(View.GONE);
        }else {
            addUserButton.setVisibility(View.VISIBLE);
        }*/


    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.add_user_button:
                Intent addUserIntent = new Intent(this, AddUsersToTournamentActivity.class);
                addUserIntent.putExtra("tournament", currentTournament);
                addUserIntent.putExtra("user", currentUser);
                startActivity(addUserIntent);
                break;
        }

    }
}
