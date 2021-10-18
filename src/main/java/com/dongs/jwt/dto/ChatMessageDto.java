package com.dongs.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long ChatRoomId;
    private String receiver;

    private String sender;
    private String message;
}
