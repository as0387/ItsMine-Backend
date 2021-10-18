package com.dongs.jwt.domain.chat;

import com.dongs.jwt.domain.user.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ChatRoomJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
}