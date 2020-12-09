package edu.uw.tcss450.group6project.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.ViewModelProvider;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.chat.ChatRoom;
import edu.uw.tcss450.group6project.ui.chat.ChatGenerator;
import edu.uw.tcss450.group6project.ui.chat.ChatRoomViewModel;
import edu.uw.tcss450.group6project.ui.weather.WeatherDailyData;
import edu.uw.tcss450.group6project.ui.weather.WeatherViewModel;

/**
 * A fragment for displaying the home landing page
 *
 * @author Anthony W
 * @version 1.0
 */
public class HomeFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private WeatherViewModel mWeatherModel;
    private ChatRoomViewModel mChatRoomModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = provider.get(ChatRoomViewModel.class);
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());
        mWeatherModel = provider.get(WeatherViewModel.class);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentHomeBinding binding = FragmentHomeBinding.bind(view);
        mWeatherModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherData -> {
            if(!weatherData.isEmpty()) {
                WeatherDailyData currentWeather = weatherData.getCurrentWeather();
                binding.textWeather.setText(weatherData.getCity() + ", " + weatherData.getState()
                + ": " + (int) Math.round(currentWeather.getTemp()) + "°F");
                binding.imageWeather.setImageResource(createIconMap().get(currentWeather.getWeather()));
            }
        });
        //binding.textWelcome.setText("Welcome Back " + mUserModel.getUsername());

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


}