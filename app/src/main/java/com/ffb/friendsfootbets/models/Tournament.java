package com.ffb.friendsfootbets.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Tournament implements Serializable {
    /* The 3 possible states for a tournament, the getState(String username) method returns the state that
     * correspond to the username.
     */
    public final static int MATCHES_FINISHED = 1;
    public final static int BETTING_FINISHED = 2;
    public final static int BETTING_ONGOING = 3;
    public final static int INVITED = 4;

    String touranmentId;
    String tournamentName;
    String tournamentAdminUsername;
    ArrayList<String> matchArray;
    ArrayList<String> invitedUserArray;
    int state;
    // points is also used to get the list of the users
    HashMap<String, Integer> points;

    public Tournament(String touranmentId) {
        this.touranmentId = touranmentId;
    }

    public Tournament(String tournamentName, String tournamentAdminUsername) {
        this.tournamentName = tournamentName;
        this.tournamentAdminUsername = tournamentAdminUsername;
        this.matchArray = new ArrayList<>();
        this.invitedUserArray = new ArrayList<>();
        this.points = new HashMap<>();
        addUser(tournamentAdminUsername);
    }

    public String getTouranmentId() {
        return touranmentId;
    }

    public void setTouranmentId(String touranmentId) {
        this.touranmentId = touranmentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentAdminUsername() {
        return tournamentAdminUsername;
    }

    public void setTournamentAdminUsername(String tournamentAdminUsername) {
        this.tournamentAdminUsername = tournamentAdminUsername;
    }

    public ArrayList<String> getMatchArray() {
        return matchArray;
    }

    public void setMatchArray(ArrayList<String> matchArray) {
        this.matchArray = matchArray;
    }

    public ArrayList<String> getUserArray() {
        return new ArrayList<>(points.keySet());
    }

    public void addUser(String username) {
        this.points.put(username, 0);
    }

    public ArrayList<String> getInvitedUserArray() {
        return invitedUserArray;
    }

    public void setInvitedUserArray(ArrayList<String> invitedUserArray) {
        this.invitedUserArray = invitedUserArray;
    }

    public void addInvitedUser(String username) {
        this.invitedUserArray.add(username);
    }

    public HashMap<String, Integer> getPoints() {
        return points;
    }

    public void setPoints(HashMap<String, Integer> points) {
        this.points = points;
    }

    /*
        * Adds points for one username. If the username isn't in the map, he will be added
        * */
    public void addPointsForUser(String username, Integer additionalPoints) {
        Integer currentPoints = this.points.get(username);
        if (currentPoints != null) {
            this.points.put(username, currentPoints + additionalPoints);
        } else {
            this.points.put(username, additionalPoints);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ArrayList<String> sortUsersByPoints(){
        ArrayList<String> usernameList = new ArrayList<>(points.keySet());
        Collections.sort(usernameList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return points.get(o1).compareTo(points.get(o2));
            }
        });
        return usernameList;
    }

    public HashMap<Integer, Match> getMatchesFromAPI(){

        return null;
    }

    // Method that tries to update the matches that have started but for which the scores aren't avilable yet
    public void updateMatches(){

    } // Method that tries to update the matches that have started but for which the scores aren't avilable yet
    public void calculatePoints(){

    }

    @Override
    public String toString() {
        return "Tournament{" +
                "touranmentId='" + touranmentId + '\'' +
                ", tournamentName='" + tournamentName + '\'' +
                ", tournamentAdminUsername='" + tournamentAdminUsername + '\'' +
                ", matchArray=" + matchArray +
                ", invitedUserArray=" + invitedUserArray +
                ", state=" + state +
                ", points=" + points +
                '}';
    }

    public void resetPoints() {
        for (String username : points.keySet()){
            points.put(username, 0);
        }
    }
}
