package com.ffb.friendsfootbets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Controller {
    /**
     * A map of all the tournaments that are taking place. The key is the id of the tournament and the value is an instance of the
     * Tournament class.
     */
    private HashMap<Integer, Tournament> tournamentsMap;
    private HashMap<Integer, Match> matchesMap;
    private HashMap<String, User> usersMap;
    private User currentUser;
    private Tournament currentTournament;
    private Match currentMatch;
    private Date lastUpdate;

    public Controller(HashMap<Integer, Tournament> tournamentsMap, User currentUser) {
        this.tournamentsMap = tournamentsMap;
        this.matchesMap = matchesMap;
        this.currentUser = currentUser;
    }

    public HashMap<Integer, Tournament> getTournamentsMap() {
        return tournamentsMap;
    }

    public void addTournament(Integer id, Tournament tournament) {
        this.tournamentsMap.put(id, tournament);
    }

    public HashMap<Integer, Match> getMatchesMap() {
        return matchesMap;
    }

    public void addMatch(Integer id, Match match) {
        this.matchesMap.put(id, match);
    }

    public HashMap<String, User> getUsersMap() {
        return usersMap;
    }

    public void addUser(String username, User user) {
        this.usersMap.put(username, user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Tournament getCurrentTournament() {
        return currentTournament;
    }

    public void setCurrentTournament(Tournament currentTournament) {
        this.currentTournament = currentTournament;
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    // TODO
    public ArrayList<Tournament> getTournamentsForUser(User user){

        return null;
    }

    // TODO
    public ArrayList<Tournament> getInvitationsForUser(User user){
        return null;
    }

    // TODO
    public ArrayList<Match> getNextGames(int numberOfGames){
        return null;
    }

    // TODO
    public void updateMatchesDatabase(){

    }

}
