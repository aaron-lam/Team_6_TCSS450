package edu.uw.tcss450.group6project.ui.weather.model;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;

/**
 * Class that encapsulates all the weather information retrieved from the web-service
 * Contains Daily Weather for 7 days
 * Contains Hourly Weather for 48-hours
 * Contains Location of the weather forecast.
 * @author Anthony
 */
public class WeatherData {

    /** List of daily weather conditions*/
    private List<WeatherDailyData> mDailyList;
    /** List of 48-hour forecast conditions*/
    private List<WeatherDailyData> mForecastList;
    /** City of the forecast*/
    private String mCity;
    /** State of the forecast*/
    private String mState;
    /** Latitude of the forecast*/
    private double mLatitude;
    /** Longitude of the forecast*/
    private double mLongitude;


    /**
     * Public constructor
     */
    public WeatherData() {
        mDailyList = new ArrayList<>();
        mForecastList = new ArrayList<>();
    }

    /** Retrieves the current weather conditions*/
    public WeatherDailyData getCurrentWeather() {
        return mDailyList.get(0);
    }

    /** Getter for the list of daily weather conditions*/
    public List<WeatherDailyData> getDailyData() {
        return mDailyList;
    }

    /** Getter for the list of 48-hour forecast*/
    public List<WeatherDailyData> getForecastData() {
        return mForecastList;
    }

    /** Checks if the weather data is loaded*/
    public boolean isEmpty() {
        return mDailyList.isEmpty();
    }

    /**
     * Getter for the city
     * @return City of the current weather
     */
    public String getCity() {
        return mCity;
    }

    /**
     * Setter for the city
     * @param city new city
     */
    public void setCity(String city) {
        mCity = city;
    }

    /**
     * Getter for the state
     * @return State (Location) of current weather
     */
    public String getState() {
        return mState;
    }

    /** Setter for the state*/
    public void setState(String state) {
        mState = state;
    }

    /**
     * Getter for the latitude
     * @return Current Weather Latitude
     */
    public double getLatitude() {
        return mLatitude;
    }

    /**
     * Setter for the latitude
     * @param latitude new latitude
     */
    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    /**
     * Getter for the longitude
     * @return Current weather Longitude
     */
    public double getLongitude() {
        return mLongitude;
    }

    /**
     * Setter for the longitude
     * @param longitude new longitude
     */
    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

}
