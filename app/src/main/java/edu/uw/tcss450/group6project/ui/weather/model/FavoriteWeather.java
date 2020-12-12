package edu.uw.tcss450.group6project.ui.weather.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteWeather that = (FavoriteWeather) o;
        return Double.compare(that.mLatitude, mLatitude) == 0 &&
                Double.compare(that.mLongitude, mLongitude) == 0 &&
                Objects.equals(mCity, that.mCity) &&
                Objects.equals(mState, that.mState);
    }

}
