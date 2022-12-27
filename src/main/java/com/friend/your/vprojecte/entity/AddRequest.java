package com.friend.your.vprojecte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "requests")
public class AddRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(
            name = "user_id",
            nullable = false
    )
    private int userId;
    @Column(
            name = "receiver_id",
            nullable = false
    )
    private int receiverId;

    @Column(name = "request_type")
    private int type;

    public AddRequest(int userId, int receiverId) {
        this.userId = userId;
        this.receiverId = receiverId;
    }
}
