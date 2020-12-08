package edu.uw.tcss450.group6project.ui.weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherData {

    private List<WeatherDailyData> mDailyList;
    private List<WeatherDailyData> mForecastList;
    private String mCity;
    private String mState;

    public WeatherData() {
        mDailyList = new ArrayList<>();
        mForecastList = new ArrayList<>();
    }

    public WeatherDailyData getCurrentWeather() {
        return mDailyList.get(0);
    }

    public List<WeatherDailyData> getDailyData() {
        return mDailyList;
    }

    public List<WeatherDailyData> getForecastData() {
        return mForecastList;
    }

    public boolean isEmpty() {
        return mDailyList.isEmpty();
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }
}
