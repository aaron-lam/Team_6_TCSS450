package edu.uw.tcss450.group6project.ui.chat;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Class to encapsulate a single chat message.  Building an Object requires the sender's username,
 * the chat message, and a time stamp.
 *
 * A boolean indicating whether the message has been seen or not is an optional field.
 *
 * @author Robert M
 * @version 2 November 2020
 */
public class ChatMessage implements Serializable {

    /** ID in the database of the message. */
    private final int mMessageID;

    /** String containing of the email of the message's author. */
    private final String mEmail;

    /**
     * A String containing the content of the chat message.
     */
    private final String mMessage;

    /**
     * A boolean representing whether the message has been read.  Default value is false (unread).
     */
    private boolean mRead;

    /**
     * A string of the username.
     */
    private final String mUsername;

    /**
     * Helper class for building chat messages.
     *
     * @author Robert M
     */
    public static class Builder {
        private final int mMessageID;
        private final String mEmail;
        private final String mMessage;
        private boolean mRead = true;
        private String mUsername;

        /**
         *  Constructs a new builder.
         *
         * @param messageID the id of the chat message
         * @param email the email of the chat message's author
         * @param message the content of the chat message
         */
        public Builder(int messageID, String email, String message) {
            this.mMessageID = messageID;
            this.mEmail = email;
            this.mMessage = message;
        }

        /**
         *  Constructs a new builder with username.
         *
         * @param messageID the id of the chat message
         * @param email the email of the chat message's author
         * @param message the content of the chat message
         * @param username the username of the chat message's author
         */
        public Builder(int messageID, String email, String message, String username) {
            this.mMessageID = messageID;
            this.mEmail = email;
            this.mMessage = message;
            this.mUsername = username;
        }

        /**
         * Add an optional boolean flag for if the message was already viewed.
         *
         * @param read true if the message has been viewed, false otherwise
         * @return the Builder of this ChatMessage
         */
        public Builder addRead(final Boolean read) {
            mRead = read;
            return this;
        }

        /**
         * Creates a ChatMessage using the builder.
         *
         * @return the ChatMessage constructed by the builder
         */
        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        ChatMessage cm = new ChatMessage.Builder(msg.getInt("messageid"),
                msg.getString("email"),
                msg.getString("message"),
                msg.getString("username")).build(); //TODO Add timestamp
        cm.setIsRead(false);
        return cm;
    }


    /**
     * Private constructor using the Builder subclass.
     *
     * @param builder the Builder used to help create the ChatMessage
     */
    private ChatMessage(final Builder builder) {
        this.mMessageID = builder.mMessageID;
        this.mEmail = builder.mEmail;
        this.mMessage = builder.mMessage;
        this.mRead = builder.mRead;
        this.mUsername = builder.mUsername;
    }

    /**
     * Getter method for the email of the chat message's author.
     * @return the email address
     */
    public String getEmail() {
        return mEmail;
    }


    /**
     * Getter method for the chat message id
     * @return message id
     */
    public int getMessageID() {
        return mMessageID;
    }

    /**
     * Getter method for the chat message's content.
     *
     * @return the message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Getter method for the chat message's username.
     *
     * @return the username
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Getter method for whether the message has been read or not.
     *
     * @return true if the message has been read; false otherwise
     */
    public boolean isRead() {
        return mRead;
    }


    public void setIsRead(boolean isRead) {
        this.mRead = isRead;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageID == ((ChatMessage) other).mMessageID;
        }
        return result;
    }

}
