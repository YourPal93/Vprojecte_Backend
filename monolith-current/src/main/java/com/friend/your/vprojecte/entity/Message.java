package com.friend.your.vprojecte.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(
            name = "author",
            nullable = false
    )
    private Integer author;
    @Column(
            name = "message_content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String messageContent;

    @Column(name = "chat_id")
    private Integer chatId;

    public Message(Integer author, String messageContent) {
        this.author = author;
        this.messageContent = messageContent;
    }
}
