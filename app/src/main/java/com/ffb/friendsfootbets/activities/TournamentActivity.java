package com.ffb.friendsfootbets.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffb.friendsfootbets.LoadSpecificFixtures;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.AddMatchAdapter;
import com.ffb.friendsfootbets.adapters.UserAdapter;
import com.ffb.friendsfootbets.adapters.UserPointsAdapter;
import com.ffb.friendsfootbets.databaselink.LoadUsersList;
import com.ffb.friendsfootbets.databaselink.SaveUser;
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

    private ListView bettersListView;
    private RelativeLayout toHide;
    private ImageButton button11;
    private ImageButton button12;
    private ImageButton button21;
    private ImageButton button22;
    private TextView scoreH;
    private TextView scoreA;

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
        // Users
        LoadUsersList loadUsersList = new LoadUsersList();
        loadUsersList.setLoadUsersListListener(new LoadUsersList.LoadUsersListListener() {
            @Override
            public void onUsersListLoaded(HashMap<String, User> userListMap) {
                TournamentActivity.this.userMap = userListMap;
                onUserOrMatchesLoaded();
            }
        });
        loadUsersList.loadUsers(currentTournament.getUserArray(), true);

        // Matches
        LoadSpecificFixtures loadFixtures = new LoadSpecificFixtures();
        loadFixtures.setLoadFixturesListener(new LoadSpecificFixtures.LoadFixturesListener() {
            @Override
            public void onFixturesLoaded(HashMap<String, Match> fixtures_list) {
                matchMap = fixtures_list;
                onUserOrMatchesLoaded();
            }
        });
        loadFixtures.loadFixtures(currentTournament.getMatchArray());

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
        System.out.println("on User or match loaded");
        if (counter == 2){
            ArrayList<User> userList = new ArrayList<User>(userMap.values());
            displayUserListAndMatches(userList);
        }
    }

    private void displayUserListAndMatches(final ArrayList<User> userList) {
        // We compute the number of points for every user
        computeUserPoints(userList, matchMap);
        // We sort the array of usernames by points
        ArrayList<String> sortedUsernames = currentTournament.sortUsersByPoints();

        // We link each username to its user instances for display purposes
        ArrayList<User> sortedUsers = new ArrayList<>();
        for (String username : sortedUsernames){
            User user = userMap.get(username);
            sortedUsers.add(user);
        }

        // Display the users and the matches in their respective listviews
        // Members of the tournament
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
        usersListView.setAdapter(userAdapter);

        // Rankings
        UserPointsAdapter userPointsAdapter = new UserPointsAdapter(getApplicationContext(),
                sortedUsers, currentTournament.getPoints());
        rankingsListView.setAdapter(userPointsAdapter);

        // Matches of the tournament
        final ArrayList<Match> matchesList = new ArrayList<>(matchMap.values());
        if (matchesList.size()>0){
            System.out.println("Match :"+matchesList.get(0).toString());
        }
        AddMatchAdapter matchAdapter = new AddMatchAdapter(getApplicationContext(), matchesList);
        matchesListView.setAdapter(matchAdapter);
        matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(TournamentActivity.this);

                final EditText edittext = new EditText(TournamentActivity.this);
                alert.setMessage(" What is your bet ?");
                alert.setTitle("Bet");

                alert.setView(edittext);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SaveUser saveUser = new SaveUser();
                        saveUser.addBet(currentUser, matchesList.get(position), edittext.getText().toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();


                /*toHide = (RelativeLayout) view.findViewById(R.id.tohide);
                boolean a = toHide.getVisibility()==View.GONE;

                for (int i = 0; i < matchesList.size(); i++) {
                    View listitem = parent.getChildAt(i);
                    toHide = (RelativeLayout) listitem.findViewById(R.id.tohide);
                    toHide.setVisibility(View.GONE);
                }

                if (a) {
                    toHide.setVisibility(View.VISIBLE);
                }

                if (!(currentUser.getBets().keySet().contains(matchesList.get(position).getMatchId()))){

                    button11 = (ImageButton) toHide.findViewById(R.id.imageButton11);
                    button12 = (ImageButton) toHide.findViewById(R.id.imageButton12);
                    button21 = (ImageButton) toHide.findViewById(R.id.imageButton21);
                    button22 = (ImageButton) toHide.findViewById(R.id.imageButton22);

                    button11.setVisibility(View.VISIBLE);
                    button12.setVisibility(View.VISIBLE);
                    button21.setVisibility(View.VISIBLE);
                    button22.setVisibility(View.VISIBLE);


                    scoreH = (TextView) findViewById(R.id.parihome);
                    scoreA = (TextView) findViewById(R.id.pariaway);

                    button11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int n = Integer.parseInt((String) scoreH.getText()) + 1;
                            scoreH.setText(String.valueOf(n));
                        }
                    });

                    button12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int n = Math.max(Integer.parseInt((String) scoreH.getText()) - 1,0);
                            scoreH.setText(String.valueOf(n));
                        }
                    });

                    button21.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int n = Integer.parseInt((String) scoreA.getText()) + 1;
                            scoreH.setText(String.valueOf(n));
                        }
                    });

                    button22.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int n = Math.max(Integer.parseInt((String) scoreA.getText()) - 1,0);
                            scoreH.setText(String.valueOf(n));
                        }
                    });



                }
                else{
                    //recupere les bets de ce match betterList
                    ArrayList<User> bettersList = new ArrayList<User>(matchesList.get(position).getBets().keySet());
                    UserBetsAdapter userBetsAdapter = new UserBetsAdapter(getApplicationContext(), bettersList, matchesList.get(position).getBets());
                    bettersListView = (ListView) toHide.findViewById(R.id.bettersList);
                    bettersListView.setAdapter(userBetsAdapter);
                    bettersListView.setVisibility(View.VISIBLE);
                }*/
            }
        });


    }

    private void computeUserPoints(ArrayList<User> userList, HashMap<String, Match> matchMap) {
        currentTournament.resetPoints();
        for (User user : userList){
            // This map links a matchId to a bet in the "homeScore-awayScore" format
            HashMap<String, String> userBets = user.getBets();
            for (String matchId : userBets.keySet()){
                if (matchMap.containsKey(matchId)){
                    Match match = matchMap.get(matchId);
                    // We add the bet to the match in order to display the bets more easily
                    match.addBet(user, userBets.get(matchId));
                    // We compute the number of points the user deserves for this on bet
                    int additionalPoints = pointsForBet(match, userBets.get(matchId));
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
                Intent addMatchIntent = new Intent(this, ChooseCompetitionActivity.class);
                addMatchIntent.putExtra("tournament", currentTournament);
                addMatchIntent.putExtra("user", currentUser);
                startActivity(addMatchIntent);
                break;
        }


    }
}
