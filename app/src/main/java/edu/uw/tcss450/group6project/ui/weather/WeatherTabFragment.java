package edu.uw.tcss450.group6project.ui.weather;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;

import edu.uw.tcss450.group6project.R;

/**
 * A fragment to navigate between single day
 * and weekly weather fragments
 */
public class WeatherTabFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createWeatherTab(view);
    }


    private void createWeatherTab(View view) {

        String[] weatherTabText = {"Today", "Fri 13", "Sat 14", "Sun 15", "Mon 16", "Tue 17", "Wed 18"};
        int[] weatherTabIcons = createIcons();
        int[] weatherTemp = {47, 52, 48 ,59, 35, 20, 102};

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeatherPagerAdapter(this, weatherTabIcons, weatherTemp));

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(weatherTabText[position]);
            tab.setIcon(weatherTabIcons[position]);
        }).attach();
    }

    private int[] createIcons() {
        int sun = R.drawable.weather_sun_24dp;
        int cloud = R.drawable.weather_cloud_24dp;
        int rain = R.drawable.weather_rain_24dp;
        int snow = R.drawable.weather_snow_24dp;
        return new int[] {cloud, cloud, rain, sun, rain, snow, sun};
    }



}

class WeatherPagerAdapter extends FragmentStateAdapter {

    int[] mIcons;
    int[] mTemps;

    public WeatherPagerAdapter(@NonNull Fragment fragment, int[] icons, int[] temps) {
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