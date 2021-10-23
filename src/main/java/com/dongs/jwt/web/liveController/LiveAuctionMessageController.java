package com.dongs.jwt.web.liveController;

import com.dongs.jwt.dto.LiveChatMessageDto;
import com.dongs.jwt.service.liveAuctionService.LiveChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LiveAuctionMessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LiveChatMessageService liveChatMessageService;
    @MessageMapping("/live/send")
    public void sendMsg(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveChatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/" + liveAuctionPostId,message);
    }
    @MessageMapping("/live/bidding/send")
    public void biding(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveChatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/" + liveAuctionPostId,message);
    }

    @MessageMapping("/live/in")
    public void inRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 입장 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        simpMessagingTemplate.convertAndSend("/topic/"+liveAuctionPostId+"/in",message);
    }
    @MessageMapping("/live/out")
    public void outRoom(LiveChatMessageDto message) throws Exception {
        System.out.println("라이브 경매 샌드 메세지 실행");
        int liveAuctionPostId = message.getLivePostId();
        liveChatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/"+liveAuctionPostId+"out",message);
    }

    //@MessageMapping("/auction/send")
//    public void sendMsg(ChatMessageDto message) throws Exception {
//        System.out.println("샌드 메세지 실행");
//        String receiver = message.getReceiver();
//        chatMessageService.save(message);
//        simpMessagingTemplate.convertAndSend("/topic/" + receiver,message);
//    }

}