package edu.uw.tcss450.group6project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherForecastBinding;


public class WeatherForecastFragment extends Fragment {

    private List<WeatherDailyData> mForecastData;

    public WeatherForecastFragment(List<WeatherDailyData> forecastData) {
        mForecastData = forecastData;
        Log.d("Forecast Fragment", "LIST SIZE: " + Integer.toString(mForecastData.size()));
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