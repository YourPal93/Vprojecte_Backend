package com.friend.your.vprojecte.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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
