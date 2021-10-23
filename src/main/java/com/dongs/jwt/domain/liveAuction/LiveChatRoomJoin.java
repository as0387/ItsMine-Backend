package com.dongs.jwt.domain.liveAuction;

import com.dongs.jwt.domain.user.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class LiveChatRoomJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name =  "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "LP_id")
    private LiveAuctionPost LAPost;
}