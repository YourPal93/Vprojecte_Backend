package com.friend.your.vprojecte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "chats")
public class Chat {

    @Id
    @Column(name = "id")
    private String id;
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "chat_id")
    private List<Message> messageLog;

    public Chat(String id) {
        this.id = id;
    }
}
