package edu.uw.tcss450.group6project.ui.weather.model;

public class FavoriteWeather {

    private String mCity;
    private String mState;
    private double mLatitude;
    private double mLongitude;

    public FavoriteWeather(String city, String state, double latitude, double longitude) {
        mCity = city;
        mState = state;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
