package com.ffb.friendsfootbets.databaselink;

import android.util.Log;

import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by Fouad-Sams on 24/05/2017.
 */

public class LoadTournaments {

    // Attributes related to database link
    private DatabaseReference mDatabase;
    private LoadTournamentsListener listener;

    // Attributes related to the objects that are handled, key : id, value : tournament instance
    public HashMap<String, Tournament> tournamentsMap;


    public LoadTournaments() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoadTournamentsListener(LoadTournamentsListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface LoadTournamentsListener {
        // This trigger will be used every time we finish loading the tournaments related to a user
        public void onTournamentsLoaded(HashMap<String, Tournament> tournamentsMap);
    }

    // this attribute enables to count how many tournaments have been downloaded from the database
    int tournamentLoadedCounter;
    int tournamentNumber;
    public void loadTournaments(User user){
        // We  initiate the counter
        tournamentLoadedCounter = 0;
        tournamentsMap = new HashMap<>();
        ArrayList<String> tournamentsInvited = user.getTournamentsInvited();
        ArrayList<String> tournamentsAccepted = user.getTournamentsAccepted();
        // We compute the number of tournament to load in order to know when to stop
        tournamentNumber =  tournamentsInvited.size() + tournamentsAccepted.size();

        String tournamentId;
        for (int i = 0; i < tournamentsInvited.size(); i++){
            tournamentId = tournamentsInvited.get(i);
            Tournament tournament = new Tournament(tournamentId);
            tournamentsMap.put(tournamentId, tournament);

            loadTournament(tournamentId, true);
        }

        for (int i = 0; i < tournamentsAccepted.size(); i++){
            tournamentId = tournamentsAccepted.get(i);
            Tournament tournament = new Tournament(tournamentId);
            tournamentsMap.put(tournamentId, tournament);

            loadTournament(tournamentId, false);
        }
    }
    /*
     * This method loads from the database a single tournament.
     */
    public void loadTournament(final String tournamentId, final boolean invited){
        // We set the references for the data we want from the database
        DatabaseReference tournamentRef = mDatabase.child("tournaments").child(tournamentId);
        DatabaseReference tournamentMatchesRef = mDatabase.child("tournamentMatches").child(tournamentId);

        // We create the listeners that will fetch the data we need
        ValueEventListener tournamentListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Tounrnament object
                Tournament tournament = tournamentsMap.get(tournamentId);
                tournament.setTournamentName((String) dataSnapshot.child("TournamentName").getValue());
                tournament.setTournamentAdminUsername((String) dataSnapshot.child("TournamentAdmin").getValue());
                if (invited){tournament.setState(Tournament.INVITED);}
                tournamentLoadedTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        ValueEventListener tournamentMatchesListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Tournament object
                Tournament tournament = tournamentsMap.get(tournamentId);

                ArrayList<String> tournamentMatchesList = new ArrayList<>();
                for (DataSnapshot tournamentMatchesDataSnapshot : dataSnapshot.getChildren()) {
                    String matchId = tournamentMatchesDataSnapshot.getKey();
                    tournamentMatchesList.add(matchId);
                }
                tournament.setMatchArray(tournamentMatchesList);
                tournamentLoadedTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        // We associate the two
        tournamentRef.addListenerForSingleValueEvent(tournamentListener);
        tournamentMatchesRef.addListenerForSingleValueEvent(tournamentMatchesListener);
    }

    private void tournamentLoadedTrigger(){
        // Each time this method is called we increment the counter
        tournamentLoadedCounter++;

        // The third time this method is called we can trigger onUserLoaded to enable the activity
        // to resume
        if (tournamentLoadedCounter == 2*tournamentNumber && listener != null){
            listener.onTournamentsLoaded(tournamentsMap);
            // TODO : load matches and compute tournament state
        }
    }

}
