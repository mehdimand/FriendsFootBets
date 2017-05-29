package com.ffb.friendsfootbets.databaselink;

import android.util.Log;

import com.ffb.friendsfootbets.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Fouad-Sams on 24/05/2017.
 */

public class LoadUser {
    // Attributes related to database link
    private DatabaseReference mDatabase;
    private LoadUserListener listener;

    // Attributes related to the objects that are handled
    private User currentUser;


    public LoadUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoadUserListener(LoadUserListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface LoadUserListener {
        // This trigger will be used every time we need to get a unique user
        public void onLoadedDataForUser(User user);
        public void userNotFound();
    }

    /*
     * This method searches for a user in the database with the email given in the parameters.
     * It returns true if the user is found in the database, and changes the currentUser to the one
     * it has found.
     */
    public void signIn(String userEmail){
        boolean userSignedIn = false;

        Query query = mDatabase.child("users").orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = "";
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    username = usersSnapshot.getKey();
                }
                //If the user is found in the database we proceed to retrieving the rest of the
                // information related to this user
                if (username != ""){
                    currentUser = new User(username);
                    getCompleteUserFromDatabase(username);
                    // the trigger that enables to go back to the activity is in the completeUserTrigger
                    // method
                }
                else{
                    currentUser = null;
                    // We directly call the method in the activity without further access to the database
                    if (listener != null) {
                        listener.userNotFound();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /* This attribute enables us to know if all 3 listners set in getCompleteUserFromDatabase have
     * been triggered.
     */
    private int completeUserCount;
    private void completeUserTrigger(){
        // Each time this method is called we increment the counter
        completeUserCount++;

        // The third time this method is called we can trigger onUserLoaded to enable the activity
        // to resume
        if (completeUserCount == 3 && listener != null){
            listener.onLoadedDataForUser(currentUser);
        }

    }

    /*
     * This method gets from the database a User instance given his username.
     * This method is needed because the data concerning one user is located in different tables in
     * the NoSQL database.
     */
    public void getCompleteUserFromDatabase(String username){
        // We get the reference for the user in the database
        completeUserCount = 0;
        DatabaseReference userInUsersRef = mDatabase.child("users").child(username);
        DatabaseReference userFollowedRef = mDatabase.child("userFollowed").child(username);
        DatabaseReference userTournamentRef = mDatabase.child("userTournament").child(username);

        // We create the one time listener that will connect to the database
        ValueEventListener usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object

                currentUser.setName((String) dataSnapshot.child("name").getValue());
                currentUser.setEmail((String) dataSnapshot.child("email").getValue());
                // In some cases the profilePicture key isn't set (when no picture is chosen)
                Object tempBoolProfilePicture = dataSnapshot.child("profilePicture").getValue();
                currentUser.setProfilePicture((tempBoolProfilePicture != null) && (boolean) tempBoolProfilePicture);
                completeUserTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        ValueEventListener usersFollowedListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object

                ArrayList<String> usersFollowedList = new ArrayList<>();
                for (DataSnapshot usersFollowedDataSnapshot : dataSnapshot.getChildren()) {
                    String usernameFollowed = usersFollowedDataSnapshot.getKey();
                    usersFollowedList.add(usernameFollowed);
                }
                currentUser.setUsersFollowed(usersFollowedList);

                // Calling the trigger to see if the other listeners have been triggered
                completeUserTrigger();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        ValueEventListener usersTournamentListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object

                ArrayList<String> tournamentAcceptedIdList = new ArrayList<>();
                ArrayList<String> tournamentInvitedIdList = new ArrayList<>();
                for (DataSnapshot tournamentIdDataSnapshot : dataSnapshot.getChildren()) {
                    String tournamentId = tournamentIdDataSnapshot.getKey();
                    // if the tournament's invitation has already been accepted, the value linked to
                    // the id of the tournament is set to true
                    if ((boolean) tournamentIdDataSnapshot.getValue()){
                        tournamentAcceptedIdList.add(tournamentId);
                    }
                    // here the tournament invitation hasn't been accepted yet
                    else {
                        tournamentInvitedIdList.add(tournamentId);
                    }
                }
                currentUser.setTournamentsAccepted(tournamentAcceptedIdList);
                currentUser.setTournamentsInvited(tournamentInvitedIdList);

                // Calling the trigger to see if the other listeners have been triggered
                completeUserTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        userInUsersRef.addListenerForSingleValueEvent(usersListener);
        userFollowedRef.addListenerForSingleValueEvent(usersFollowedListener);
        userTournamentRef.addListenerForSingleValueEvent(usersTournamentListener);

    }

    public User getCurrentUser() {
        return currentUser;
    }
}
