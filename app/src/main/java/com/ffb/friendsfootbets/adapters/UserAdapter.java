package com.ffb.friendsfootbets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Younes and Mehdi on 12/05/2017.
 */
// Adapted from : http://tutos-android-france.com/listview-afficher-une-liste-delements/
public class UserAdapter extends ArrayAdapter<User> {

    //Users est la liste des models à afficher
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_users_row,parent, false);
        }

        UserViewHolder viewHolder = (UserViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new UserViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.profilePicture = (ImageView) convertView.findViewById(R.id.profilePicture);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<User> Users
        User user = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(user.getName());
        viewHolder.username.setText(user.getUsername());
        displayProfilePicture(user, viewHolder.profilePicture);


        return convertView;
    }

    private void displayProfilePicture(User user, ImageView imageView) {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference userPictureRef;
        if (user.hasProfilePicture()){
            // Reference to user's picture
            userPictureRef = storageRef.child("profilePicture/"+user.getUsername()+".jpg");
        }else{
            // Reference to user's picture
            userPictureRef = storageRef.child("images/nophoto.png");
        }
        // Reference to user's picture

        imageView.setVisibility(View.VISIBLE);
        // Load the image using Glide
        Glide.with(this.getContext() /* context */)
                .using(new FirebaseImageLoader())
                .load(userPictureRef)
                .into(imageView);

        // TODO : add progress bar
    }

    private class UserViewHolder{
        protected TextView name;
        protected TextView username;
        protected ImageView profilePicture;

    }


}