package com.dongs.jwt.web.liveController;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.LiveBidMessageDto;
import com.dongs.jwt.dto.LiveChatMessageDto;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.service.liveAuctionService.LiveAuctionPostService;
import com.dongs.jwt.service.liveAuctionService.LiveChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class LiveAuctionMessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LiveChatMessageService liveChatMessageService;
    private final LiveAuctionPostService liveAuctionPostService;

    //라이브 경매 채팅
    @MessageMapping("/live/send")
    public void sendMsg(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveChatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/" + liveAuctionPostId, message);
    }

//    //전광판에 경매 정보 요청
//    @MessageMapping("/live/bidInfo/send")
//    public void bidInfo(LiveBidMessageDto message) throws Exception {
//        System.out.println("라이브 경매 전광판 샌드 메세지 실행");
//        int liveAuctionPostId = message.getLivePostId();
//        LiveAuctionPost post = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
//        message.setBidder(post.getBidder().getNickname());
//        message.setPrice(post.getBid());
//        simpMessagingTemplate.convertAndSend("/topic/bidInfo/" + liveAuctionPostId, message);
//    }

    @MessageMapping("/live/bidding/send") //여기서 경매 입찰 처리
    public void biding(@RequestBody HashMap<String, Object> map) throws Exception {
        int price = (int)map.get("price");
        int liveAuctionPostId = (int)map.get("livePostId");
        int principalId = (int)map.get("sender");
        LiveChatMessageDto result = liveAuctionPostService.라이브경매입찰하기(liveAuctionPostId, price, principalId);
        switch (result.getMessage()) {
            case "low":
            case "again":
            case "same":
                simpMessagingTemplate.convertAndSend("/topic/log/" + liveAuctionPostId, result);
                break;
            default:
                LiveAuctionPost post = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
                result.setMessage(post.getBidder().getNickname() + "님이" + post.getBid() + "원에 입찰하셨습니다!");
                simpMessagingTemplate.convertAndSend("/topic/log/" + liveAuctionPostId, result);
                break;
        }

    }

    //입장
    @MessageMapping("/live/in")
    public void inRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 입장 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveAuctionPostService.라이브경매입장(liveAuctionPostId);
        simpMessagingTemplate.convertAndSend("/topic/in/" + liveAuctionPostId, message);
    }

    //나감
    @MessageMapping("/live/out")
    public void outRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 퇴장 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveAuctionPostService.라이브경매퇴장(liveAuctionPostId);
        simpMessagingTemplate.convertAndSend("/topic/out/" + liveAuctionPostId, message);
    }
}