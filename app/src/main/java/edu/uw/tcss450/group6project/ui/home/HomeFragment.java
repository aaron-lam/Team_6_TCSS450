package edu.uw.tcss450.group6project.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.chat.ChatRoomViewModel;
import edu.uw.tcss450.group6project.ui.contacts.requests_tab.ContactRequestTabViewModel;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherViewModel;

/**
 * A fragment for displaying the home landing page
 *
 * @author Anthony W
 * @version 1.0
 */
public class HomeFragment extends Fragment {

    /** Model storing user information. */
    private UserInfoViewModel mUserModel;
    /** Model storing weather information. */
    private WeatherViewModel mWeatherModel;
    /** Model storing info about chat rooms the user is in. */
    private ChatRoomViewModel mChatRoomModel;
    /** Model storing info about contact requests the user has received. */
    private ContactRequestTabViewModel mContactRequestModel;

    /** Bottom Navigation menu*/
    private BottomNavigationView mNavView;

    /** Flag to check if weather model is loaded*/
    private boolean weatherLoaded = false;
    /** Flag to check if chat model is loaded*/
    private boolean chatLoaded = false;
    /** Flag to check if contact request model is loaded*/
    private boolean contactRequestsLoaded = false;

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater);
        mNavView = getActivity().findViewById(R.id.nav_view);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);

        mChatRoomModel = provider.get(ChatRoomViewModel.class);
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());

        mWeatherModel = provider.get(WeatherViewModel.class);

        mContactRequestModel = provider.get(ContactRequestTabViewModel.class);
        mContactRequestModel.connectGet(mUserModel.getJWT());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentHomeBinding.bind(view);

        mChatRoomModel.addRoomListObserver(getViewLifecycleOwner(), roomList -> {
            chatLoaded = true;
            checkLoaded();
        });

        mWeatherModel.addWeatherDataListObserver(getViewLifecycleOwner(), weatherData -> {
            if(!weatherData.isEmpty()) {
                WeatherDailyData currentWeather = weatherData.getCurrentWeather();
                binding.textWeather.setText(weatherData.getCity() + ", " + weatherData.getState()
                + ": " + (int) Math.round(currentWeather.getTemp()) + "°F");
                binding.imageWeather.setImageResource(createIconMap().getOrDefault(currentWeather.getWeather(), R.drawable.weather_cloud_24dp));
                weatherLoaded = true;
                checkLoaded();
            }
        });
        mContactRequestModel.addContactRequestListObserver(getViewLifecycleOwner(), contactList -> {
            if(contactList.isEmpty()) {
                binding.textNewContacts.setText("No new contact requests");
            } else {
                binding.textNewContacts.setText("You have " + contactList.size() + " contact requests!");
            }
            contactRequestsLoaded = true;
            checkLoaded();
        });
        binding.textViewHelloMessage.setText("Good " + getTimeGreeting() + ", " + mUserModel.getUsername());

        mNavView.setVisibility(View.INVISIBLE);
    }

    /**
     * Helper method to check if all the view models are loaded.
     * Removes loading screen and displays bottom navigation when all models are loaded.
     */
    private void checkLoaded() {
        if(weatherLoaded &&  contactRequestsLoaded && chatLoaded) {
            binding.layoutWait.setVisibility(View.GONE);
            mNavView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Return time greeting based on current time
     * @return time greeting
     */
    private String getTimeGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "morning";
        } else if (timeOfDay < 16) {
            return "afternoon";
        } else if (timeOfDay < 21) {
            return "evening";
        }
        return "night";
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