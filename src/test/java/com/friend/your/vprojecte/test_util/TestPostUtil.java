package com.friend.your.vprojecte.test_util;

import com.friend.your.vprojecte.entity.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestPostUtil {

    public static Post getTestPostNoId() {
        Post testPost = new Post();

        testPost.setUserLogin("TEST_LOGIN");
        testPost.setDescription("TEST_DESCRIPTION");
        testPost.setUrl("TEST_URL");
        testPost.setComments(new ArrayList<>());
        testPost.setLikes(new HashSet<>());
        testPost.setType(2);
        testPost.setCreationDate(LocalDateTime.now());

        return testPost;
    }

    public static Post getTestPost() {
        Post testPost = getTestPostNoId();

        testPost.setId(1);

        return testPost;
    }

    public static List<Post> getTestPostMultipleNoId(int amount) {
        List<Post> testPosts = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Post testPost = getTestPostNoId();

            testPosts.add(testPost);
        }

        return testPosts;
    }

    public static List<Post> getTestPostMultiple(int amount) {
        List<Post> testPosts = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            Post testPost = getTestPostNoId();

            testPost.setId(i + 1);
            testPosts.add(testPost);
        }

        return testPosts;
    }
}
