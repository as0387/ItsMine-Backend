package com.dongs.jwt.dto;

import com.dongs.jwt.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveBidMessageDto {
    private int LivePostId;
    private String bidder;
    private int price;
}
