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
public class WeatherTabViewModel extends AndroidViewModel {

    /** List of weather data retrieved from server */
    private MutableLiveData<List<WeatherData>> mWeatherDataList;

    /**
     * Constructor for the view model.
     * @param application application using the view model.
     */
    public WeatherTabViewModel(@NonNull Application application) {
        super(application);
        mWeatherDataList = new MutableLiveData<>();
        mWeatherDataList.setValue(new ArrayList<>());
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
                                    @NonNull Observer<? super List<WeatherData>> observer) {
        mWeatherDataList.observe(owner, observer);
    }

    /**
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing weather data
     */
    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;

            if (root.has(getString.apply(R.string.keys_json_weather_daily))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_weather_daily));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonBlog = data.getJSONObject(i);
                    WeatherData dailyWeather = new WeatherData.Builder(
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_day)),
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_weather)),
                            jsonBlog.getDouble(
                                    getString.apply(
                                            R.string.keys_json_weather_temp)))
                            .build();
                    if (!mWeatherDataList.getValue().contains(dailyWeather)) {
                        mWeatherDataList.getValue().add(dailyWeather);
                    }
                }
            } else {
                Log.e("ERROR!", "No data array");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mWeatherDataList.setValue(mWeatherDataList.getValue());
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
