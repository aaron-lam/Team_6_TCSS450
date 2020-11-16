package edu.uw.tcss450.group6project.ui.weather;

import java.io.Serializable;

public class WeatherData implements Serializable {

    private final String mDay;
    private final String mWeather;
    private final double mTemp;

    /**
     * Helper class for building Weather Data
     */
    public static class Builder {
        private final String mDay;
        private final String mWeather;
        private final double mTemp;

        /**
         * Constructs a new Buider
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

    public WeatherData(final Builder builder) {
        this.mDay = builder.mDay;
        this.mWeather = builder.mWeather;
        this.mTemp = builder.mTemp;
    }

    public String getDay() {
        return mDay;
    }
    public String getWeather() {
        return mWeather;
    }
    public double getTemp() {
        return mTemp;
    }
}
