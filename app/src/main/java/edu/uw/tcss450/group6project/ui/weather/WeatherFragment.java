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

/**
 * A fragment for displaying the weather.
 *
 * @author Robert M
 * @author Anthony N
 * @version 1.0
 */
public class WeatherFragment extends Fragment {

    private int mIcon;
    private double mTemperature;
    private int mHumidity;
    private double mWindSpeed;

    /**
     * Constructor for weather fragment.
     * @param icon icon for the current weather conditions.
     * @param data weather data for the day.
     */
    public WeatherFragment(int icon, WeatherDailyData data) {
        mIcon = icon;
        mTemperature = data.getTemp();
        mHumidity = data.getHumidity();
        mWindSpeed = data.getWindSpeed();
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
        binding.textLocation.setText("Tacoma, WA");
        binding.textTemperature.setText("Temperature: " + ((int) Math.round(mTemperature)) + "°F");
        binding.textHumidity.setText("Humidity: " + mHumidity + "%");
        binding.textWindspeed.setText("Wind Speed: " + ((int) Math.round(mWindSpeed)) + " mph");
    }
}