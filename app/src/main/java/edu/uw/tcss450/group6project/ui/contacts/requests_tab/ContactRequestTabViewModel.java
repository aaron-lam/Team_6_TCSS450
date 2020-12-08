package edu.uw.tcss450.group6project.ui.contacts.requests_tab;

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
import edu.uw.tcss450.group6project.ui.contacts.Contact;

public class ContactRequestTabViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<List<ContactRequest>> mContactRequestList;

    public ContactRequestTabViewModel(@NonNull Application application) {
        super(application);
        mContactRequestList = new MutableLiveData<List<ContactRequest>>();
        mContactRequestList.setValue(new ArrayList<ContactRequest>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Register as an observer to listen to retrieval of contact request data.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addContactRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<ContactRequest>> observer) {
        mContactRequestList.observe(owner, observer);
    }

    /**
     * Handles errors when making requests to the server.
     * @param error the error message
     */
    private void handleGetError(final VolleyError error) {
        //you should add much better error handling in a production release. //i.e. YOUR PROJECT
        mContactRequestList.setValue(new ArrayList<ContactRequest>());
    }

    /**
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing contact request data
     */
    private void handleGetResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        List<ContactRequest> contactRequests = new ArrayList<>();
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contact_requests))) {
                JSONArray data = root.getJSONArray(getString.apply(R.string.keys_json_contact_requests));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    ContactRequest contactRequest = new ContactRequest.Builder(
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_username)),
                            jsonContact.getString(
                                    getString.apply(
                                            R.string.keys_json_contact_userId)))
                            .build();
                    contactRequests.add(contactRequest);
                }
            } else {
                Log.e("ERROR!", "No data array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mContactRequestList.setValue(contactRequests);
    }

    /**
     * get a list of people that have requested to be contacts
     * @param jwt user JWT token
     */
    public void connectGet(String jwt) {
        String url = "https://team6-tcss450-web-service.herokuapp.com/contactRequests";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleGetResult,
                this::handleGetError) {
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

    public void connectConfirm(String jwt, String memberId) {
        String url = "https://team6-tcss450-web-service.herokuapp.com/contactRequests/" + memberId;
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, //no body for this get request
                this::handleGetResult,
                this::handleGetError) {
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

        // Get the current list of contacts
        List<ContactRequest> tempList = mContactRequestList.getValue();
        ContactRequest tempContactRequest = null;

        // Find the contact
        for (ContactRequest cr : tempList) {
            if (cr.getMemberId() == memberId) {
                tempContactRequest = cr;
            }
        }

        // Remove the contact and update the list
        tempList.remove(tempContactRequest);
        mContactRequestList.setValue(tempList);
    }

    public void connectDeny(String jwt, String memberId) {
        String url = "https://team6-tcss450-web-service.herokuapp.com/contactRequests/" + memberId;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null, //no body for this get request
                this::handleGetResult,
                this::handleGetError) {
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

        // Get the current list of contacts
        List<ContactRequest> tempList = mContactRequestList.getValue();
        ContactRequest tempContactRequest = null;

        // Find the contact
        for (ContactRequest cr : tempList) {
            if (cr.getMemberId() == memberId) {
                tempContactRequest = cr;
            }
        }

        // Remove the contact and update the list
        tempList.remove(tempContactRequest);
        mContactRequestList.setValue(tempList);
    }
}