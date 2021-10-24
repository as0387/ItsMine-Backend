package com.dongs.jwt.web.liveController;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.dto.LiveBidMessageDto;
import com.dongs.jwt.dto.LiveChatMessageDto;
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

    //전광판에 경매 정보 요청
    @MessageMapping("/live/bidInfo/send")
    public void bidInfo(LiveBidMessageDto message) throws Exception {
        System.out.println("라이브 경매 전광판 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        LiveAuctionPost post = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
        message.setBidder(post.getBidder().getNickname());
        message.setPrice(post.getBid());
        simpMessagingTemplate.convertAndSend("/topic/bidInfo/" + liveAuctionPostId, message);
    }

    @MessageMapping("/live/bidding/send") //여기서 경매 입찰 처리
    public ResponseEntity<?> biding(@RequestBody HashMap<String, Integer> map,
                                    @AuthenticationPrincipal PrincipalDetails principal) throws Exception {
        System.out.println("라이브 입찰 샌드 메세지 실행");
        int price = map.get("price");
        int liveAuctionPostId = map.get("liveAuctionPostId");
        LiveAuctionPost post =  liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
        String result = liveAuctionPostService.라이브경매입찰하기(post, price, principal.getUser());
        if(result.equals("low")){
            return new ResponseEntity<String>(result, HttpStatus.FORBIDDEN);
        }else if(result.equals("same")){
            return new ResponseEntity<String>(result, HttpStatus.FORBIDDEN);
        }
        String message = post.getBidder()+ "님이" + post.getBid() + "원에 입찰하셨습니다!";
        simpMessagingTemplate.convertAndSend("/topic/log/" + liveAuctionPostId, message);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    //입장
    @MessageMapping("/live/in")
    public void inRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 입장 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        LiveAuctionPost post = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
        post.setBidEntryCount(post.getBidEntryCount() + 1);
        simpMessagingTemplate.convertAndSend("/topic/in/" + liveAuctionPostId, message);
    }

    //나감
    @MessageMapping("/live/out")
    public void outRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 퇴장 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        LiveAuctionPost post = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
        if (post.getBidEntryCount() > 0) {
            post.setBidEntryCount(post.getBidEntryCount() - 1);
        }
        simpMessagingTemplate.convertAndSend("/topic/out/" + liveAuctionPostId, message);
    }
}