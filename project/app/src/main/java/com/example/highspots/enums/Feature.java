package com.example.highspots.enums;

import androidx.annotation.NonNull;

public enum Feature {

    // LOCATION BASED
    MOUNTAIN("Mountain"),
    LAKE("Lake"),
    URBAN("Urban"),
    FOREST("Forest"),
    VALLEY("Valley"),
    SEA("Sea"),
    RIVER("River"),
    HILL("Hill"),
    PARK("Park"),
    BEACH("Beach"),
    DESERT("Desert"),
    COUNTRYSIDE("Countryside"),
    WATERFALL("Waterfall"),

    // NATURE RELATED
    GRASS("Grass"),
    SAND("Sand"),
    WILDLIFE("Wildlife"),
    STARS("Stars"),
    SUNSET("Sunset"),
    SUNRISE("Sunrise"),
    MOON("Moon"),

    // THING BASED
    BENCH("Bench"),
    GAZEBO("Gazebo"),
    TABLE("Table"),
    TRASHCANS("Trash Cans"),
    PARKING("Parking"),

    // ADJECTIVE BASED
    PEACEFUL("Peaceful"),
    QUIET("Quiet"),
    RELAXING("Relaxing"),
    SECLUDED("Secluded"),
    ROMANTIC("Romantic"),
    ADVENTUROUS("Adventurous"),
    SCARY("Scary"),
    PARANOIA("Paranoia");

    final String feature;

    Feature(String feature) {
        this.feature = feature;
    }

    @NonNull
    @Override
    public String toString() {
        return this.feature;
    }

}
