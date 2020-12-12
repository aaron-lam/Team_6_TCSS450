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

public class FavoriteWeatherViewModel extends AndroidViewModel {

    private MutableLiveData<List<FavoriteWeather>> mFavoriteWeather;

    public FavoriteWeatherViewModel(@NonNull Application application) {
        super(application);
        mFavoriteWeather = new MutableLiveData<>(new ArrayList<>());
    }

    public void connectGet(String jwt) {
        String url = getApplication().getResources().getString(R.string.url_weather_favorite);

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
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
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing weather data
     */
    private void handleResult(final JSONObject result) {
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
                            favorite.getString("city"),
                            favorite.getString("state"),
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

    }

}
