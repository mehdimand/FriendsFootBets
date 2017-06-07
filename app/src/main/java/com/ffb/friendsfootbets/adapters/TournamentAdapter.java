package com.ffb.friendsfootbets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.Tournament;
import com.ffb.friendsfootbets.models.User;

import java.util.ArrayList;

/**
 * Created by Younes and Mehdi on 12/05/2017.
 */
// Adapted from : http://tutos-android-france.com/listview-afficher-une-liste-delements/
public class TournamentAdapter extends ArrayAdapter<Tournament> {
    private User currentUser;

    //Tournaments est la liste des models à afficher
    public TournamentAdapter(Context context, ArrayList<Tournament> tournaments, User user) {
        super(context, 0, tournaments);
        currentUser = user;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_tournaments_row,parent, false);
        }

        TournamentViewHolder viewHolder = (TournamentViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TournamentViewHolder();
            viewHolder.tournamentState = (ImageView) convertView.findViewById(R.id.tournamentState);
            viewHolder.tournamentName = (TextView) convertView.findViewById(R.id.tournamentName);
            viewHolder.adminUsername = (TextView) convertView.findViewById(R.id.adminUsername);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tournament> Tournaments
        Tournament tournament = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        if(currentUser.getTournamentsInvited().contains(tournament.getTouranmentId())){
            viewHolder.tournamentState.setImageResource(R.drawable.ic_turned_in_not);
        }else{
            viewHolder.tournamentState.setImageResource(R.drawable.ic_turned_in);
        }
        viewHolder.tournamentName.setText(tournament.getTournamentName());
        viewHolder.adminUsername.setText(tournament.getTournamentAdminUsername());

        return convertView;
    }

    private class TournamentViewHolder{
        protected ImageView tournamentState;
        protected TextView tournamentName;
        protected TextView adminUsername;

    }


}