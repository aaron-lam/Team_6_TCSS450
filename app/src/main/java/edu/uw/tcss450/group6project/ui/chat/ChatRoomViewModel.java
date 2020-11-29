package edu.uw.tcss450.group6project.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.io.RequestQueueSingleton;

/**
 * View Model to store information about the chat rooms the user is in
 * and messages sent by and to the user.
 * @author Charles Bryan, Anthony Nguyen, Aaron Lam
 */
public class ChatRoomViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private Map<Integer, ChatRoom> mChatRooms;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mChatRooms = new HashMap<>();
    }


    /**
     * Register as an observer to listen to changes in ALL the chat rooms.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addRoomObserver(@NonNull LifecycleOwner owner,
                                @NonNull Observer<? super List<ChatMessage>> observer) {
        for(ChatRoom cRoom : mChatRooms.values()) {
            cRoom.observe(owner, observer);
        }
    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     * @param chatId the chatId of the chat to observer
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addMessageObserver(int chatId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<ChatMessage>> observer) {
        getOrCreateMapEntry(chatId).observe(owner, observer);
    }

    /**
     * Return a reference to the List<> associated with the chat room. If the View Model does
     * not have a mapping for this chatID, it will be created.
     *
     * WARNING: While this method returns a reference to a mutable list, it should not be
     * mutated externally in client code. Use public methods available in this class as
     * needed.
     *
     * @param chatId the id of the chat room List to retrieve
     * @return a reference to the list of messages
     */
    public List<ChatMessage> getMessageListByChatId(final int chatId) {
        return getOrCreateMapEntry(chatId).getMessages();
    }

    private ChatRoom getOrCreateMapEntry(final int chatId) {
        if(!mChatRooms.containsKey(chatId)) {
            mChatRooms.put(chatId, new ChatRoom(chatId));
        }
        return mChatRooms.get(chatId);
    }

    /**
     * Returns all the chat rooms loaded in the view model.
     * @return List of chat rooms.
     */
    public List<ChatRoom> getChatRooms() {
        List<ChatRoom> rooms = new ArrayList<>(mChatRooms.values());
        return new ArrayList<>(mChatRooms.values());
    }

    /**
     * Requests all the chat rooms that the user is a member of
     * @param email email of the user
     * @param jwt jwt of the user
     */
    public void loadChatRooms(final String email, final String jwt) {

        String url = getApplication().getResources().getString(R.string.url_chat_chatrooms) + email;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> handleChatRoomSuccess(response, jwt),
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put(getApplication().getResources().getString(R.string.header_jwt_auth), jwt);
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
     * Request to delete a user from a chat room
     * @param jwt jwt of the user
     * @param chatId id of the chat room
     * @param email email of the user
     */
    public void deleteChatRoom(final String jwt,
                               final int chatId,
                               final String email,
                               ChatListRecyclerViewAdapter.ChatListViewHolder holder) {

        String url = getApplication().getResources().getString(R.string.base_url)
                + "chats/" + chatId + "/" + email;

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> handleChatRoomDeleteSuccess(response, chatId, holder),
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
     * Makes a request to the web service to get the first batch of messages for a given Chat Room.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent requests to the web service for a given chat room should be made from
     * getNextMessages()
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt the users signed JWT
     */
    public void getFirstMessages(final int chatId, final String jwt) {

        String url = getApplication().getResources().getString(R.string.url_chat_messages) + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleChatMessagesSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put(getApplication().getResources().getString(R.string.header_jwt_auth), jwt);
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

        //code here will run
    }

    /**
     * Makes a request to the web service to get the next batch of messages for a given Chat Room.
     * This request uses the earliest known ChatMessage in the associated list and passes that
     * messageId to the web service.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent calls to this method receive earlier and earlier messages.
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt the users signed JWT
     */
    public void getNextMessages(final int chatId, final String jwt) {
        String url = getApplication().getResources().getString(R.string.url_chat_messages) +
                chatId +
                "/" +
                mChatRooms.get(chatId).getMessages().get(0).getMessageID();

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleChatMessagesSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put(getApplication().getResources().getString(R.string.header_jwt_auth), jwt);
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

        //code here will run
    }

    /**
     * When a chat message is received externally to this ViewModel, add it
     * with this method.
     * @param chatId
     * @param message
     */
    public void addMessage(final int chatId, final ChatMessage message) {
        ChatRoom room = mChatRooms.get(chatId);
        room.addMessage(message);
    }

    /**
     * Method to handle a successful request of chat rooms the user is a member of.
     * Stores chat rooms in the view model and requests recent messages from these chat rooms.
     * @param response
     * @param jwt
     */
    private void handleChatRoomSuccess(final JSONObject response, final String jwt) {
        if(!response.has("rowCount") || !response.has("rows")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        try {
            int roomCount = response.getInt("rowCount");
            Log.d("Handle Chat Room Success", Integer.toString(roomCount));
            JSONArray rooms = response.getJSONArray("rows");
            for(int i = 0; i < roomCount; i++) {
                if(!mChatRooms.containsKey(rooms.getInt(i))) {
                    int id = rooms.getInt(i);
                    Log.d("Room ID", Integer.toString(id));
                    getFirstMessages(id, jwt);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handleChatRoomSuccess ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * Method to handle a successful request of messages in a specified chat room.
     * Stores the messages sent into the view model.
     * @param response
     */
    private void handleChatMessagesSuccess(final JSONObject response) {
        List<ChatMessage> list;
        if (!response.has("chatId")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        try {
            list = getMessageListByChatId(response.getInt("chatId"));
            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ChatMessage cMessage = new ChatMessage.Builder(
                        message.getInt("messageid"),
                        message.getString("email"),
                        message.getString("message") //TODO put the timestamp back in
                ).build();
                if (!list.contains(cMessage)) {
                    // don't add a duplicate
                    list.add(0, cMessage);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat message already received",
                            "Or duplicate id:" + cMessage.getMessageID());
                }

            }
            //inform observers of the change (setValue)
            getOrCreateMapEntry(response.getInt("chatId")).setMessages(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handleChatMessageSuccess ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * Method to handle a successful delete request of a specific chat room.
     * @param response Response in JSON format
     */
    private void handleChatRoomDeleteSuccess(final JSONObject response, final int chatId, ChatListRecyclerViewAdapter.ChatListViewHolder holder) {
        if (!response.has("sucess")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        this.mChatRooms.remove(chatId);
        holder.deleteChatCallback();
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
}