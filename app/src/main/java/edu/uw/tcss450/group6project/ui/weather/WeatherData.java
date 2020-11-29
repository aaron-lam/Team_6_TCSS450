package edu.uw.tcss450.group6project.ui.weather;

import java.io.Serializable;

/**
 * Class that encapsulates Weather Data received from the server.
 * @author Anthony Nguyen
 */
public class WeatherData implements Serializable {

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
     * Helper class for building Weather Data
     */
    public static class Builder {
        private final String mDay;
        private final String mWeather;
        private final double mTemp;
        private final int mHumidity;
        private final double mWindSpeed;

        /**
         * Constructs a new Builder
         * @param day day of the data
         * @param weather weather condition for the day
         * @param temp temperature for the day
         */
        public Builder(String day, String weather, double temp, int humidity, double windspeed) {
            mDay = day;
            mWeather = weather;
            mTemp = temp;
            mHumidity = humidity;
            mWindSpeed = windspeed;
        }

        public WeatherData build() {
            return new WeatherData(this);
        }
    }

    /**
     * Constructs a new weather object
     * @param builder
     */
    public WeatherData(final Builder builder) {
        this.mDay = builder.mDay;
        this.mWeather = builder.mWeather;
        this.mTemp = builder.mTemp;
        this.mHumidity = builder.mHumidity;
        this.mWindSpeed = builder.mWindSpeed;
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
     * Retrieves the temperature of the day
     * @return the temperature
     */
    public double getTemp() {
        return mTemp;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public double getWindSpeed() {
        return mHumidity;
    }

}
