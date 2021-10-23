package com.dongs.jwt.domain.liveAuction;

import com.dongs.jwt.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor
public class LiveChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="live_message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "LP_id")
    private LiveAuctionPost LA_post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    public LiveChatMessage(String message, LocalDateTime time, LiveAuctionPost LA_post, User writer){
        this.message=message;
        this.time=time;
        this.LA_post=LA_post;
        this.writer=writer;
    }
}
