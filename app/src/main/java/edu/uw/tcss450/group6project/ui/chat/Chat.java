package edu.uw.tcss450.group6project.ui.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to encapsulate a Chat between users of the application.  Building an Object requires a
 * List of Participants.
 *
 * A list of chat messages may be included as an optional field.
 *
 * @author Robert M
 * @version 2 November 2020
 */
public class Chat implements Serializable {

    private final List<String> mParticipants;
    private final List<ChatMessage> mMessages;

    /**
     * Constructor for a new Chat, with a list of participants as the parameter.
     *
     * @param participants a list of participants in the Chat
     */
    public Chat(List<String> participants) {
        mParticipants = participants;
        mMessages = new ArrayList<>();
    }

    /**
     * Constructor for an existing Chat, with a list of participants and a list of past messages
     * as the parameters.
     *
     * @param participants a list of the participants in the Chat
     * @param messages a list of the previous messages in the Chat
     */
    public Chat(List<String> participants, List<ChatMessage> messages) {
        mParticipants = participants;
        mMessages = messages;
    }

    /**
     * Getter method for the participants in a chat.
     *
     * @return the List of chat participants
     */
    public List<String> getParticipants() {
        return mParticipants;
    }

    /**
     * Getter method for the messages in a chat.
     *
     * @return the List of chat messages
     */
    public List<ChatMessage> getMessages() {
        return mMessages;
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
     * Returns the contents of the last message in the Chat, or an empty String if there have been
     * no messages.
     *
     * @return the contents of the last Chat message
     */
    public String getLastMessage() {
        if (mMessages.size() > 0) {
            return mMessages.get(mMessages.size() - 1).getMessage();
        } else {
            return "";
        }

    }
}
