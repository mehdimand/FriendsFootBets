package com.ffb.friendsfootbets.databaselink;

import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fouad-Sams on 31/05/2017.
 */

public class SaveTournament {

    private DatabaseReference mDatabase;

    public SaveTournament() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /* User is no longer invited and becomes member of the tournament*/
    public void addUserToTournament(User user, Tournament tournament){
        // We set the number of points of the new user to 0
        if (!(tournament.getUserArray().contains(user.getUsername()))){
            mDatabase.child("tournamentUsers").child(tournament.getTouranmentId()).child("usersPoints").child(user.getUsername()).setValue(0);
        }

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
    public void removeInvitedUser(User user, Tournament Tournament){

    }
}
