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

public class WeatherTabViewModel extends AndroidViewModel {

    private MutableLiveData<List<WeatherData>> mWeatherDataList;

    public WeatherTabViewModel(@NonNull Application application) {
        super(application);
        mWeatherDataList = new MutableLiveData<>();
        mWeatherDataList.setValue(new ArrayList<>());
    }

    public void connectLocation(double latitude, double longitude) {
        String url = "https://team6-tcss450-web-service.herokuapp.com/weather/location";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
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
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void addWeatherDataListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<WeatherData>> observer) {
        mWeatherDataList.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;

            if (root.has(getString.apply(R.string.keys_json_data))) {
                JSONArray data = root.getJSONArray(
                        getString.apply(R.string.keys_json_data));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonBlog = data.getJSONObject(i);
                    WeatherData post = new WeatherData.Builder(
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_day)),
                            jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_weather)),
                            Double.parseDouble(jsonBlog.getString(
                                    getString.apply(
                                            R.string.keys_json_weather_temp))))
                            .build();
                    if (!mWeatherDataList.getValue().contains(post)) {
                        mWeatherDataList.getValue().add(post);
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

    private void handleError(VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }


}
