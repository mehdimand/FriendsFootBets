package com.ffb.friendsfootbets;

import android.os.AsyncTask;
import android.util.Log;

import com.ffb.friendsfootbets.models.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by laaby on 29/08/2017.
 */

public class LoadCompetitions extends AsyncTask<Integer, Integer, HashMap<String, String>>{

    private static final String COMPETITIONS_URL = "http://api.football-data.org/v1/competitions/?season=";

    /* Key is the id of the competition, value is the name of the competition
     */
    public HashMap<String, String> competitionsMap;

    private LoadCompetitionsListener listener;


    public LoadCompetitions(){
        this.competitionsMap = new HashMap<>();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoadCompetitionsListener(LoadCompetitionsListener listener) {
        this.listener = listener;
    }

    /*
     * We implement a listener in order to get back to the initial activity after the data from the
     * database is loaded.
     */
    public interface LoadCompetitionsListener {
        // This trigger will be used every time we need to get a unique user
        public void onLoadedCompetitions(HashMap<String, String> competitionsMap);
    }


    /**
     *
     * @param seasonYear : the year in which the season started : for the 2017/2018 it is 2017
     * @param numberToDisplay : number of competitions to load
     */
    public void loadCompetitions(Integer seasonYear, Integer numberToDisplay){
        competitionsMap = new HashMap<>();
        this.execute(new Integer[]{seasonYear, numberToDisplay});

    }

    @Override
    protected HashMap<String, String> doInBackground(Integer... integers) {
        Integer seasonYear = integers[0];
        Integer numberToDisplay = integers[1];
        String requestURL = COMPETITIONS_URL+seasonYear.toString();

        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(requestURL);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                // Getting JSON Array node
                JSONArray competitions = new JSONArray(jsonStr);
                int max;
                if (numberToDisplay < 0){
                    max = competitions.length();
                }else {
                    max = Math.min(competitions.length(), numberToDisplay);
                }
                // looping through All the table
                for (int i = 0; i < max; i++) {
                    JSONObject f = competitions.getJSONObject(i);
                    JSONObject links = f.getJSONObject("_links");

                    String id = f.getString("id");
                    String name = f.getString("caption");

                    competitionsMap.put(id, name);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");

        }

        return competitionsMap;
    }

    public void onPostExecute(HashMap<String, String> competitionsMap){
        if (listener != null){
            listener.onLoadedCompetitions(competitionsMap);
        }
    }
}
