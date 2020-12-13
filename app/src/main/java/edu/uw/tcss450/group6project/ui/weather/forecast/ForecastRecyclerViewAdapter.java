package edu.uw.tcss450.group6project.ui.weather.forecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentWeatherForecastCardBinding;
import edu.uw.tcss450.group6project.ui.weather.model.WeatherDailyData;

/**
 * Class that handles creating and setting the information of the cards in Recycler View.
 * Each card displays information of a 1-hour forecast.
 * @author Anthony
 */
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

    /** List of the hourly forecasts to display in the recycler view. */
    private final List<WeatherDailyData> mForecastData;

    /**
     * Constructor for recycler view adapter to display the hourly forecast.
     * @param forecastData list of forecast data to display.
     */
    public ForecastRecyclerViewAdapter(List<WeatherDailyData> forecastData) {
        this.mForecastData = forecastData;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_forecast_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.setHourlyWeather(mForecastData.get(position));
    }

    @Override
    public int getItemCount() {
        return mForecastData.size();
    }

    /**
     * Class to handle setting contents of cards in the recycler view.
     * @author Anthony
     */
    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherForecastCardBinding binding;
        private WeatherDailyData mHourlyForecast;
        public ForecastViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherForecastCardBinding.bind(view);
        }

        /**
         * Sets the hourly forecast card to display the data of a hourly forecast.
         * @param hourlyForecast hourly forecast to display.
         */
        void setHourlyWeather(final WeatherDailyData hourlyForecast) {
            mHourlyForecast = hourlyForecast;
            binding.forecastTextTemp.setText(((int) Math.round(hourlyForecast.getTemp())) + "°");
            binding.forecastTextHumidity.setText(hourlyForecast.getHumidity() + "%");
            binding.forecastTextWind.setText(((int) Math.round(hourlyForecast.getWindSpeed())) + " mph");
            Map<String, Integer> iconMap = createIconMap();
            binding.forecastWeather.setImageResource(iconMap.get(hourlyForecast.getWeather()));
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
}
