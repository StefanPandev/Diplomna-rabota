package com.example.highspots.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String dbID;
    private String nickName;
    private String email;
    private String role = "User";
    private Map<String, String> foundSpots = new HashMap<>();
    private Map<String, String> visitedSpots = new HashMap<>();
    private Map<String, String> ratedSpots = new HashMap<>();

    public User() { }

    public User(String dbID, String nickName, String email) {
        this.dbID = dbID;
        this.nickName = nickName;
        this.email = email;
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getFoundSpots() {
        return foundSpots;
    }

    public Map<String, String> getVisitedSpots() {
        return visitedSpots;
    }

    public void addFoundSpot(String foundSpot) {
        this.foundSpots.put(foundSpot, foundSpot);
    }

    public void removeFoundSpot(String foundSpot) {
        this.foundSpots.remove(foundSpot);
    }

    public void addVisitedSpot(String visitedSpot) {
        this.visitedSpots.put(visitedSpot, visitedSpot);
    }

    public void removeVisitedSpot(String visitedSpot) {
        this.visitedSpots.remove(visitedSpot);
    }

    public Map<String, String> getRatedSpots() {
        return ratedSpots;
    }

    public void addRatedSpot(String newlyRatedSpot) {
        this.ratedSpots.put(newlyRatedSpot, newlyRatedSpot);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFoundSpots(Map<String, String> foundSpots) {
        this.foundSpots = foundSpots;
    }

    public void setVisitedSpots(Map<String, String> visitedSpots) {
        this.visitedSpots = visitedSpots;
    }

    public void setRatedSpots(Map<String, String> ratedSpots) {
        this.ratedSpots = ratedSpots;
    }
}
