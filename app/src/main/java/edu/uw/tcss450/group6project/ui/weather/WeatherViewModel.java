package edu.uw.tcss450.group6project.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import edu.uw.tcss450.group6project.R;

/**
 * View Model class to store data about weather.
 * @author Anthony
 */
public class WeatherViewModel extends AndroidViewModel {

    /** List of weather data retrieved from server */
    private MutableLiveData<WeatherData> mWeatherData;

    /**
     * Constructor for the view model.
     * @param application application using the view model.
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherData = new MutableLiveData<>();
        mWeatherData.setValue(new WeatherData());
    }

    public void connectZipCode(String zipcode) {
        String url = getApplication().getResources().getString(R.string.url_weather_location);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(getApplication().getResources().getString(R.string.keys_json_weather_zip), zipcode);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Requests weather data based on a location.
     * @param latitude latitude of request
     * @param longitude longitude of request
     */
    public void connectLocation(double latitude, double longitude) {
        String url = getApplication().getResources().getString(R.string.url_weather_location);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(getApplication().getResources().getString(R.string.keys_json_weather_lat), Double.toString(latitude));
                headers.put(getApplication().getResources().getString(R.string.keys_json_weather_long), Double.toString(longitude));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Register as an observer to listen to retrieval of weather data.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addWeatherDataListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super WeatherData> observer) {
        mWeatherData.observe(owner, observer);
    }

    /**
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing weather data
     */
    private void handleResult(final JSONObject result) {
        mWeatherData.getValue().getDailyData().clear();
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;

            if (root.has(getString.apply(R.string.keys_json_weather_daily))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_weather_daily));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonBlog = data.getJSONObject(i);
                    WeatherDailyData dailyWeather = new WeatherDailyData(
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_day)),
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_weather)),
                            jsonBlog.getDouble(
                                    getString.apply(
                                            R.string.keys_json_weather_temp)),
                            jsonBlog.getInt(
                                    getString.apply(
                                            R.string.keys_json_weather_humidity)),
                            jsonBlog.getDouble(
                                    getString.apply(
                                            R.string.keys_json_weather_wind)));
                    mWeatherData.getValue().getDailyData().add(dailyWeather);
                }
            } else {
                Log.e("WEATHER MODEL ERROR!", "No daily data array");
            }

            if (root.has(getString.apply(R.string.keys_json_weather_forecast))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_weather_forecast));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonBlog = data.getJSONObject(i);
                    WeatherDailyData dailyWeather = new WeatherDailyData(
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_weather)),
                            jsonBlog.getDouble(
                                    getString.apply(
                                            R.string.keys_json_weather_temp)),
                            jsonBlog.getInt(
                                    getString.apply(
                                            R.string.keys_json_weather_humidity)),
                            jsonBlog.getDouble(
                                    getString.apply(
                                            R.string.keys_json_weather_wind)));
                    mWeatherData.getValue().getForecastData().add(dailyWeather);
                }
            } else {
                Log.e("WEATHER MODEL ERROR!", "No forecast data array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mWeatherData.setValue(mWeatherData.getValue());
        Log.d("Weather View Model", "Model loaded");
    }

    /**
     * Handles errors when making requests to the server.
     * @param error the error message
     */
    private void handleError(VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
}
