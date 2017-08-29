package com.ffb.friendsfootbets.adapters;


/**
 * Created by mehdimand on 11/05/2017.
 * Class appelée par ChooseCompetitionActivity
 *
 * https://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffb.friendsfootbets.R;

import java.util.ArrayList;

public class CompetitionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> tx1;


    public CompetitionAdapter(Activity context, ArrayList<String> tx1) {
        super(context, R.layout.list_comp, tx1);
        this.context = context;
        this.tx1 = tx1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_comp, null, true);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.namecomp);


        txtTitle1.setText(tx1.get(position));

        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.iconcomp);

        //TODO : find an image more appropriate
        imageView1.setImageResource(R.drawable.logo_ldc);

        return rowView;
    }
}