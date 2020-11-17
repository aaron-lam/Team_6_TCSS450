package edu.uw.tcss450.group6project.ui.weather;

import java.io.Serializable;

/**
 * Class that encapsulates Weather Data received from the server.
 */
public class WeatherData implements Serializable {

    /** The day of the weather recording. */
    private final String mDay;
    /** The weather conditions of the day. */
    private final String mWeather;
    /** The temperature of the day. */
    private final double mTemp;

    /**
     * Helper class for building Weather Data
     */
    public static class Builder {
        private final String mDay;
        private final String mWeather;
        private final double mTemp;

        /**
         * Constructs a new Builder
         * @param day day of the data
         * @param weather weather condition for the day
         * @param temp temperature for the day
         */
        public Builder(String day, String weather, double temp) {
            mDay = day;
            mWeather = weather;
            mTemp = temp;
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
}
