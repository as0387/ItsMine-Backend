package com.dongs.jwt.web.chat;

import com.dongs.jwt.dto.ChatMessageDto;
import com.dongs.jwt.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    @MessageMapping("/talk/send")
    public void sendMsg(ChatMessageDto message) throws Exception {
        System.out.println("샌드 메세지 실행");
        String receiver = message.getReceiver();
        chatMessageService.save(message);
        simpMessagingTemplate.convertAndSend("/topic/" + receiver,message);
    }

}