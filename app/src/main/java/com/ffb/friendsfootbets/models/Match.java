package com.ffb.friendsfootbets.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Match {
    String nameHomeTeam;
    String crestHomeUrl;
    String nameAwayTeam;
    String crestAwayUrl;
    HashMap<User, String> bets;
    Date matchDate;
    //Set to -1 if the score isn't available yet
    int scoreHomeTeam;
    int scoreAwayTeam;

    public Match(String nameHomeTeam, String crestHomeUrl, String nameAwayTeam, String crestAwayUrl, HashMap<User, String> bets, Date matchDate) {
        this.nameHomeTeam = nameHomeTeam;
        this.crestHomeUrl = crestHomeUrl;
        this.nameAwayTeam = nameAwayTeam;
        this.crestAwayUrl = crestAwayUrl;
        this.bets = bets;
        this.matchDate = matchDate;
        this.scoreHomeTeam = -1;
        this.scoreAwayTeam = -1;
    }

    public String getNameHomeTeam() {
        return nameHomeTeam;
    }

    public void setNameHomeTeam(String nameHomeTeam) {
        this.nameHomeTeam = nameHomeTeam;
    }

    public String getCrestHomeUrl() {
        return crestHomeUrl;
    }

    public void setCrestHomeUrl(String crestHomeUrl) {
        this.crestHomeUrl = crestHomeUrl;
    }

    public String getNameAwayTeam() {
        return nameAwayTeam;
    }

    public void setNameAwayTeam(String nameAwayTeam) {
        this.nameAwayTeam = nameAwayTeam;
    }

    public String getCrestAwayUrl() {
        return crestAwayUrl;
    }

    public void setCrestAwayUrl(String crestAwayUrl) {
        this.crestAwayUrl = crestAwayUrl;
    }

    public HashMap<User, String> getBets() {
        return bets;
    }

    public void addBet(User user, String bet) {
        this.bets.put(user, bet);
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getScoreHomeTeam() {
        return scoreHomeTeam;
    }

    public void setScoreHomeTeam(int scoreHomeTeam) {
        this.scoreHomeTeam = scoreHomeTeam;
    }

    public int getScoreAwayTeam() {
        return scoreAwayTeam;
    }

    public void setScoreAwayTeam(int scoreAwayTeam) {
        this.scoreAwayTeam = scoreAwayTeam;
    }
}
