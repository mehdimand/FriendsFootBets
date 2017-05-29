package com.ffb.friendsfootbets;


/**
 * Created by mehdimand on 11/05/2017.
 * Class appelée par AddMatchToTournament
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

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] tx1;
    private final Integer[] imageId1;


    public CustomList(Activity context, String[] tx1, Integer[] imageId1) {
        super(context, R.layout.listforcomp, tx1);
        this.context = context;
        this.tx1 = tx1;
        this.imageId1 = imageId1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listforcomp, null, true);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.namecomp);


        txtTitle1.setText(tx1[position]);

        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.iconcomp);

        imageView1.setImageResource(imageId1[position]);

        return rowView;
    }
}