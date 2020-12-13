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

/**
 * Class that handles displaying all the favorite locations in a RecyclerView.
 * @author Anthony
 */
public class FavoriteLocationRecyclerViewAdapter extends
        RecyclerView.Adapter<FavoriteLocationRecyclerViewAdapter.FavoriteLocationViewHolder> {

    /** List of favorite locations. */
    private List<FavoriteWeather> mFavoriteLocations;
    /** Fragment this adapter is being used in.*/
    private Fragment mFragment;
    /** Weather model to send requests to and update*/
    private WeatherViewModel mWeatherModel;
    /** User model to access JWT*/
    private UserInfoViewModel mUserModel;

    /**
     * Constructor for the fragment.
     * @param favoriteLocations List of favorite locations to display.
     * @param fragment Fragment adapter is being used in.
     */
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

    /**
     * Class to handle displaying information of a favorite location in a card.
     * @author Anthony
     */
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
            String latitude = String.format("%.2f", mFavoriteWeather.getLatitude());
            String longitude = String.format("%.2f", mFavoriteWeather.getLongitude());

            binding.textLocation.setText("(" + latitude + ", " + longitude + ")");
        }
    }
}
