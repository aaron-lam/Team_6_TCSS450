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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.io.RequestQueueSingleton;

/**
 * Class that stores and updates a user's favorite weather locations.
 * @author Anthony
 */
public class FavoriteWeatherViewModel extends AndroidViewModel {

    /** Model with a list of the user's favorite locations. */
    private MutableLiveData<List<FavoriteWeather>> mFavoriteWeather;

    /**
     * Constructor to initialize the view model.
     * @param application
     */
    public FavoriteWeatherViewModel(@NonNull Application application) {
        super(application);
        mFavoriteWeather = new MutableLiveData<>(new ArrayList<>());
    }

    /**
     * Connect to the web service and request to delete a location from the user's favorites.
     * @param city city of location to delete
     * @param state state of location to delete
     * @param latitude latitude of location to delete
     * @param longitude longitude of location to delete
     * @param jwt jwt for authorization and user information
     */
    public void connectDelete(String city, String state, double latitude,
                            double longitude, String jwt) {

        String url = getApplication().getResources().getString(R.string.url_weather_favorite);

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                result -> handleFavoriteDelete(city, state, latitude, longitude), // we get a response but do nothing with it
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                headers.put("city", city);
                headers.put("state", state);
                headers.put("lat", Double.toString(latitude));
                headers.put("long", Double.toString(longitude));
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

    /**
     * Connect to webservice and request to add a location to the user's favorites.
     * @param city city of the favorite location
     * @param state state of the favorite location
     * @param latitude latitude of the favorite location
     * @param longitude longitude of the favorite location
     * @param jwt jwt for authorization and user info
     */
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

    /**
     * Connect to the webservice and retrieve a list of the user's favorite locations.
     * @param jwt jwt for authorization and user info.
     */
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

    /**
     * Removes the city from the view model after the delete request is made.
     * @param city city of location to delete
     * @param state state of location to delete
     * @param latitude latitude of location to delete
     * @param longitude longitude of location to delete
     */
    private void handleFavoriteDelete(String city, String state, double latitude,
                                   double longitude) {
        Log.d("Favorite Location", "Deleting From List");
        //add favorite to view model
        FavoriteWeather favoriteWeather = new FavoriteWeather(city, state, latitude, longitude);
        mFavoriteWeather.getValue().remove(favoriteWeather);
        mFavoriteWeather.setValue(mFavoriteWeather.getValue());
    }

    /**
     * Adds the city to the view model after a successful post requst is made
     * @param city city of location to add
     * @param state state of location to add
     * @param latitude latitude of location to add
     * @param longitude longitude of location to add
     */
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
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
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

    /** Returns the list of favorites in the view model. */
    public List<FavoriteWeather> getFavorites() {
        return mFavoriteWeather.getValue();
    }
}
