package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @OneToOne
    @JoinColumn(name = "friend_id")
    private AppUserPlate friendPlate;
    @Column(name = "added")
    private boolean added;

    public Friend(AppUserPlate friendPlate) {
        this.friendPlate = friendPlate;
    }
}
