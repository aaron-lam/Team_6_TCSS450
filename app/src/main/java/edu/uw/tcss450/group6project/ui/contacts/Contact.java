package edu.uw.tcss450.group6project.ui.contacts;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a confirmed contact on the user's contact list.
 *
 * @author Robert M, Aaron L
 * @version 12 November 2020
 */
public class Contact implements Serializable {

    /**
     * The contact's first name.
     */
    private final String mFirstName;

    /**
     * The contact's last name.
     */
    private final String mLastName;

    /**
     * The contact's user name (display name).
     */
    private final String mUserName;

    /**
     * Helper class for building immutable Contact object.
     *
     * @author Aaron L
     */
    public static class Builder {
        private final String mFirstName;
        private final String mLastName;
        private final String mUserName;


        /**
         * Constructs a new Builder.
         *
         * @param firstName contact's first name
         * @param lastName contact's last name
         * @param userName contact's username
         */
        public Builder(String firstName, String lastName, String userName) {
            this.mFirstName = firstName;
            this.mLastName = lastName;
            this.mUserName = userName;
        }

        public Contact build() {
            return new Contact(this);
        }
    }

    /**
     * Parameterized constructor requiring the contact's first and last names and user name.
     *
     */
    private Contact (final Builder builder) {
        mFirstName = builder.mFirstName;
        mLastName = builder.mLastName;
        mUserName = builder.mUserName;
    }

    /**
     * Getter for first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Getter for last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return mLastName;
    }

    /** Getter for user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return mUserName;
    }
}
