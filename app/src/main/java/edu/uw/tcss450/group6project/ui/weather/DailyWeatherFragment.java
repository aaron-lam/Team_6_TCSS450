package edu.uw.tcss450.group6project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;

/**
 * A fragment for displaying the daily weather.
 *
 * @author Robert M
 * @author Anthony N
 * @version 1.0
 */
public class DailyWeatherFragment extends Fragment {

    /** Weather icon to display*/
    private int mIcon;
    /** Temperature (F)*/
    private double mTemperature;
    /** Humidity (%)*/
    private int mHumidity;
    /** Wind Speed (mph)*/
    private double mWindSpeed;
    /** City of the weather*/
    private String mCity;
    /** State of the weather*/
    private String mState;

    /**
     * Constructor for weather fragment.
     * @param icon icon for the current weather conditions.
     * @param data weather data for the day.
     */
    public DailyWeatherFragment(int icon, WeatherDailyData data, String city, String state) {
        mIcon = icon;
        mTemperature = data.getTemp();
        mHumidity = data.getHumidity();
        mWindSpeed = data.getWindSpeed();
        mCity = city;
        mState = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());
        binding.weatherCurrentWeather.setImageResource(mIcon);
        binding.textLocation.setText(mCity + ", " + mState);
        binding.textTemperature.setText(getResources().getString(R.string.weather_temperature) +
                ": " + ((int) Math.round(mTemperature)) + "°F");
        binding.textHumidity.setText(getResources().getString(R.string.weather_humidity) +
                ": " + mHumidity + "%");
        binding.textWindspeed.setText(getResources().getString(R.string.weather_windspeed) +
                ": " + + ((int) Math.round(mWindSpeed)) + " mph");
    }
}