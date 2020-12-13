package edu.uw.tcss450.group6project.ui.weather.model;

import java.util.Objects;

/**
 * Class that encapsulates a single favorite weather location.
 * @author Anthony
 */
public class FavoriteWeather {
    /** City of the favorite location. */
    private String mCity;
    /** State of the favorite location. */
    private String mState;
    /** Latitude of the favorite location. */
    private double mLatitude;
    /** Longitude of the favorite location. */
    private double mLongitude;

    /**
     * Constructor for creating a new favorite weather.
     * @param city favorite city
     * @param state favorite state
     * @param latitude latitude of favorite location
     * @param longitude longitude of favorite location
     */
    public FavoriteWeather(String city, String state, double latitude, double longitude) {
        mCity = city;
        mState = state;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    /**
     * City of the favorite location
     * @return city
     */
    public String getCity() {
        return mCity;
    }

    /**
     * State of the favorite location.
     * @return the state
     */
    public String getState() {
        return mState;
    }

    /**
     * Latitude of the favorite location
     * @return the latitude
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * Longitude of the favorite location
     * @return the longitude
     */
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
