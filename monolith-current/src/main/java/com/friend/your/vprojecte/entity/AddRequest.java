package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "requests")
public class AddRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(
            name = "user_id",
            nullable = false
    )
    private Integer userId;
    @Column(
            name = "receiver_id",
            nullable = false
    )
    private Integer receiverId;

    @Column(name = "request_type")
    private int type;

    public AddRequest(int userId, int receiverId) {
        this.userId = userId;
        this.receiverId = receiverId;
    }
}
