package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class TestMessageUtil {

    public static Message getTestMessageNoId() {
        Message testMessage = new Message();

        testMessage.setMessageContent("TEST_MESSAGE");
        testMessage.setAuthor(1);
        testMessage.setChatId(1);

        return testMessage;
    }

    public static Message getTestMessage() {
        Message testMessage = getTestMessageNoId();

        testMessage.setId(1);

        return testMessage;
    }

    public static List<Message> getTestMessageMultiple(int amount) {
        List<Message> testMessages = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Message testMessage = getTestMessageNoId();

            testMessage.setId(i + 1);
            testMessages.add(testMessage);
        }

        return testMessages;
    }

    public static List<Message> getTestMessageMultipleNoId(int amount) {
        List<Message> testMessages = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Message testMessage = getTestMessageNoId();

            testMessages.add(testMessage);
        }

        return testMessages;
    }
}
