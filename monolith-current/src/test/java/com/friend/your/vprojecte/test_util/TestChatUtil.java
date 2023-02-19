package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.Chat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestChatUtil {

    public static Chat getTestChatNoId() {
        Chat testChat = new Chat();

        testChat.setUsers(new ArrayList<>());
        testChat.setMessageLog(new HashSet<>());

        return testChat;
    }

    public static Chat getTestChat() {
        Chat testChat = getTestChatNoId();

        testChat.setId(1);

        return testChat;
    }

    public static List<Chat> getTestChatMultiple(int amount) {
        List<Chat> testChats = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Chat testChat = getTestChatNoId();

            testChat.setId(i + 1);
            testChats.add(testChat);
        }

        return testChats;
    }

    public static List<Chat> getTestChatMultipleNoId(int amount) {
        List<Chat> testChats = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Chat testChat = getTestChatNoId();

            testChats.add(testChat);
        }

        return testChats;
    }
}
