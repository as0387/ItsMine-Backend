package com.dongs.jwt.domain.product;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class NomalAuctionPost {
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
	
	@Column
    private String imageUrl;
	
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
	 
	@CreationTimestamp
	private Timestamp createDate;
	
	@LastModifiedDate
    private LocalDateTime modifiedDate;
}