package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(
            name = "friend_id",
            nullable = false
    )
    private int friendId;
    @Column(name = "added")
    private boolean added;

    public Friend(int friendId, boolean added) {
        this.friendId = friendId;
        this.added = added;
    }
}
