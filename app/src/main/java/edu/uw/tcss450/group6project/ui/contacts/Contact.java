package edu.uw.tcss450.group6project.ui.contacts;

/**
 * Represents a confirmed contact on the user's contact list.
 *
 * @author Robert M
 * @version 12 November 2020
 */
public class Contact {

    /**
     * The contact's first name.
     */
    private String mFirstName;

    /**
     * The contact's last name.
     */
    private String mLastName;

    /**
     * The contact's user name (display name).
     */
    private String mUserName;

    /**
     * Private parameter-less constructor to prevent use.
     */
    private Contact() {
        // Empty constructor
    }

    /**
     * Parameterized constructor requiring the contact's first and last names and user name.
     *
     * @param first contact's first name
     * @param last contact's last name
     * @param user contact's user name
     */
    public Contact (final String first, final String last, final String user) {
        mFirstName = first;
        mLastName = last;
        mUserName = user;
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
