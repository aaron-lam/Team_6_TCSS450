package edu.uw.tcss450.group6project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new WeatherPagerAdapter(this));

        String[] weatherTabText = {"Single Day", "Week View"};
        int[] weatherTabIcons = {R.drawable.weather_calendar_24dp, R.drawable.weather_snow_24dp};

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                tab.setText(weatherTabText[position]);
                tab.setIcon(weatherTabIcons[position]);
            }).attach();

    }
}

class WeatherPagerAdapter extends FragmentStateAdapter {

    public WeatherPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new WeatherFragment();
            default:
                return new WeatherWeekFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}