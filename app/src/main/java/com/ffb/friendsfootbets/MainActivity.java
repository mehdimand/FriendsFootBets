package com.ffb.friendsfootbets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Controller controller;
    private Button modifyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modifyProfile = (Button) findViewById(R.id.modify_profile_button);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // We ensure that when the user isn't loaded we can't go the the modify profile page
        modifyProfile.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        String userEmail = extras.getString("userEmail");

        if (controller == null || controller.getCurrentUser() == null || controller.getCurrentUser().getEmail() == userEmail) {
            controller = new Controller();
            controller.setControllerListener(new Controller.ControllerListener() {

                @Override
                public void onUserLoaded(User user) {
                    MainActivity.this.onUserLoaded(user);
                }
            });
            // Query to the database : is there a user with this email in the database ?
            controller.signIn(userEmail);
            // This method uses asynchronous tasks, therefore all other action after sign in can't be in
            // the onCreate method
        }else{
            // In case the user attributes have changed in the database while the activity was on pause
            controller.signIn(controller.getCurrentUser().getEmail());
        }
    }

    void onUserLoaded(User user){
        if (user != null){
            modifyProfile.setVisibility(View.VISIBLE);
            // TODO : display the tournaments etc of the home page
            displayTournaments();
        }
        else{
            Bundle extras = getIntent().getExtras();
            String userEmail = extras.getString("userEmail");
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("userEmail", userEmail);
            profileIntent.putExtra("firstConnection", true);
            startActivity(profileIntent);
        }
    }

    public void onClick(View view){
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("currentUser", controller.getCurrentUser());
        profileIntent.putExtra("firstConnection", false);
        startActivity(profileIntent);
    }

    private void displayTournaments() {
    }


}
