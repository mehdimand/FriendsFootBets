package com.ffb.friendsfootbets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ffb.friendsfootbets.R;
import com.ffb.friendsfootbets.models.Tournament;

import java.util.ArrayList;

/**
 * Created by Younes and Mehdi on 12/05/2017.
 */
// Adapted from : http://tutos-android-france.com/listview-afficher-une-liste-delements/
public class TournamentAdapter extends ArrayAdapter<Tournament> {

    //Tournaments est la liste des models à afficher
    public TournamentAdapter(Context context, ArrayList<Tournament> tournaments) {
        super(context, 0, tournaments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_tournaments_row,parent, false);
        }

        TournamentViewHolder viewHolder = (TournamentViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TournamentViewHolder();
            //viewHolder.tournamentState = (TextView) convertView.findViewById(R.id.tournamentState);
            viewHolder.tournamentName = (TextView) convertView.findViewById(R.id.tournamentName);
            viewHolder.adminUsername = (TextView) convertView.findViewById(R.id.adminUsername);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tournament> Tournaments
        Tournament tournament = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        //viewHolder.tournamentState.setText(tournament.getState());
        viewHolder.tournamentName.setText(tournament.getTournamentName());
        viewHolder.adminUsername.setText(tournament.getTournamentAdminUsername());

        return convertView;
    }

    private class TournamentViewHolder{
        protected TextView tournamentState;
        protected TextView tournamentName;
        protected TextView adminUsername;

    }


}