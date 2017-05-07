package com.ffb.friendsfootbets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
        }
    }

    void onUserLoaded(User user){
        if (user != null){
            // TODO : display the tournaments etc of the home page
            System.out.println("User found !");
            System.out.println(user.toString());

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


}
