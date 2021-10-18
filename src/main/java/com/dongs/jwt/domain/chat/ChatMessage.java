package com.dongs.jwt.domain.chat;

import com.dongs.jwt.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    public ChatMessage(String message, LocalDateTime time, ChatRoom chatRoom, User writer){
        this.message=message;
        this.time=time;
        this.chatRoom=chatRoom;
        this.writer=writer;
    }
}
