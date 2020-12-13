package edu.uw.tcss450.group6project.ui.weather.forecast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;

/**
 * Fragment that displays an 1-hour forecast.
 * @author Anthony
 */
public class WeatherForecastCardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_forecast_card, container, false);
    }
}