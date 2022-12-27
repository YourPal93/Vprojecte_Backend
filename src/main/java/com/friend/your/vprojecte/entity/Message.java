package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(
            name = "author",
            nullable = false
    )
    private int author;
    @Column(
            name = "message_content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String messageContent;

    public Message(int author, String messageContent) {
        this.author = author;
        this.messageContent = messageContent;
    }
}
