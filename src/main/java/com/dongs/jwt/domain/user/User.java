package com.dongs.jwt.domain.user;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique = true)
	private String username; 
	
	@Column(nullable = false)
	private String password;
	private String email;
	private String nickname;
	private String phone;
	private String roles;//USER,ADMIN
	
	@Column
	private String provider;
	@Column
	private String providerId;
	
	@Column
    private String profileImageUrl;
	
	@CreationTimestamp
	private Timestamp createDate;
}

