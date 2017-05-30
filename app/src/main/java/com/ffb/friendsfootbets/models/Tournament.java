package com.ffb.friendsfootbets.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Tournament implements Serializable {
    /* The 3 possible states for a tournament, the getState(User user) method returns the state that
     * correspond to the user.
     */
    public final static int MATCHES_FINISHED = 1;
    public final static int BETTING_FINISHED = 2;
    public final static int BETTING_ONGOING = 3;
    public final static int INVITED = 4;

    String touranmentId;
    String tournamentName;
    String tournamentAdminUsername;
    ArrayList<String> matchArray;
    ArrayList<User> userArray;
    ArrayList<User> invitedUserArray;
    int state;
    HashMap<User, Integer> points;

    public Tournament(String touranmentId) {
        this.touranmentId = touranmentId;
    }

    public Tournament(String tournamentName, String tournamentAdminUsername) {
        this.tournamentName = tournamentName;
        this.tournamentAdminUsername = tournamentAdminUsername;
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

    public ArrayList<User> getUserArray() {
        return userArray;
    }

    public void setUserArray(ArrayList<User> userArray) {
        this.userArray = userArray;
    }

    public void addUser(User user) {
        this.userArray.add(user);
    }

    public ArrayList<User> getInvitedUserArray() {
        return invitedUserArray;
    }

    public void setInvitedUserArray(ArrayList<User> invitedUserArray) {
        this.invitedUserArray = invitedUserArray;
    }

    public void addInvitedUser(User user) {
        this.invitedUserArray.add(user);
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
