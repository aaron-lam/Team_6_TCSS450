package edu.uw.tcss450.group6project.ui.weather.model;

import java.io.Serializable;

/**
 * Class that encapsulates Weather Data received from the server.
 * @author Anthony Nguyen
 */
public class WeatherDailyData implements Serializable {

    /** The day of the weather recording. */
    private final String mDay;
    /** The weather conditions of the day. */
    private final String mWeather;
    /** The temperature of the day. */
    private final double mTemp;
    /** The humidity of the day. */
    private final int mHumidity;
    /** The wind speed of the day. */
    private final double mWindSpeed;

    /**
     * Constructor for creating daily data.
      * @param day Day of the forecast
     * @param weather Weather conditions for the day
     * @param temp Temperature for the day (F)
     * @param humidity Humidity for the day (%)
     * @param windspeed Wind speed for the day (mph)
     */
    public WeatherDailyData(String day, String weather, double temp, int humidity, double windspeed) {
        mDay = day;
        mWeather = weather;
        mTemp = temp;
        mHumidity = humidity;
        mWindSpeed = windspeed;
    }

    /**
     * Constructor for creating hourly data.
     * @param weather weather conditions for the hour
     * @param temp temperature for the hour (F)
     * @param humidity humidity for the hour (%)
     * @param windspeed windpseed for the hour (mph)
     */
    public WeatherDailyData(String weather, double temp, int humidity, double windspeed) {
        mDay = "Forecast";
        mWeather = weather;
        mTemp = temp;
        mHumidity = humidity;
        mWindSpeed = windspeed;
    }

    /**
     * Retrieves the day of the weather data
     * @return The day of the week and date
     */
    public String getDay() {
        return mDay;
    }

    /**
     * Retrieves the weather conditions
     * @return weather condition
     */
    public String getWeather() {
        return mWeather;
    }

    /**
     * Retrieves the temperature of the day.
     * @return the temperature (F)
     */
    public double getTemp() {
        return mTemp;
    }

    /**
     * Retrieves the humidity for the day.
     * @return the humidity (%)
     */
    public int getHumidity() {
        return mHumidity;
    }

    /**
     * Retrieves the windspeed for the day.
     * @return windspeed (mph)
     */
    public double getWindSpeed() {
        return mWindSpeed;
    }

}
