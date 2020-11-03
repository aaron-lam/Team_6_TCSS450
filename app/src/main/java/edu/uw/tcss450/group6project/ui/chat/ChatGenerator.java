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
    private static final Chat[] CHATS;
    public static final int COUNT = 20;

    static {
        ChatMessage msg = new ChatMessage
                .Builder("Jim",
                "This is a fake chat for the purposes of testing.\n Hi, how are you?",
                new Date())
                .build();

        List<String> users = new ArrayList<>();
        List<ChatMessage> msgList = new ArrayList<>();

        users.add("Jim");
        users.add("Jack");
        users.add("Jose");
        users.add("Tito");

        for(int i = 0; i < COUNT; i++) {
            msgList.add(msg);
        }

        CHATS = new Chat[COUNT];
        for (int i = 0; i < CHATS.length; i++) {
            CHATS[i] = new Chat(users, msgList);
        }
    }

    /**
     * Returns a list of Chats for testing purposes.
     *
     * @return the list of Chats
     */
    public static List<Chat> getChatList() {
        return Arrays.asList(CHATS);
    }

    /**
     * Returns the array of Chats for testing purposes.
     *
     * @return the array of Chats
     */
    public static Chat[] getCHATS() {
        return Arrays.copyOf(CHATS, CHATS.length);
    }

    /**
     * Private constructor.
     */
    private ChatGenerator(){}
}
