package edu.uw.tcss450.group6project.ui.contacts;

import java.util.Arrays;
import java.util.List;

/**
 * A class used to create Dummy Contacts for development purposes.
 *
 * @author Robert M
 * @version 3 November 2020
 */
public class DummyContacts {

    /**
     * Array of dummy contacts for fake data display.
     */
    private static final String[] CONTACTS = {"Jack", "Jim", "Jose", "Tito", "Aaron", "Anthony",
        "Chase", "Robert", "Charles", "Mark", "Joe", "Don"};

    /**
     * Returns the contacts as a List.
     *
     * @return a List of contacts
     */
    public static List<String> getContactList() {
        return Arrays.asList(CONTACTS);
    }

    /**
     * Returns the contacts as an array.
     *
     * @return an Array of contacts
     */
    public static String[] getCONTACTS() {
        return Arrays.copyOf(CONTACTS, CONTACTS.length);
    }

    /**
     * Private constructor.
     */
    private DummyContacts() {

    }

}
