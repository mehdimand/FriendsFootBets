package com.ffb.friendsfootbets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.UserAdapter;
import com.ffb.friendsfootbets.adapters.UserPointsAdapter;
import com.ffb.friendsfootbets.databaselink.LoadUsersList;
import com.ffb.friendsfootbets.models.Match;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class TournamentActivity extends AppCompatActivity {

    User currentUser;
    Tournament currentTournament;

    private TextView tournamentNameView;
    private TextView tournamentAdminView;
    private ListView usersListView;
    private ListView matchesListView;
    private ListView rankingsListView;
    private ImageButton addUserButton;
    private ImageButton addMatchButton;

    private HashMap<String, User> userMap;
    private HashMap<String, Match> matchMap;

    //is set to 2 when both the users and the matches have been loaded
    int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        tournamentNameView = (TextView) findViewById(R.id.tournamentName);
        tournamentAdminView = (TextView) findViewById(R.id.tournamentAdmin);
        usersListView = (ListView) findViewById(R.id.usersList);
        matchesListView = (ListView) findViewById(R.id.matchesList);
        rankingsListView = (ListView) findViewById(R.id.rankingList);

        addUserButton = (ImageButton) findViewById(R.id.add_user_button);
        addMatchButton = (ImageButton) findViewById(R.id.add_match_button);

    }

    @Override
    protected void onStart(){
        super.onStart();
        //reinitialize the counter
        counter = 0;

        // fetch data in the intent
        Bundle extras = getIntent().getExtras();
        currentUser = (User) extras.getSerializable("currentUser");
        currentTournament = (Tournament) extras.getSerializable("tournament");

        //fetch the list of matches, users and rankings
        LoadUsersList loadUsersList = new LoadUsersList();
        loadUsersList.setLoadUsersListListener(new LoadUsersList.LoadUsersListListener() {
            @Override
            public void onUsersListLoaded(HashMap<String, User> userListMap) {
                TournamentActivity.this.userMap = userListMap;
                onUserOrMatchesLoaded();
            }
        });
        loadUsersList.loadUsers(currentTournament.getUserArray(), true);



        // Setting the text views
        tournamentNameView.setText(currentTournament.getTournamentName());
        tournamentAdminView.setText(currentTournament.getTournamentAdminUsername());


        // if user is admin
        /*if (currentUser.getUsername() != currentTournament.getTournamentAdminUsername()){
            addUserButton.setVisibility(View.GONE);
            addMatchButton.setVisibility(View.GONE);
        }else {
            addUserButton.setVisibility(View.VISIBLE);
            addMatchButton.setVisibility(View.VISIBLE);
        }*/


    }

    private void onUserOrMatchesLoaded(){
        counter++;
        if (counter == 2){
            ArrayList<User> userList = new ArrayList<User>(userMap.values());
            displayUserList(userList);
        }
    }

    private void displayUserList(final ArrayList<User> userList) {
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
        usersListView.setAdapter(userAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                User user = userList.get(position);

            }
        });
        computeUserPoints(userList, matchMap);
        ArrayList<String> sortedUsernames = currentTournament.sortUsersByPoints();
        ArrayList<User> sortedUsers = new ArrayList<>();
        for (String username : sortedUsernames){
            User user = userMap.get(username);
            sortedUsers.add(user);
        }

        UserPointsAdapter userPointsAdapter = new UserPointsAdapter(getApplicationContext(),
                userList, currentTournament.getPoints());
        usersListView.setAdapter(userAdapter);

    }

    private void computeUserPoints(ArrayList<User> userList, HashMap<String, Match> matchMap) {
        for (User user : userList){
            HashMap<String, String> bets = user.getBets();
            for (String matchId : bets.keySet()){
                if (matchMap.containsKey(matchId)){
                    Match match = matchMap.get(matchId);
                    int additionalPoints = pointsForBet(match, bets.get(matchId));
                    currentTournament.addPointsForUser(user.getUsername(), additionalPoints);
                }
            }
        }
    }

    private int pointsForBet(Match match, String bet) {
        String[] betArray = bet.split("-");
        int homeBet = Integer.parseInt(betArray[0]);
        int awayBet = Integer.parseInt(betArray[1]);

        int homeGoals = match.getScoreHomeTeam();
        int awayGoals = match.getScoreAwayTeam();

        if (homeBet == homeGoals && awayBet == awayGoals){
            //exact score
            return 100;
        }else if (homeBet - awayBet == homeGoals - awayGoals){
            //exact goal average
            return 75;
        } else if ((homeBet - awayBet)*(homeGoals - awayGoals) >= 0 ){
            //exact winner
            return 50;
        } else{
            //lost bet
            return 0;
        }
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.add_user_button:
                Intent addUserIntent = new Intent(this, AddUsersToTournamentActivity.class);
                addUserIntent.putExtra("tournament", currentTournament);
                addUserIntent.putExtra("user", currentUser);
                startActivity(addUserIntent);
                break;
            case R.id.add_match_button:
                Intent addMatchIntent = new Intent(this, AddMatchToTournament.class);
                addMatchIntent.putExtra("tournament", currentTournament);
                addMatchIntent.putExtra("user", currentUser);
                startActivity(addMatchIntent);
                break;
        }


    }
}
