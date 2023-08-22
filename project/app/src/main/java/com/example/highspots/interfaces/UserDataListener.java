package com.example.highspots.interfaces;

public interface UserDataListener {

    /**
     * This method is responsible for retrieving user data from UserDataRepository.
     */
    default public void retrieveUserData() {}

    /**
     * This method is responsible for retrieving the found spots from UserDataRepository.
     */
    default public void retrieveFoundSpotsData() {}

}
