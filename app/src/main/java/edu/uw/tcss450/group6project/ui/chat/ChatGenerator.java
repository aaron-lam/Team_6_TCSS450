package edu.uw.tcss450.group6project.ui.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class is used to create Dummy Chats for development purposes.  Adapted from BlogGenerator
 * (TCSS 450 Au20 Lab 2 by Charles Bryan).
 *
 * @author Robert M
 * @version 2 November 2020
 */
public class ChatGenerator {

    /**
     * An array of chats to be generated for testing purposes.
     */
    private static final ChatRoom[] CHATS;

    /**
     * The number of chats to generate.
     */
    public static final int COUNT = 20;

    static {


        List<String> users = new ArrayList<>();
        List<ChatMessage> msgList = new ArrayList<>();

        users.add("Jim");
        users.add("Jack");
        users.add("Jose");
        users.add("Tito");

        for(int i = 0; i < COUNT; i++) {
            ChatMessage msg = new ChatMessage
                    .Builder(i, "cfb3@uw.edu",
                    "This is a fake chat for the purposes of testing.\n Hi, how are you? \n This is some more text to make sure you don't see it")
                    .build();
            msgList.add(msg);
        }

        CHATS = new ChatRoom[COUNT];
        for (int i = 0; i < CHATS.length; i++) {
            CHATS[i] = new ChatRoom(users, msgList);
        }
    }

    /**
     * Returns a list of Chats for testing purposes.
     *
     * @return the list of Chats
     */
    public static List<ChatRoom> getChatList() {
        return Arrays.asList(CHATS);
    }

    /**
     * Returns the array of Chats for testing purposes.
     *
     * @return the array of Chats
     */
    public static ChatRoom[] getCHATS() {
        return Arrays.copyOf(CHATS, CHATS.length);
    }

    /**
     * Private constructor.
     */
    private ChatGenerator(){}
}
