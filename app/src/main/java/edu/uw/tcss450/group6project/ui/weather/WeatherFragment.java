package edu.uw.tcss450.group6project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherBinding;

/**
 * A fragment for displaying the weather.
 *
 * @author Robert M
 * @version 1.0
 */
public class WeatherFragment extends Fragment {

    private int mTemperature;
    private int mIcon;

    public WeatherFragment(int icon, int temp) {
        mIcon = icon;
        mTemperature = temp;
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
        binding.textWeatherDisplay.setText("Tacoma, WA: " + mTemperature + "°F");
    }
}