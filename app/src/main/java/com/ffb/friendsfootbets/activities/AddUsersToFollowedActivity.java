package com.ffb.friendsfootbets.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.UserAdapter;
import com.ffb.friendsfootbets.databaselink.LoadUsersList;
import com.ffb.friendsfootbets.databaselink.SaveTournament;
import com.ffb.friendsfootbets.databaselink.SaveUser;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUsersToFollowedActivity extends AppCompatActivity {

    private ListView usersListView;
    private View loadingCircle;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_to_tournament);

        usersListView = (ListView) findViewById(R.id.list_users);

        loadingCircle = (View) findViewById(R.id.loadingPanel);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Bundle extras = getIntent().getExtras();
        currentUser = (User) extras.getSerializable("user");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // remove the previous elements of the listview after a query
                loadingCircle.setVisibility(View.VISIBLE);
                usersListView.setVisibility(View.GONE);

                LoadUsersList loadUsersList = new LoadUsersList();
                loadUsersList.setLoadUsersListListener(new LoadUsersList.LoadUsersListListener() {
                    @Override
                    public void onUsersListLoaded(HashMap<String, User> userListMap) {
                        ArrayList<User> userList = new ArrayList<User>(userListMap.values());
                        displayUserList(userList);
                    }
                });
                loadUsersList.searchUsername(newText, 10);

                return true;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void displayUserList(final ArrayList<User> userList){
        // if the user has already been added or invited to the tournament, we delete him from the list
        ArrayList<String> alreadyFollowedUsers = currentUser.getUsersFollowed();
        final ArrayList<User>  usersToDisplay = new ArrayList<>();
        for (User user : userList){
            if ( !(alreadyFollowedUsers.contains(user.getUsername())) ){
                usersToDisplay.add(user);
            }
        }
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), usersToDisplay);
        usersListView.setAdapter(userAdapter);

        loadingCircle.setVisibility(View.GONE);
        usersListView.setVisibility(View.VISIBLE);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                User user = usersToDisplay.get(position);
                askFollowUser(user);


            }
        });


    }

    public void askFollowUser(final User user){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddUsersToFollowedActivity.this);
        alertDialogBuilder.setMessage("Do you want to follow "+user.getName()+" ?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)  {
                followUser(user);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)  {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void followUser(User user){
        SaveUser saveUser = new SaveUser();
        saveUser.followUser(currentUser, user);
        currentUser.addUserFollowed(user.getUsername());
    }

}
