package com.ffb.friendsfootbets.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.adapters.UserAdapter;
import com.ffb.friendsfootbets.databaselink.LoadUsersList;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUsersToTournamentActivity extends Activity implements SearchView.OnQueryTextListener {

    private ListView followedUsersListView;
    private Tournament currentTournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users_to_tournament);

        followedUsersListView = (ListView) findViewById(R.id.list_users);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Bundle extras = getIntent().getExtras();
        currentTournament = (Tournament) extras.getSerializable("tournament");

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
        System.out.println(menu.findItem(R.id.search).getActionView().toString());
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        LoadUsersList loadUsersList = new LoadUsersList();
        loadUsersList.setLoadUsersListListener(new LoadUsersList.LoadUsersListListener() {
            @Override
            public void onUsersListLoaded(HashMap<String, User> userListMap) {
                ArrayList<User> userList = new ArrayList<User>(userListMap.values());
                UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
                followedUsersListView.setAdapter(userAdapter);
            }
        });
        loadUsersList.searchUsername(query);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        LoadUsersList loadUsersList = new LoadUsersList();
        loadUsersList.setLoadUsersListListener(new LoadUsersList.LoadUsersListListener() {
            @Override
            public void onUsersListLoaded(HashMap<String, User> userListMap) {
                ArrayList<User> userList = new ArrayList<User>(userListMap.values());
                UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
                followedUsersListView.setAdapter(userAdapter);
            }
        });
        loadUsersList.searchUsername(newText);
        return true;
    }



}
