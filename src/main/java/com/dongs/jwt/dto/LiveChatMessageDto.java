package com.dongs.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveChatMessageDto {
    private int LivePostId;
    private String sender;
    private String message;
}
