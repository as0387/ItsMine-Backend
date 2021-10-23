package com.dongs.jwt.web.liveController;

import com.dongs.jwt.domain.chat.ChatRoom;
import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.service.liveAuctionService.LiveChatMessageService;
import com.dongs.jwt.service.liveAuctionService.LiveAuctionPostJoinService;
import com.dongs.jwt.service.liveAuctionService.LiveAuctionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LiveAuctionController {

    private final LiveAuctionPostJoinService liveAuctionPostJoinService;
    private final LiveAuctionPostService liveAuctionPostService;
    private final LiveChatMessageService liveChatMessageService;

//    @GetMapping("/chat-list")
//    public ResponseEntity<?> chatList(@AuthenticationPrincipal PrincipalDetails principal){
//        User user = principal.getUser();
//        List<ChatListDto> list = chatRoomJoinService.getChatRoomList(user);
//        return new ResponseEntity<List<ChatListDto>>(list, HttpStatus.OK);
//    }

    @PostMapping("/chat/newChat")
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Integer> roomUserInfo){
        int userId1 = roomUserInfo.get("userId1");
        int userId2 =roomUserInfo.get("userId2");
        return new ResponseEntity<Long>(liveAuctionPostJoinService.createChatRoom(userId1,userId2), HttpStatus.OK);
    }

    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<?> openChatRoom(@PathVariable int chatRoomId){
        return new ResponseEntity<LiveAuctionPost>(liveAuctionPostService.openChatRoom(chatRoomId), HttpStatus.OK);
    }

//    @PostMapping("/chat")
//    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> messageInfo){
//        int writerId = (int)messageInfo.get("writerId");
//        Long roomId = Long.valueOf(String.valueOf(messageInfo.get("roomId")));
//        String message = (String)messageInfo.get("message");
//        chatMessageService.sendMessage(writerId,roomId,message);
//        return new ResponseEntity<String>("ok", HttpStatus.OK);
//    }

}
