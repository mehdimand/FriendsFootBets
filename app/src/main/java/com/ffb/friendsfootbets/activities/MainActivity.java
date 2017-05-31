package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;
import com.ffb.friendsfootbets.adapters.TournamentAdapter;
import com.ffb.friendsfootbets.databaselink.LoadTournaments;
import com.ffb.friendsfootbets.databaselink.LoadUser;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private User currentUser;
    private Button modifyProfile;
    private View loadingCircle;
    private ListView tournamentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modifyProfile = (Button) findViewById(R.id.modify_profile_button);

        loadingCircle = (View) findViewById(R.id.loadingPanel);

        tournamentsListView = (ListView) findViewById(R.id.tournamentsList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // We ensure that when the user isn't loaded we can't go the the modify profile page
        modifyProfile.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail");

        LoadUser loadUser = new LoadUser();
        loadUser.setLoadUserListener(new LoadUser.LoadUserListener() {
            @Override
            public void onLoadedDataForUser(User user) {
                currentUser = user;
                MainActivity.this.onUserLoaded(user);
            }

            @Override
            public void userNotFound() {
                MainActivity.this.onUserNotFound();
            }
        });

        if (currentUser == null || currentUser.getEmail() != userEmail) {
            currentUser = new User();
            // Query to the database : is there a user with this email in the database ?
            loadUser.signIn(userEmail);
            // This method uses asynchronous tasks, therefore all other action after sign in can't be in
            // the onCreate method
        }else{
            // In case the user attributes have changed in the database while the activity was on pause
            loadUser.signIn(currentUser.getEmail());
        }
    }

    void onUserLoaded(User user) {
        modifyProfile.setVisibility(View.VISIBLE);
        // TODO : display the tournaments etc of the home page
        LoadTournaments loadTournaments = new LoadTournaments();
        loadTournaments.setLoadTournamentsListener(new LoadTournaments.LoadTournamentsListener() {

            @Override
            public void onTournamentsLoaded(HashMap<String, Tournament> tournamentsMap) {
                displayTournaments(tournamentsMap);
            }
        });
        loadTournaments.loadTournaments(currentUser);
    }
    void onUserNotFound(){
        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail");
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("userEmail", userEmail);
        profileIntent.putExtra("firstConnection", true);
        startActivity(profileIntent);
    }
    private void displayTournaments(HashMap<String, Tournament> tournamentsMap) {
        // We remove the loading circle
        loadingCircle.setVisibility(View.GONE);

        // Display the tournaments in the listview
        final ArrayList<Tournament> tournamentsList = new ArrayList<>(tournamentsMap.values());
        TournamentAdapter tournamentAdapter = new TournamentAdapter(this, tournamentsList);
        tournamentsListView.setAdapter(tournamentAdapter);

        tournamentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                Tournament  tournament = tournamentsList.get(position);

                Intent tournamentIntent = new Intent(getApplicationContext(), TournamentActivity.class);
                tournamentIntent.putExtra("currentUser", currentUser);
                tournamentIntent.putExtra("tournament", tournament);
                startActivity(tournamentIntent);

            }
        });
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.modify_profile_button:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("currentUser", currentUser);
                profileIntent.putExtra("firstConnection", false);
                startActivity(profileIntent);
                break;
        }

    }



}
