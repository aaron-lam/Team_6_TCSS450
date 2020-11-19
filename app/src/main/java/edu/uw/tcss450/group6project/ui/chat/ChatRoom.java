package edu.uw.tcss450.group6project.ui.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatRoom implements Serializable {

    private final List<String> mParticipants;
    private MutableLiveData<List<ChatMessage>> mMessages;
    private int mChatRoomID;

    /**
     * Constructor for a new chat without any data.
     */
    public ChatRoom(int roomID) {
        mParticipants = new ArrayList<>();
        mMessages = new MutableLiveData<>(new LinkedList<>());
        mChatRoomID = roomID;
    }

    /**
     * Constructor for a new Chat, with a list of participants as the parameter.
     *
     * @param participants a list of participants in the Chat
     */
    public ChatRoom(List<String> participants) {
        mParticipants = participants;
        mMessages = new MutableLiveData<>(new LinkedList<>());
    }

    /**
     * Constructor for an existing Chat, with a list of participants and a list of past messages
     * as the parameters.
     *
     * @param participants a list of the participants in the Chat
     * @param messages a list of the previous messages in the Chat
     */
    public ChatRoom(List<String> participants, List<ChatMessage> messages) {
        mParticipants = participants;
        mMessages = new MutableLiveData<>(new LinkedList<>());
        mMessages.setValue(messages);
    }

    public void addMessage(ChatMessage message) {
        List<ChatMessage> messageList = mMessages.getValue();
        if(!messageList.contains(message)) {
            if(!mParticipants.contains(message.getEmail())) {
                mParticipants.add(message.getEmail());
            }
            messageList.add(message);
            mMessages.setValue(messageList);
        }
    }

    public int getChatRoomID() {
        return mChatRoomID;
    }

    /**
     * Getter method for the participants in a chat.
     *
     * @return the List of chat participants
     */
    public List<String> getParticipants() {
        return mParticipants;
    }

    public List<ChatMessage> getMessages() {
        return mMessages.getValue();
    }

    public void setMessages(List<ChatMessage> messages) {
        mMessages.setValue(messages);
    }

    /**
     * Returns the contents of the last message in the Chat, or an empty String if there have been
     * no messages.
     *
     * @return the contents of the last Chat message
     */
    public String getLastMessage() {
        List<ChatMessage> messages = mMessages.getValue();
        if (messages.size() > 0) {
            return messages.get(messages.size() - 1).getMessage();
        } else {
            return "";
        }
    }

    /**
     * Returns a string containing the participants of the chat, with each username separated
     * by a comma.
     *
     * @return the string containing the comma-separated participants in the chat
     */
    public String participantsAsString() {
        StringBuilder sb = new StringBuilder();
        for(String p : mParticipants) {
            sb.append(p);
            if (mParticipants.indexOf(p) < mParticipants.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Register as an observer to listen to this chat room's list of messages.
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void observe(@NonNull LifecycleOwner owner,
                        @NonNull Observer<? super List<ChatMessage>> observer) {
        mMessages.observe(owner, observer);
    }

}