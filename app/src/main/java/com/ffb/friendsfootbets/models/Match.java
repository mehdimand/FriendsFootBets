package com.ffb.friendsfootbets.models;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class Match {
    String matchId;
    String nameHomeTeam;
    String crestHomeUrl;
    String nameAwayTeam;
    String crestAwayUrl;
    HashMap<User, String> bets;
    String matchDate;
    String matchHour;
    //Set to -1 if the score isn't available yet
    int scoreHomeTeam;
    int scoreAwayTeam;

    public Match(String matchId, String nameHomeTeam, String nameAwayTeam, String matchDate, String matchHour, int scoreHomeTeam, int scoreAwayTeam) {
        this.matchId = matchId;
        this.nameHomeTeam = nameHomeTeam;
        this.nameAwayTeam = nameAwayTeam;
        this.matchDate = matchDate;
        this.matchHour = matchHour;
        this.scoreHomeTeam = scoreHomeTeam;
        this.scoreAwayTeam = scoreAwayTeam;
        bets = new HashMap<>();
    }

    public Match(String matchId, String nameHomeTeam, String nameAwayTeam, String matchDate, int scoreHomeTeam, int scoreAwayTeam) {
        this.matchId = matchId;
        this.nameHomeTeam = nameHomeTeam;
        this.nameAwayTeam = nameAwayTeam;
        this.matchDate = matchDate;
        this.scoreHomeTeam = scoreHomeTeam;
        this.scoreAwayTeam = scoreAwayTeam;
        bets = new HashMap<>();
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchHour() {
        return matchHour;
    }

    public void setMatchHour(String matchHour) {
        this.matchHour = matchHour;
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

    @Override
    public String toString() {
        return "Match{" +
                "matchId='" + matchId + '\'' +
                ", nameHomeTeam='" + nameHomeTeam + '\'' +
                ", crestHomeUrl='" + crestHomeUrl + '\'' +
                ", nameAwayTeam='" + nameAwayTeam + '\'' +
                ", crestAwayUrl='" + crestAwayUrl + '\'' +
                ", bets=" + bets +
                ", matchDate='" + matchDate + '\'' +
                ", matchHour='" + matchHour + '\'' +
                ", scoreHomeTeam=" + scoreHomeTeam +
                ", scoreAwayTeam=" + scoreAwayTeam +
                '}';
    }
}
