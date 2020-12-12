package edu.uw.tcss450.group6project.ui.weather.favorites;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentFavoriteLocationCardBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.weather.model.FavoriteWeather;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherViewModel;

public class FavoriteLocationRecyclerViewAdapter extends
        RecyclerView.Adapter<FavoriteLocationRecyclerViewAdapter.FavoriteLocationViewHolder> {

    private List<FavoriteWeather> mFavoriteLocations;
    private Fragment mFragment;
    private WeatherViewModel mWeatherModel;
    private UserInfoViewModel mUserModel;

    public FavoriteLocationRecyclerViewAdapter(List<FavoriteWeather> favoriteLocations, Fragment fragment) {
        mFavoriteLocations = favoriteLocations;
        mFragment = fragment;
        mWeatherModel = new ViewModelProvider(fragment.getActivity()).get(WeatherViewModel.class);
        mUserModel = new ViewModelProvider(fragment.getActivity()).get(UserInfoViewModel.class);
    }

    @NonNull
    @Override
    public FavoriteLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteLocationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite_location_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteLocationViewHolder holder, int position) {
        holder.setFavoriteLocation(mFavoriteLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return mFavoriteLocations.size();
    }

    public class FavoriteLocationViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentFavoriteLocationCardBinding binding;
        private FavoriteWeather mFavoriteWeather;
        public FavoriteLocationViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFavoriteLocationCardBinding.bind(view);
            binding.buttonSelect.setOnClickListener(click -> {
                mWeatherModel.connectLocation(mFavoriteWeather.getLatitude(),
                        mFavoriteWeather.getLongitude(), mUserModel.getJWT());
                mFragment.getActivity().onBackPressed();
            });
        }

        void setFavoriteLocation(final FavoriteWeather favoriteWeather) {
            mFavoriteWeather = favoriteWeather;
            binding.textCitystate.setText(mFavoriteWeather.getCity() + ", " + mFavoriteWeather.getState());
            binding.textLocation.setText("(" + mFavoriteWeather.getLatitude() + ", " +
                    mFavoriteWeather.getLongitude() + ")");
        }
    }
}
