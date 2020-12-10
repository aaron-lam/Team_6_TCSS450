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

public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

    private final List<WeatherDailyData> mForecastData;

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

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherForecastCardBinding binding;
        private WeatherDailyData mHourlyForecast;
        public ForecastViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherForecastCardBinding.bind(view);
        }

        void setHourlyWeather(final WeatherDailyData hourlyForecast) {
                mHourlyForecast = hourlyForecast;
                binding.forecastTextTemp.setText(((int) Math.round(hourlyForecast.getTemp())) + "°");
                binding.forecastTextHumidity.setText(hourlyForecast.getHumidity() + "%");
                binding.forecastTextWind.setText(((int) Math.round(hourlyForecast.getWindSpeed())) + " mph");
                Map<String, Integer> iconMap = createIconMap();
                binding.forecastWeather.setImageResource(iconMap.get(hourlyForecast.getWeather()));
            }
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
