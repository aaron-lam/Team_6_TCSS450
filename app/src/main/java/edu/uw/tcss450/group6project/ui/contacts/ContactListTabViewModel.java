package edu.uw.tcss450.group6project.ui.contacts;

import android.app.Application;
import android.util.Log;

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

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import edu.uw.tcss450.group6project.R;

/**
 * View Model class for persistent contact data.
 * @author Aaron L
 * @version 18 November 2020
 */
public class ContactListTabViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactList;

    public ContactListTabViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
    }

    /**
     * Register as an observer to listen to retrieval of contact data.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }

    /**
     * Handles errors when making requests to the server.
     * @param error the error message
     */
    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release. //i.e. YOUR PROJECT
        mContactList.setValue(new ArrayList<>());
    }

    /**
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing contact data
     */
    private void handleGetResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        List<Contact> contacts = new ArrayList<>();
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contacts))) {
                JSONArray data = root.getJSONArray(getString.apply(R.string.keys_json_contacts));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    Contact contact = new Contact.Builder(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_first)),
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_last)),
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_username)),
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_userId)))
                            .build();
                    contacts.add(contact);
                }
            } else {
                Log.e("ERROR!", "No data array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mContactList.setValue(contacts);
    }

    /**
     * get a list of contact list
     * @param jwt user JWT token
     */
    public void connectGet(String jwt) {
        String url = "https://team6-tcss450-web-service.herokuapp.com/contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleGetResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new

                DefaultRetryPolicy( 10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(

                getApplication().

                        getApplicationContext()).

                add(request);
    }
}
