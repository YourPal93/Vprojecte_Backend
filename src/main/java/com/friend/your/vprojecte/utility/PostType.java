package com.friend.your.vprojecte.utility;

public enum PostType {
    USER(0),
    GROUP(1),
    FEED(2);

    final int index;

    PostType (int index) {
        this.index = index;
    }
}
