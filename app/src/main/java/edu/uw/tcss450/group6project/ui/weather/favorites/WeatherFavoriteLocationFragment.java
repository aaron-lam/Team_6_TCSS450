package edu.uw.tcss450.group6project.ui.weather.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentFavoriteLocationCardBinding;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherFavoriteLocationBinding;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherForecastBinding;
import edu.uw.tcss450.group6project.ui.weather.forecast.ForecastRecyclerViewAdapter;
import edu.uw.tcss450.group6project.ui.weather.model.FavoriteWeatherViewModel;

/**
 * Fragment to display the user's favorite locations in a view model.
 */
public class WeatherFavoriteLocationFragment extends Fragment {
    /** Model storing user's favorite locations. */
    private FavoriteWeatherViewModel mFavoriteLocations;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteLocations = new ViewModelProvider(getActivity()).get(FavoriteWeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_favorite_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentWeatherFavoriteLocationBinding binding = FragmentWeatherFavoriteLocationBinding.bind(view);
        RecyclerView rv = binding.listRoot;
        rv.setAdapter(new FavoriteLocationRecyclerViewAdapter(mFavoriteLocations.getFavorites(), this));
    }
}