package com.dongs.jwt.domain.product;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import com.dongs.jwt.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	@Column(columnDefinition = "int default 0")
	private int type;
	
	@Column(columnDefinition = "int default 1")
	private int endType;
	 
	 @Column
	 private int price;
	 
	 @Column
	 private int bid;
	 
	 @Column
	 private int bidderId;
	 
	 @Column
	 private int bidLimit;
	 
	 @Column
	 private int minBidUnit;
	 
	 @Column
	 private int endTime;
	 
	@JoinColumn(name = "bidder")
	@ManyToOne
	 private User bidder;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)//하나의 게시글에는 여러개의 댓글이 달릴수있음 ,
	@JsonIgnoreProperties({"post"})													//mappedBy 연관관계의 주인이아니다라는 의미를 가짐  DB에 칼럼을 만들지마세요!, ""안의 값은 reply의 프로퍼티명을 써주면된다.
	@OrderBy("id desc")																	//CascadeType.REMOVE board 지울때 reply도 다날림.
	private List<Photo> photos;
	
	@OneToMany(mappedBy = "post2", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)//하나의 게시글에는 여러개의 댓글이 달릴수있음 ,
	@JsonIgnoreProperties({"post2"})																//mappedBy 연관관계의 주인이아니다라는 의미를 가짐  DB에 칼럼을 만들지마세요!, ""안의 값은 reply의 프로퍼티명을 써주면된다.
	@OrderBy("id desc")																						//CascadeType.REMOVE board 지울때 reply도 다날림.
	private List<Reply> replys;
	 
	@CreationTimestamp
	private Timestamp createDate;
	
	@LastModifiedDate
    private LocalDateTime modifiedDate;
}