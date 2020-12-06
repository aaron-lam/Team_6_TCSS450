package edu.uw.tcss450.group6project.ui.weather;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherTabBinding;
import edu.uw.tcss450.group6project.model.LocationViewModel;

/**
 * A fragment to navigate between single day
 * and weekly weather fragments
 */
public class WeatherTabFragment extends Fragment {

    /** Model for the weather data*/
    private WeatherViewModel mWeatherModel;

    private LocationViewModel mLocationViewModel;

    private SearchView mSearchView;

    private SearchView.OnQueryTextListener mSearchListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mLocationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        //Hard coded values for sprint 2 testing purposes
        Log.d("Weather Tab Lat", Double.toString(mLocationViewModel.getLatitude()));
        Log.d("Weather Tab Long", Double.toString(mLocationViewModel.getLongitude()));
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
        FragmentWeatherTabBinding binding = FragmentWeatherTabBinding.bind(getView());
        mWeatherModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherData -> {
            if(!weatherData.isEmpty()) {
                createWeatherTab(view, weatherData.getForecastData(), weatherData.getDailyData());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_weather_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }

        if (mSearchView != null) {
            mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            mSearchListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    Log.i("onQueryTextSubmit", query);
                    boolean validinput = isZipCode(query);
                    if(validinput) {
                        Log.i("Zip Code Query", "Valid");
                        mWeatherModel.connectZipCode(query);

                    } else {
                        //Create an error
                        Log.i("Zip Code Query", "Inavlid");
                    }
                    mSearchView.clearFocus();
                    return true;
                }

                //Unimplemented method that does nothing
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            };
            mSearchView.setOnQueryTextListener(mSearchListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean isZipCode(String submitText) {
        Pattern pattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        return pattern.matcher(submitText).matches();
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
        double[] weatherTemp = new double[7];

        for(int i = 0; i < 7; i++) {
            WeatherDailyData data = dailyData.get(i);
            weatherTabText[i] = data.getDay();
            weatherTabIcons[i] = mIconMap.get(data.getWeather());
        }

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeatherPagerAdapter(this, weatherTabIcons, forecastData, dailyData));

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position == 0) {
                tab.setText("24-hour");
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
            return new WeatherFragment(mIcons[position-1], mDailyData.get(position - 1));
        }

        @Override
        public int getItemCount() {
            return 8;
        }
    }
}