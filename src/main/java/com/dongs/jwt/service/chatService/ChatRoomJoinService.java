package com.dongs.jwt.service.chatService;

import com.dongs.jwt.domain.chat.ChatRoom;
import com.dongs.jwt.domain.chat.ChatRoomJoin;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.ChatListDto;
import com.dongs.jwt.repository.chat.ChatRoomJoinRepository;
import com.dongs.jwt.repository.chat.ChatRoomRepository;
import com.dongs.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomJoinService {
    private final UserRepository userRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long createChatRoom(int userId1, int userId2) {//1:1채팅
        Long ret = check(userId1, userId2);
        if (ret != 0) {
            //이미 존재하는 방이면 해당 방 번호 리턴
            return ret;
        }
        ChatRoom chatRoom = new ChatRoom();
        ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);
        //두명 다 입장
        createRoom(userId1, newChatRoom);
        createRoom(userId2, newChatRoom);
        return newChatRoom.getId();
    }

    public Long check(int user1, int user2) {
        User userFirst = userRepository.findById(user1).orElseThrow(() -> new IllegalArgumentException(user1 + "는 존재하지 않습니다."));
        List<ChatRoomJoin> listFirst = chatRoomJoinRepository.findByUser(userFirst);
        Set<ChatRoom> setFirst = new HashSet<>();
        for (ChatRoomJoin chatRoomJoin : listFirst) {
            setFirst.add(chatRoomJoin.getChatRoom());
        }
        User userSecond = userRepository.findById(user2).orElseThrow(() -> new IllegalArgumentException(user2 + "는 존재하지 않습니다."));
        List<ChatRoomJoin> listSecond = chatRoomJoinRepository.findByUser(userSecond);
        for (ChatRoomJoin chatRoomJoin : listSecond) {
            if (setFirst.contains(chatRoomJoin.getChatRoom())) {
                return chatRoomJoin.getChatRoom().getId();
            }
        }
        return 0L;
    }

    public void createRoom(int userId, ChatRoom chatRoom) {
        ChatRoomJoin chatRoomJoin = new ChatRoomJoin();
        chatRoomJoin.setChatRoom(chatRoom);
        chatRoomJoin.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(userId + "는 존재하지 않습니다.")));
        chatRoomJoinRepository.save(chatRoomJoin);
    }

    @Transactional
    public List<ChatListDto> getChatRoomList(User user) {
        List<ChatRoomJoin> crjList = chatRoomJoinRepository.findByUser(user);
        List<ChatListDto> cld = new ArrayList<>();

        for(ChatRoomJoin crj : crjList){
            ChatListDto chatListDto = new ChatListDto();
            chatListDto.setChatRoomId(crj.getChatRoom().getId());
            List<ChatRoomJoin> crjList2 = chatRoomJoinRepository.findByChatRoomId(crj.getChatRoom().getId());
            for(ChatRoomJoin crj2 : crjList2) {
                if(!crj2.getUser().getNickname().equals(user.getNickname())) {
                    chatListDto.setOpponentUserName(crj2.getUser().getNickname());
                }
            }

            cld.add(chatListDto);
        }

        return cld;
    }
}
