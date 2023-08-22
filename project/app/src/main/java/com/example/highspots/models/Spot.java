package com.example.highspots.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spot implements Serializable {

    private Map<String, String> features = new HashMap<>();
    private String location;
    private String dbID;
    private double rating;
    private int numberOfRatings;
    private Map<String, String> visitors = new HashMap<>();
    private String imageName;
    private String creatorID;
    private Map<String, List<String>> comments = new HashMap<>();

    public Spot() { }

    public Spot(Map<String, String> features, String location, String dbID, double rating, int numberOfRatings, Map<String, String> visitors, String creatorID, String imageName) {
        this.features = features;
        this.location = location;
        this.dbID = dbID;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
        this.visitors = visitors;
        this.creatorID = creatorID;
        this.imageName = imageName;
    }

    public Map<String, String> getFeatures() {
        return features;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addFeature(String feature) {
        this.features.put(feature, feature);
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public Map<String, String> getVisitors() {
        return visitors;
    }

    public void addVisitor(String visitorID) {
        this.visitors.put(visitorID, visitorID);
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }

    public void setVisitors(Map<String, String> visitors) {
        this.visitors = visitors;
    }

    public Map<String, List<String>> getComments() {
        return comments;
    }

    public void setComments(Map<String, List<String>> comments) {
        this.comments = comments;
    }

    public void addNewRating(double rating) {
        double newRating = ((this.rating * numberOfRatings) + rating) / (numberOfRatings + 1);
        this.numberOfRatings++;
        this.rating = newRating;
    }
}
