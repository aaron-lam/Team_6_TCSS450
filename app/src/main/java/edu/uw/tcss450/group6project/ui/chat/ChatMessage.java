package edu.uw.tcss450.group6project.ui.chat;

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

    /** String containing of the email of the message's author. */
    private final String mEmail;

    /**
     * A String containing the username of the chat message's author.
     */
    private final String mUsername;

    /**
     * A String containing the content of the chat message.
     */
    private final String mMessage;

    /**
     * A Date Object representing the time the message was created.
     */
    private final Date mTimeStamp;

    /**
     * A boolean representing whether the message has been read.  Default value is false (unread).
     */
    private final boolean mRead;

    /**
     * Helper class for building chat messages.
     *
     * @author Robert M
     */
    public static class Builder {
        private final String mEmail;
        private final String mUsername;
        private final String mMessage;
        private final Date mTimestamp;
        private boolean mRead = false;

        /**
         *  Constructs a new builder.
         *
         * @param email the email of the chat message's author
         * @param username the username of the chat message's author
         * @param message the content of the chat message
         * @param timestamp the date/time the message was created
         */
        public Builder(String email, String username, String message, Date timestamp) {
            this.mEmail = email;
            this.mUsername = username;
            this.mMessage = message;
            this.mTimestamp = timestamp;
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
     * Private constructor using the Builder subclass.
     *
     * @param builder the Builder used to help create the ChatMessage
     */
    private ChatMessage(final Builder builder) {
        this.mEmail = builder.mEmail;
        this.mUsername = builder.mUsername;
        this.mMessage = builder.mMessage;
        this.mTimeStamp = builder.mTimestamp;
        this.mRead = builder.mRead;
    }

    /**
     * Getter method for the email of the chat message's author.
     * @return the email address
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Getter method for the username of the chat message's author.
     *
     * @return the author
     */
    public String getUsername() {
        return mUsername;
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
     * Getter method for the chat message's creation date/time.
     *
     * @return the date/time the message was created
     */
    public Date getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * Getter method for whether the message has been read or not.
     *
     * @return true if the message has been read; false otherwise
     */
    public boolean isRead() {
        return mRead;
    }

}
