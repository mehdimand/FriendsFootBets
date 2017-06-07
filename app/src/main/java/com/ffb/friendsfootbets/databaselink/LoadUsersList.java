package com.ffb.friendsfootbets.databaselink;

import android.util.Log;

import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by Fouad-Sams on 29/05/2017.
 */

public class LoadUsersList {

    // Attributes related to database link
    private DatabaseReference mDatabase;
    private LoadUsersListListener listener;

    // Attributes related to the objects that are handled, key : id, value : tournament instance
    public HashMap<String, User> usersListMap;


    public LoadUsersList() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoadUsersListListener(LoadUsersListListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface LoadUsersListListener {
        // This trigger will be used every time we finish loading the tournaments related to a user
        public void onUsersListLoaded(HashMap<String, User> userListMap);
    }

    // this attribute enables to count how many tournaments have been downloaded from the database
    int usersLoadedCounter;
    int usersNumber;
    public void loadUsers(ArrayList<String> usernamesList, boolean withMatches){
        // We  initiate the counter
        usersLoadedCounter = 0;
        usersListMap = new HashMap<>();

        // We compute the number of users to load in order to know when to stop
        if (usernamesList != null){
            usersNumber = usernamesList.size();
        }else{
            usersNumber = 0;
        }


        for (int i = 0; i < usersNumber ; i++){
            String username = usernamesList.get(i);
            User user = new User(username);
            usersListMap.put(username, user);

            loadUser(username, withMatches);
        }

        // we need to multiply this variable because there will be twice as much calls in this case
        if (withMatches){
            usersNumber = usersNumber*2;
        }

    }
    /*
     * This method loads from the database a single tournament.
     */
    public void loadUser(final String username, final boolean withMatches){
        // We set the references for the data we want from the database
        DatabaseReference usersRef = mDatabase.child("users").child(username);

        // We create the one time listener that will connect to the database
        ValueEventListener usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object
                User user = usersListMap.get(username);
                user.setName((String) dataSnapshot.child("name").getValue());
                user.setEmail((String) dataSnapshot.child("email").getValue());
                // In some cases the profilePicture key isn't set (when no picture is chosen)
                Object tempBoolProfilePicture = dataSnapshot.child("profilePicture").getValue();
                user.setProfilePicture((tempBoolProfilePicture != null) && (boolean) tempBoolProfilePicture);
                userLoadedTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };


        // We associate the two
        usersRef.addListenerForSingleValueEvent(usersListener);

        if (withMatches){
            DatabaseReference userMatchesRef = mDatabase.child("userMatches").child(username);
            ValueEventListener userMatchesListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get User object
                    User user = usersListMap.get(username);

                    HashMap<String, String> betsMap = new HashMap<>();
                    for (DataSnapshot betsDataSnapshot : dataSnapshot.getChildren()) {
                        String matchId = betsDataSnapshot.getKey();
                        String bet = (String) betsDataSnapshot.getValue();
                        betsMap.put(matchId, bet);
                    }
                    user.setBets(betsMap);

                    userLoadedTrigger();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            userMatchesRef.addListenerForSingleValueEvent(userMatchesListener);
        }
    }

    private void userLoadedTrigger(){
        // Each time this method is called we increment the counter
        usersLoadedCounter++;

        // The third time this method is called we can trigger onUserLoaded to enable the activity
        // to resume
        if (usersLoadedCounter == usersNumber && listener != null){
            listener.onUsersListLoaded(usersListMap);
        }
    }

    public void searchUsername(String usernameQuery, int maxNumber){
        usersListMap = new HashMap<>();
        Query query = mDatabase.child("users").orderByKey().startAt(usernameQuery).endAt(usernameQuery+"~").limitToFirst(maxNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = "";
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    username = usersSnapshot.getKey();
                    User user = new User(username);

                    user.setName((String) usersSnapshot.child("name").getValue());
                    user.setEmail((String) usersSnapshot.child("email").getValue());
                    // In some cases the profilePicture key isn't set (when no picture is chosen)
                    Object tempBoolProfilePicture = usersSnapshot.child("profilePicture").getValue();
                    user.setProfilePicture((tempBoolProfilePicture != null) && (boolean) tempBoolProfilePicture);

                    usersListMap.put(username, user);
                }

                if (usersListMap.size() > 0){
                    listener.onUsersListLoaded(usersListMap);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
