package edu.uw.tcss450.group6project.ui.weather.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
import edu.uw.tcss450.group6project.io.RequestQueueSingleton;

public class FavoriteWeatherViewModel extends AndroidViewModel {

    private MutableLiveData<List<FavoriteWeather>> mFavoriteWeather;

    public FavoriteWeatherViewModel(@NonNull Application application) {
        super(application);
        mFavoriteWeather = new MutableLiveData<>(new ArrayList<>());
    }

    public void connectDelete(String city, String state, double latitude,
                            double longitude, String jwt) {
        Log.d("Favorite Weather", "Unfavoriting Location");
        String url = getApplication().getResources().getString(R.string.url_weather_favorite);
        JSONObject body = new JSONObject();
        try {
            body.put("city", city);
            body.put("state", state);
            body.put("lat", latitude);
            body.put("long", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body, //push token found in the JSONObject body
                result -> handleFavoriteDelete(city, state, latitude, longitude), // we get a response but do nothing with it
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
    }

    public void connectPost(String city, String state, double latitude,
                            double longitude, String jwt) {
        Log.d("Favorite Weather", "Favoriting Location");
        String url = getApplication().getResources().getString(R.string.url_weather_favorite);

        JSONObject body = new JSONObject();
        try {
            body.put("city", city);
            body.put("state", state);
            body.put("lat", latitude);
            body.put("long", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //push token found in the JSONObject body
                result -> handleFavoriteAdd(city, state, latitude, longitude), // we get a response but do nothing with it
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

    }

    public void connectGet(String jwt) {
        String url = getApplication().getResources().getString(R.string.url_weather_favorite);

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleFavoriteLoad,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(getApplication().getResources().getString(R.string.header_jwt_auth), jwt);
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

    private void handleFavoriteDelete(String city, String state, double latitude,
                                   double longitude) {
        //add favorite to view model
        FavoriteWeather favoriteWeather = new FavoriteWeather(city, state, latitude, longitude);
        mFavoriteWeather.getValue().remove(favoriteWeather);
        mFavoriteWeather.setValue(mFavoriteWeather.getValue());
    }

    private void handleFavoriteAdd(String city, String state, double latitude,
                                   double longitude) {
        //add favorite to view model
        FavoriteWeather favoriteWeather = new FavoriteWeather(city, state, latitude, longitude);
        mFavoriteWeather.getValue().add(favoriteWeather);
        mFavoriteWeather.setValue(mFavoriteWeather.getValue());
    }

    /**
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing weather data
     */
    private void handleFavoriteLoad(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;

            if (root.has(getString.apply(R.string.keys_json_weather_favorite))) {
                JSONArray favorites = root.getJSONArray(
                        getString.apply(R.string.keys_json_weather_favorite));
                for(int i = 0 ; i < favorites.length(); i++) {
                    JSONObject favorite = favorites.getJSONObject(i);
                    FavoriteWeather favoriteWeather = new FavoriteWeather(
                            favorite.getString("cityname"),
                            favorite.getString("statename"),
                            favorite.getDouble("lat"),
                            favorite.getDouble("long"));
                    mFavoriteWeather.getValue().add(favoriteWeather);
                }
            } else {
                Log.e("WEATHER FAVORITE!", "No favorite data");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mFavoriteWeather.setValue(mFavoriteWeather.getValue());
        Log.d("Favorite Location View Model", "Model loaded");
    }

    /**
     * Handles errors when making requests to the server.
     * @param error the error message
     */
    private void handleError(VolleyError error) {
        //you should add much better error handling in a production release.
        error.printStackTrace();
    }

    /**
     * Checks if a given lat/long are present in the user's favorite locations
     * @param latitude latitude to search
     * @param longitude longitude to search
     * @return Whether the lat/long pair are already favorite
     */
    public boolean containsLocation(double latitude, double longitude) {

        for(FavoriteWeather fw: mFavoriteWeather.getValue()) {
            if(Double.compare(fw.getLatitude(), latitude) == 0 &&
                Double.compare(fw.getLongitude(), longitude) == 0) {
                return true;
            }
        }
        return false;
    }
}
