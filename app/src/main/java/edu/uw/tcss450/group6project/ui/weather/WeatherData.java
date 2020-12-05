package edu.uw.tcss450.group6project.ui.weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherData {

    private List<WeatherDailyData> dailyList;
    private List<WeatherDailyData> forecastList;

    public WeatherData() {
        dailyList = new ArrayList<>();
        forecastList = new ArrayList<>();
    }

    public WeatherDailyData getCurrentWeather() {
        return dailyList.get(0);
    }

    public List<WeatherDailyData> getDailyData() {
        return dailyList;
    }

    public List<WeatherDailyData> getForecastData() {
        return forecastList;
    }

    public boolean isEmpty() {
        return dailyList.isEmpty();
    }


}
