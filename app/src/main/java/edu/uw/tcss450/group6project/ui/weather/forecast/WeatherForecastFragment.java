package edu.uw.tcss450.group6project.ui.weather.forecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherForecastBinding;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;

/**
 * Fragment that displays a 48-hour weather forecast in a Recycler View.
 * @author Anthony
 */
public class WeatherForecastFragment extends Fragment {
    /** List of hourly-forecast data. */
    private List<WeatherDailyData> mForecastData;

    /**
     * Constructor for the fragment.
     * @param forecastData List of hourly forecast data
     */
    public WeatherForecastFragment(List<WeatherDailyData> forecastData) {
        mForecastData = forecastData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentWeatherForecastBinding binding = FragmentWeatherForecastBinding.bind(view);
        RecyclerView rv = binding.listRoot;
        rv.setAdapter(new ForecastRecyclerViewAdapter(mForecastData));
    }
}