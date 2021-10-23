package com.dongs.jwt.domain.liveAuction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.dongs.jwt.domain.chat.ChatMessage;
import com.dongs.jwt.domain.product.Photo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dongs.jwt.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@Column(columnDefinition = "int default 1")
	private int type;
	
	@Column(columnDefinition = "int default 1")
	private int endType;
	 
	 @Column
	 private int bid;
	 private int bidderId;
	 private int bidLimit;
	 private int endTime;
	 
	@JoinColumn(name = "bidder")
	@ManyToOne
	 private User bidder;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)//하나의 게시글에는 여러개의 댓글이 달릴수있음 ,
	@JsonIgnoreProperties({"post"})													//mappedBy 연관관계의 주인이아니다라는 의미를 가짐  DB에 칼럼을 만들지마세요!, ""안의 값은 reply의 프로퍼티명을 써주면된다.
	@OrderBy("id desc")																	//CascadeType.REMOVE board 지울때 reply도 다날림.
	private List<Photo> photos;

	@OneToMany(mappedBy = "liveAuctionPost",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"liveAuctionPost"})
	@OrderBy("id desc")
	private List<ChatMessage> messages = new ArrayList<>();
	 
	@CreationTimestamp
	private Timestamp createDate;
	
	@LastModifiedDate
    private LocalDateTime modifiedDate;

}