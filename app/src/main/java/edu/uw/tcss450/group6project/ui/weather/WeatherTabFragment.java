package edu.uw.tcss450.group6project.ui.weather;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherTabBinding;

/**
 * A fragment to navigate between single day
 * and weekly weather fragments
 */
public class WeatherTabFragment extends Fragment {

    private WeatherTabViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherTabViewModel.class);
        //Hard coded values for sprint 2 testing purposes
        mModel.connectLocation(47.48, -122.21);
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

        FragmentWeatherTabBinding binding = FragmentWeatherTabBinding.bind(getView());
        mModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherDataList -> {
            if(!weatherDataList.isEmpty()) {
                createWeatherTab(view, weatherDataList);
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.weather_top_menu, menu);
        //Add code to search here
    }

    private void createWeatherTab(View view, List<WeatherData> weatherDataList) {

        Map<String, Integer> mIconMap = createIconMap();
        String[] weatherTabText = new String[7];
        int[] weatherTabIcons = new int[7];
        double[] weatherTemp = new double[7];

        for(int i = 0; i < 7; i++) {
            WeatherData data = weatherDataList.get(i);
            weatherTabText[i] = data.getDay();
            weatherTabIcons[i] = mIconMap.get(data.getWeather());
            weatherTemp[i] = data.getTemp();
        }

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeatherPagerAdapter(this, weatherTabIcons, weatherTemp));

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(weatherTabText[position]);
            tab.setIcon(weatherTabIcons[position]);
        }).attach();
    }

    private Map<String, Integer> createIconMap() {

        Map<String, Integer> iconMap = new HashMap<>();
        iconMap.put("Clouds", R.drawable.weather_cloud_24dp);
        iconMap.put("Snow", R.drawable.weather_snow_24dp);
        iconMap.put("Rain", R.drawable.weather_rain_24dp);
        iconMap.put("Clear", R.drawable.weather_sun_24dp);

        return iconMap;
    }

    class WeatherPagerAdapter extends FragmentStateAdapter {

        int[] mIcons;
        double[] mTemps;

        public WeatherPagerAdapter(@NonNull Fragment fragment, int[] icons, double[] temps) {
            super(fragment);
            mIcons = icons;
            mTemps = temps;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new WeatherFragment(mIcons[position], mTemps[position]);
        }

        @Override
        public int getItemCount() {
            return 7;
        }

    }
}