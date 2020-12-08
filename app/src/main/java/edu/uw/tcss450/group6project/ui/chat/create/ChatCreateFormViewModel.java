package edu.uw.tcss450.group6project.ui.chat.create;

import android.app.Application;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.io.RequestQueueSingleton;
import edu.uw.tcss450.group6project.ui.chat.add_contact.ChatContactAddFormFragment;
import edu.uw.tcss450.group6project.ui.contacts.Contact;

/**
 * View Model class for persistent contact data.
 * @author Aaron L
 * @version 18 November 2020
 */
public class ChatCreateFormViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactList;
    private MutableLiveData<Set<String>> mSelectedContactsSet;
    private MutableLiveData<JSONObject> mResponse;

    public ChatCreateFormViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(new ArrayList<>());
        mSelectedContactsSet = new MutableLiveData<>();
        mSelectedContactsSet.setValue(new HashSet<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Register as an observer to listen to retrieval of contact data.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addChatCreateFormObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }

    /**
     * Handles unsuccessful request to the web service.
     * No Client Behavior, just sends error messages to the android logs.
     * @param error error from the load.
     */
    private void handleError(final VolleyError error) {
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
     * When a successful call is made to the server. Parses the retrieved JSON
     * and stores the data in the view model.
     * @param result JSON retrieved from server containing contact data
     */
    private void handleResult(final JSONObject result) {
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
        String url = getApplication().getResources().getString(R.string.base_url) + "contacts";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
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

    /**
     * Create a new chat room.
     * @param jwt user JWT token
     * @param roomName room name
     * @param fragment chat create form fragment
     */
    public void connectPost(String jwt, String roomName, ChatCreateFormFragment fragment) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats";
        JSONObject body = new JSONObject();
        JSONArray memberIds = new JSONArray();
        for (String id : mSelectedContactsSet.getValue()) {
            memberIds.put(id);
        }
        try {
            body.put("name", roomName);
            body.put("memberIds", memberIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> handleChatRoomCreateSuccess(response, fragment),
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
     * Add new contacts to chat room
     * @param jwt user JWT token
     * @param roomName room name
     * @param fragment chat create form fragment
     */
    public void addNewContactsToRoom(String jwt, int roomName, ChatContactAddFormFragment fragment) {
        if (Objects.requireNonNull(mSelectedContactsSet.getValue()).isEmpty()) {
            fragment.handleAddNewContactsToRoomError("Please select at least one contact.", 0);
            return;
        }
        String url = getApplication().getResources().getString(R.string.base_url) + "chats/" + roomName;
        JSONObject body = new JSONObject();
        JSONArray memberIds = new JSONArray();
        for (String id : mSelectedContactsSet.getValue()) {
            memberIds.put(id);
        }
        try {
            body.put("memberIds", memberIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                response -> handleAddNewContactToRoomSuccess(response, fragment),
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    try {
                        JSONObject jsonObject = new JSONObject(new String(networkResponse.data, StandardCharsets.UTF_8));
                        fragment.handleAddNewContactsToRoomError(jsonObject.get("message"), 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }) {
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
     * Update contact list.
     * @param memberId member id
     */
    public void updateContact(String memberId) {
        Set<String> newSet = mSelectedContactsSet.getValue();
        if (Objects.requireNonNull(mSelectedContactsSet.getValue()).contains(memberId)) {
            newSet.remove(memberId);
        }
        else {
            newSet.add(memberId);
        }
        mSelectedContactsSet.setValue(newSet);
    }

    /**
     * Method to handle a successful delete request of a specific chat room.
     * @param response Response in JSON format
     */
    private void handleChatRoomCreateSuccess(final JSONObject response, ChatCreateFormFragment fragment) {
        if (!response.has("sucess")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        fragment.createNewChatRoomCallback();
    }

    /**
     * Method to handle a successful delete request of a specific chat room.
     * @param response Response in JSON format
     */
    private void handleAddNewContactToRoomSuccess(JSONObject response, ChatContactAddFormFragment fragment) {
        if (!response.has("sucess")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        fragment.addNewContactsToRoomCallback();
    }
}
