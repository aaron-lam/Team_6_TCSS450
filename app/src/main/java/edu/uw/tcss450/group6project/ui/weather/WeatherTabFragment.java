package edu.uw.tcss450.group6project.ui.weather;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.weather.forecast.WeatherForecastFragment;
import edu.uw.tcss450.group6project.ui.weather.model.FavoriteWeatherViewModel;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherViewModel;

/**
 * A fragment to display all the weather information.
 * Displays 48-hourly forecast
 * Displays 7-day daily forecast.
 * Allows user update via zipcode, map, and favorite locations
 * @author Anthony
 */
public class WeatherTabFragment extends Fragment {

    /** Model for the weather data. */
    private WeatherViewModel mWeatherModel;
    /** Model for the user data. */
    private UserInfoViewModel mUserModel;
    /** Model for the user's favorite locations. */
    private FavoriteWeatherViewModel mFavoriteLocationModel;
    /** Top search action for entering zip codes. */
    private SearchView mSearchView;
    /** Whether or not the current location is a favorite location. */
    private boolean mFavoriteLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mFavoriteLocationModel = new ViewModelProvider(getActivity()).get(FavoriteWeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_weather_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createWeatherTab(view, mWeatherModel.getForecastData(), mWeatherModel.getDailyData());
        mWeatherModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherData -> {
            if(!weatherData.isEmpty()) {
                createWeatherTab(view, weatherData.getForecastData(), weatherData.getDailyData());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_weather_menu, menu);

        //change the icon based on whether or not the location is a favorite
        setFavoriteIcon(menu.findItem(R.id.action_favorite));
        mWeatherModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherData -> {
            setFavoriteIcon(menu.findItem(R.id.action_favorite));
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);
        createSearchView(searchItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Creates the search view for entering zipcodes
     * Creates snackbar display when invalid zipcode is entered
     * @param searchItem Menu item to press to open search view
     */
    private void createSearchView(MenuItem searchItem) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }

        if (mSearchView != null) {
            mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            mSearchView.setQueryHint(getResources().getString(R.string.weather_zip));
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(isZipCode(query)) {
                        mWeatherModel.connectZipCode(query, mUserModel.getJWT());
                        mSearchView.setQuery("", false);
                        mSearchView.setIconified(true);
                    } else {
                        makeSnackbar(R.string.weather_zip_error, Color.RED, Color.WHITE);
                    }
                    mSearchView.clearFocus(); //removes the keyboard
                    return true;
                }
                //Unimplemented method that does nothing
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    /**
     * Checks if the submitted text has the format of a zipcode
     * @param submitText Zipcode submission
     * @return If the submitted text has a valid zipcode format
     */
    private boolean isZipCode(String submitText) {
        Pattern pattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        return pattern.matcher(submitText).matches();
    }

    /**
     * Creates a message display at the bottom of the screen
     * @param stringId String resource to display
     * @param bgColor Background Color of the message popup
     * @param textColor Text Color of the message popup
     */
    private void makeSnackbar(int stringId, int bgColor, int textColor) {
        Snackbar snackbar = Snackbar.make(getView(), stringId, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(bgColor);
        snackbar.setTextColor(textColor);
        snackbar.setAction("Dismiss", click -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_map) {
            Navigation.findNavController(getView()).navigate(WeatherTabFragmentDirections.actionNavigationWeatherToMapsFragment());
        } else if (item.getItemId() == R.id.action_favorite) {
            toggleFavoriteLocation(item);
        } else if(item.getItemId() == R.id.action_bookmark) {
            Navigation.findNavController(getView()).navigate(WeatherTabFragmentDirections.
                    actionNavigationWeatherToWeatherFavoriteLocationFragment());
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggles whether or not the location is a favorite location.
     * Makes requests to the FavoriteLocationViewModel
     * Update icons and text displayed for the menu items
     * @param item Menu item to toggle favorite item
     */
    private void toggleFavoriteLocation(MenuItem item) {
        if(mFavoriteLocation) { //unfavorite the location
            mFavoriteLocationModel.connectDelete(mWeatherModel.getCity(), mWeatherModel.getState(),
                    mWeatherModel.getLatitude(), mWeatherModel.getLongitude(), mUserModel.getJWT());
            item.setIcon(R.drawable.weather_nonfavorite_24dp);
            item.setTitle(R.string.weather_add_favorite);
            makeSnackbar(R.string.weather_unfavorite, Color.BLUE, Color.WHITE);
        } else { //favorite the location
            mFavoriteLocationModel.connectPost(mWeatherModel.getCity(), mWeatherModel.getState(),
                    mWeatherModel.getLatitude(), mWeatherModel.getLongitude(), mUserModel.getJWT());
            item.setIcon(R.drawable.weather_favorite_24dp);
            item.setTitle(R.string.weather_remove_favorite);
            makeSnackbar(R.string.weather_favorite, Color.BLUE, Color.WHITE);
        }
        mFavoriteLocation = !mFavoriteLocation;
    }

    /**
     * If the location is a favorite then set the icon/text on the menu
     * to reflect that
     * @param starMenuItem Menu item to toggle favorites
     */
    private void setFavoriteIcon(MenuItem starMenuItem) {
        if(mFavoriteLocationModel.containsLocation(mWeatherModel.getLatitude(), mWeatherModel.getLongitude())) {
            Log.d("Weather Tab", "Favorited Location");
            mFavoriteLocation = true;
            starMenuItem.setIcon(R.drawable.weather_favorite_24dp);
            starMenuItem.setTitle(R.string.weather_remove_favorite);
        } else {
            Log.d("Weather Tab", "NON Favorited Location");
            mFavoriteLocation = false;
            starMenuItem.setIcon(R.drawable.weather_nonfavorite_24dp);
            starMenuItem.setTitle(R.string.weather_add_favorite);
        }
    }

    /**
     * Creates the tabs that display weather information.
     * @param view View to build the tabs on
     * @param dailyData Data to display on the tabs
     */
    private void createWeatherTab(View view, List<WeatherDailyData> forecastData, List<WeatherDailyData> dailyData) {

        Map<String, Integer> mIconMap = createIconMap();
        String[] weatherTabText = new String[7];
        int[] weatherTabIcons = new int[7];

        for(int i = 0; i < 7; i++) {
            WeatherDailyData data = dailyData.get(i);
            weatherTabText[i] = data.getDay();
            //if the icon doesn't exist default to cloud
            weatherTabIcons[i] = mIconMap.getOrDefault(data.getWeather(), R.drawable.weather_cloud_24dp);
        }

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeatherPagerAdapter(this, weatherTabIcons, forecastData, dailyData));

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position == 0) {
                tab.setText(getResources().getString(R.string.weather_48hour));
                tab.setIcon(R.drawable.weather_calendar_24dp);
            } else {
                tab.setText(weatherTabText[position-1]);
                tab.setIcon(weatherTabIcons[position-1]);
            }
        }).attach();
    }

    /**
     * Creates a mapping of weather conditions to an icon
     * @return Map of weather icons
     */
    private Map<String, Integer> createIconMap() {

        Map<String, Integer> iconMap = new HashMap<>();
        iconMap.put("Clouds", R.drawable.weather_cloud_24dp);
        iconMap.put("Snow", R.drawable.weather_snow_24dp);
        iconMap.put("Rain", R.drawable.weather_rain_24dp);
        iconMap.put("Clear", R.drawable.weather_sun_24dp);
        return iconMap;
    }

    /**
     * Adapter class that handles which day on the tab list to display
     */
    class WeatherPagerAdapter extends FragmentStateAdapter {

        int[] mIcons;
        List<WeatherDailyData> mForecastData;
        List<WeatherDailyData> mDailyData;

        /**
         * Constructor for Weather Adapter.
         * @param fragment fragment to display on. (Weather)
         * @param icons array of icons that represent the weather conditions for the week
         * @param dailyData list of temperatures for the week
         */
        public WeatherPagerAdapter(@NonNull Fragment fragment, int[] icons, List<WeatherDailyData> forecastData,
                                   List<WeatherDailyData> dailyData) {
            super(fragment);
            mIcons = icons;
            mForecastData = forecastData;
            mDailyData = dailyData;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position == 0) {
                return new WeatherForecastFragment(mForecastData);
            }
            return new DailyWeatherFragment(mIcons[position-1], mDailyData.get(position - 1),
                    mWeatherModel.getCity(), mWeatherModel.getState());
        }

        /**
         * Forecast tab and 7 daily tabs
         * @return Number of tabs (8)
         */
        @Override
        public int getItemCount() {
            return 8;
        }
    }
}