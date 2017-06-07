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

public class SaveUser {

    private DatabaseReference mDatabase;

    public SaveUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void followUser(User currentUser, User userToFollow) {
        mDatabase.child("userFollowed").child(currentUser.getUsername()).child(userToFollow.getUsername()).setValue(true);
    }
    public void addBet(User user, Match match, String bet){
        mDatabase.child("userMatches").child(user.getUsername()).child(match.getMatchId()).setValue(bet);
    }
}
