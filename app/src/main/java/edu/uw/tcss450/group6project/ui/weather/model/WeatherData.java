package edu.uw.tcss450.group6project.ui.weather.model;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;

public class WeatherData {

    /** List of daily weather conditions*/
    private List<WeatherDailyData> mDailyList;
    /** List of 48-hour forecast conditions*/
    private List<WeatherDailyData> mForecastList;
    /** City of the forecast*/
    private String mCity;
    /** State of the forecast*/
    private String mState;

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

    /** Getter for the city of weather conditions*/
    public String getCity() {
        return mCity;
    }

    /** Setter for the city */
    public void setCity(String city) {
        mCity = city;
    }

    /** Getter for the state*/
    public String getState() {
        return mState;
    }

    /** Setter for the state*/
    public void setState(String state) {
        mState = state;
    }
}
