package com.dongs.jwt.domain.liveAuction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.dongs.jwt.domain.product.Photo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.dongs.jwt.domain.user.User;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiveAuctionPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "live_id")
	private int id;
	
	@Column
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;

	@Column(columnDefinition = "integer default 0")
	private int startType;

	@Column(columnDefinition = "integer default 0")
	private int endType;
	 
	@Column
	private int bid;

	@Column
	private int minBidUnit;

	@Column(columnDefinition = "integer default 0")
	 private int bidEntryCount;
	 
	@JoinColumn(name = "bidder_id")
	@ManyToOne
	 private User bidder;

	@OneToMany(mappedBy = "liveAuctionPost", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)//하나의 게시글에는 여러개의 댓글이 달릴수있음 ,
	@JsonIgnoreProperties({"liveAuctionPost"})													//mappedBy 연관관계의 주인이아니다라는 의미를 가짐  DB에 칼럼을 만들지마세요!, ""안의 값은 reply의 프로퍼티명을 써주면된다.
	@OrderBy("id desc")																	//CascadeType.REMOVE board 지울때 reply도 다날림.
	@Column(nullable = false)
	private List<Photo> livePhotos;

	@OneToMany(mappedBy = "liveAuctionPost",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"liveAuctionPost"})
	@OrderBy("id desc")
	private List<LiveChatMessage> messages = new ArrayList<>();

	@CreationTimestamp
	private Timestamp createDate;
	
	@Column
    private LocalDateTime auctionStartDate;

}