package com.ffb.friendsfootbets.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.User;
import com.ffb.friendsfootbets.adapters.UserAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    final int PIC_CROP = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private EditText usernameView;
    private EditText nameView;
    private ImageView imageView;
    private View profileFormView;
    private View progressView;
    private boolean firstConnection;
    private boolean messageShown = false;
    private String userEmail;
    private User currentUser;
    private int numberFollowedUsers;
    ArrayList<User> followedUserList;
    private ListView followedUsersListView;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;

    private Uri fileUri; // file url to store image/video
    private boolean changingProfilePicture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        imageView = (ImageView) findViewById(R.id.userPicture);

        usernameView = (EditText) findViewById(R.id.username);

        nameView = (EditText) findViewById(R.id.name);

        Button addPictureButton = (Button) findViewById(R.id.add_picture_button);
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
                changingProfilePicture = true;
                //uploadUserPicture();
            }
        });

        Button modifyProfileButton = (Button) findViewById(R.id.modify_profile_button);
        modifyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptModifyProfile();
            }
        });

        Button followUserButton = (Button) findViewById(R.id.follow_user_profile_button);
        followUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddUsersToFollowedActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

        profileFormView = findViewById(R.id.username_form);
        progressView = findViewById(R.id.modify_profile_progress);
        followedUsersListView = (ListView) findViewById(R.id.list_followed_users);


        // Get the relevant information passed with the activity change (in the onCreate rather that
        // in the onStarted because the call to the camera can create issues)
        Bundle extras = getIntent().getExtras();
        firstConnection = extras.getBoolean("firstConnection");
        if (firstConnection) {
            userEmail = extras.getString("userEmail");
        }
        else{
            currentUser = (User) extras.getSerializable("currentUser");
            userEmail = currentUser.getEmail();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If it's the first connection, we explain the goal of this activity
        if (firstConnection && !messageShown){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("For your first connection you need to setup your profile.");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)  {
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            messageShown = true;
        } // if the user wants to modify his profile, he cannot change his username
        else if (!firstConnection){
            // A user that has already set his username cannot change it
            usernameView.setVisibility(View.GONE);
            // We auto fill the form with data from the database
            nameView.setText(currentUser.getName());
            // If the user has a profile picture, then we display it
            if (currentUser.hasProfilePicture() && !changingProfilePicture){
                showUserPicture(currentUser.getUsername());
            }
            // Afficher la liste des utilisateurs suivis
            loadFollowedUsers();

        }


    }
    private void loadFollowedUsers(){
        // We get the number of followed users in order to know when all the users have been loaded
        numberFollowedUsers = currentUser.getUsersFollowed().size();
        followedUserList = new ArrayList<User> ();

        // We create the one time listener that will connect to the database
        ValueEventListener usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object
                User user = new User(dataSnapshot.getKey());
                user.setName((String) dataSnapshot.child("name").getValue());
                user.setEmail((String) dataSnapshot.child("email").getValue());
                // In some cases the profilePicture key isn't set (when no picture is chosen)
                Object tempBoolProfilePicture = dataSnapshot.child("profilePicture").getValue();
                user.setProfilePicture((tempBoolProfilePicture != null) && (boolean) tempBoolProfilePicture);
                followedUserList.add(user);
                followedUsersLoadedTrigger();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        numberUsersLoaded = 0;
        for (int i = 0; i < numberFollowedUsers; i++){
            String username = currentUser.getUsersFollowed().get(i);
            DatabaseReference userInUsersRef = mDatabase.child("users").child(username);
            userInUsersRef.addListenerForSingleValueEvent(usersListener);
        }
    }
    private int numberUsersLoaded;
    private void followedUsersLoadedTrigger() {
        numberUsersLoaded++;
        if (numberUsersLoaded == numberFollowedUsers){
            UserAdapter userAdapter = new UserAdapter(this, followedUserList);
            followedUsersListView.setAdapter(userAdapter);


        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showLoadingCircle(boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        profileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /* --- Part of the code related to the profile picture --- */
    private void showUserPicture(String username) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Reference to user's picture
        StorageReference userPictureRef = storageRef.child("profilePicture/"+username+".jpg");

        imageView.setVisibility(View.VISIBLE);
        // Load the image using Glide
        //TODO : solve issue size picture changing if loaded from camera or internet
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(userPictureRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);

        // TODO : add progress bar
    }

    private void uploadUserPicture(String username) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Reference to user's picture
        StorageReference userPictureRef = storageRef.child("profilePicture/" + username + ".jpg");

        // These lines throw a ClassCastException sometimes
        Bitmap bitmap;
        try{
            Drawable drawable = imageView.getDrawable();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            bitmap = bitmapDrawable .getBitmap();
        }
        catch (ClassCastException e){
            GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) imageView.getDrawable();
            bitmap = bitmapDrawable .getBitmap();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);

        /*
        int newWidth = 500;
        int newHeight = 500;
        Bitmap picture;
        Bitmap resized = Bitmap.createScaledBitmap(picture, newWidth, newHeight, true);
        */

        UploadTask uploadTask = userPictureRef.putStream(bis);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(ProfileActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            // TODO : find another way to crop/resize the picture

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");
                imageView.setVisibility(View.VISIBLE);

                // We recycle the imageView only if it's already filled with a picture
                if (imageView.getDrawable() != null){
                    try{
                        ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
                    }catch (ClassCastException e){
                        ((GlideBitmapDrawable)imageView.getDrawable().getCurrent()).getBitmap().recycle(); /* without getCurrent there is a class cast exception*/
                    }
                }


                imageView.setImageBitmap(selectedBitmap);
            }
        }
        else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // we try to crop the image
                performCrop(fileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
          return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        // we save the picture in a directory related to the app
        File mediaStorageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profilePicture");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("FFB", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    // This method verifies if the username isn't already taken and adds the user to the database
    // or 
    private void attemptModifyProfile(){
        // TODO : add the functionality follow user

        showLoadingCircle(true);
        // Get the data of the form only if it is the first connection
        if (firstConnection){
            String username = usernameView.getText().toString();

            // Check if the username exists
            DatabaseReference userInUsersRef = mDatabase.child("users").child(username);

            ValueEventListener usersListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get User object
                    boolean usernameUsed = false;
                    for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                        usernameUsed = true;
                    }
                    if (!usernameUsed){
                        modifyProfile();
                    }
                    else {
                        showLoadingCircle(false);
                        usernameView.setError("Username already used");
                        usernameView.requestFocus();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    showLoadingCircle(false);
                }
            };

            userInUsersRef.addListenerForSingleValueEvent(usersListener);
        } else{
            modifyProfile();
        }
    }

    private void modifyProfile(){
        // Get data from the form :
        String username;
        if (firstConnection){
            username = usernameView.getText().toString();
        }else{
            username = currentUser.getUsername();
        }
        String name = nameView.getText().toString();
        boolean hasProfilePicture = imageView.getDrawable() != null;
        if (hasProfilePicture){
            uploadUserPicture(username);
        }

        // Set the values in the database
        mDatabase.child("users").child(username).child("name").setValue(name);
        mDatabase.child("users").child(username).child("profilePicture").setValue(hasProfilePicture);
        mDatabase.child("users").child(username).child("email").setValue(userEmail);

        Toast.makeText(this, "Your modifications have been taken into account", Toast.LENGTH_SHORT).show();
        showLoadingCircle(false);
        changingProfilePicture = false;
        finish();
        //TODO : add a method that cleans the folder of profile pictures (for exemple by deleting all files with a time stamp)
    }

}
