package com.friend.your.vprojecte.enums;

public enum PostTypes {
    USER(0),
    GROUP(1),
    FEED(2);

    final int index;

    PostTypes(int index) {
        this.index = index;
    }
}
