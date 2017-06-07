package com.ffb.friendsfootbets.databaselink;

import com.ffb.friendsfootbets.models.Match;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Fouad-Sams on 31/05/2017.
 */

public class SaveTournament {

    private DatabaseReference mDatabase;
    private SaveTournamentListener listener;

    public SaveTournament() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSaveTournamentListener(SaveTournamentListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface SaveTournamentListener {
        // This trigger will be used every time we finish loading the tournaments related to a user
        public void onTournamentCreated(Tournament tournament);
    }

    /* Returns null is the name is already taken */
    public Tournament createTournament(User admin, String tournamentName){
        DatabaseReference tournamentRef = mDatabase.child("tournaments");
        // Generate a reference to a new location and add some data using push()
        DatabaseReference pushedTournamentRef = tournamentRef.push();

        // Get the unique ID generated by a push()
        String tournamentId = pushedTournamentRef.getKey();

        Tournament tournament = new Tournament(tournamentName, admin.getUsername());
        tournament.setTouranmentId(tournamentId);
        tournament.addUser(admin.getUsername());

        addTournamentInDatabase(tournament);

        return tournament;

    }

    public void addTournamentInDatabase(Tournament tournament){
        mDatabase.child("tournaments").child(tournament.getTouranmentId()).child("TournamentName").setValue(tournament.getTournamentName());
        mDatabase.child("tournaments").child(tournament.getTouranmentId()).child("TournamentAdmin").setValue(tournament.getTournamentAdminUsername());
        mDatabase.child("userTournament").child(tournament.getTournamentAdminUsername()).child(tournament.getTouranmentId()).setValue(true);
        // We set the number of points of the new user to 0
        mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("usersPoints").child(tournament.getTournamentAdminUsername()).setValue(0);
    }

    /* User is no longer invited and becomes member of the tournament*/
    public void addUserToTournament(User user, Tournament tournament){
        // We set the number of points of the new user to 0
        mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("usersPoints").child(user.getUsername()).setValue(0);

        //The user is no longer invited
        mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("invitedUsers").child(user.getUsername()).removeValue();

        mDatabase.child("userTournament").child(user.getUsername()).child(tournament.getTouranmentId()).setValue(true);

    }

    /*User is invited to the tournament*/
    public void inviteUserToTournament(User user, Tournament tournament){
        // Add the user to the list of invited users
        mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("invitedUsers").child(user.getUsername()).setValue(true);
        // Add the tournament to the user's list of tournament
        mDatabase.child("userTournament").child(user.getUsername()).child(tournament.getTouranmentId()).setValue(false);

    }

    /* User is kicked out of the tournament */
    public void removeUserFromTournament(User user, Tournament Tournament){

    }

    /* User refuses the invitation */
    public void removeInvitedUser(User user, Tournament tournament){
        // Add the user to the list of invited users
        mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("invitedUsers").child(user.getUsername()).removeValue();
        // Add the tournament to the user's list of tournament
        mDatabase.child("userTournament").child(user.getUsername()).child(tournament.getTouranmentId()).removeValue();
    }

    public void addMatchtoTournament(Tournament tournament, String matchId){
        mDatabase.child("tournamentMatches").child(tournament.getTouranmentId()).child(matchId).setValue(true);
    }
}
