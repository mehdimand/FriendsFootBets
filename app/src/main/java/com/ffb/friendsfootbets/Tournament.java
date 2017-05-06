package com.ffb.friendsfootbets;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Tournament {
    /* The 3 possible states for a tournament, the getState(User user) method returns the state that
     * correspond to the user.
     */
    final static int MATCHES_FINISHED = 1;
    final static int BETTING_FINISHED = 2;
    final static int BETTING_ONGOING = 3;

    String tournamentName;
    User tournamentAdmin;
    ArrayList<Integer> matchArray;
    ArrayList<User> userArray;
    HashMap<User, Integer> points;

    public Tournament(String tournamentName, User tournamentAdmin) {
        this.tournamentName = tournamentName;
        this.tournamentAdmin = tournamentAdmin;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public User getTournamentAdmin() {
        return tournamentAdmin;
    }

    public void setTournamentAdmin(User tournamentAdmin) {
        this.tournamentAdmin = tournamentAdmin;
    }

    public ArrayList<Integer> getMatchArray() {
        return matchArray;
    }

    public void setMatchArray(ArrayList<Integer> matchArray) {
        this.matchArray = matchArray;
    }

    public ArrayList<User> getUserArray() {
        return userArray;
    }

    public void setUserArray(ArrayList<User> userArray) {
        this.userArray = userArray;
    }

    public void addUser(User user) {
        this.userArray.add(user);
    }

    public HashMap<User, Integer> getPoints() {
        return points;
    }

    /*
    * Adds points for one user. If the user isn't in the map, he will be added
    * */
    public void addPointsForUser(User user, Integer additionalPoints) {
        Integer currentPoints = this.points.get(user);
        if (currentPoints != null) {
            this.points.put(user, currentPoints + additionalPoints);
        } else {
            this.points.put(user, additionalPoints);
        }
    }

    //TODO
    public int getState(User user){
        return BETTING_ONGOING;
    }

    //TODO tri sur place de userArray
    public void sortUsersByPoints(){

    }

    public HashMap<Integer, Match> getMatchesFromAPI(){

        return null;
    }

    // Method that tries to update the matches that have started but for which the scores aren't avilable yet
    public void updateMatches(){

    } // Method that tries to update the matches that have started but for which the scores aren't avilable yet
    public void calculatePoints(){

    }

}
