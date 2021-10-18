package com.dongs.jwt.domain.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.dongs.jwt.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY) //IDENTITY : 프로젝트에서 연결된 DB(오라클인가 MySQL인가에 따라)의 넘버링 전략을 따라간다.
	private int id;
	
	@Column(nullable = false, length = 200)
	private String content; //섬머노트 라이브러리를 쓸건데 그대 html 태그가 섞여서 글자 용량이 큼
	
	@ManyToOne
	@JoinColumn(name="postId")
	private Post post2;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	public void update(User user, Post post, String content) {
		setUser(user);
		setPost2(post);
		setContent(content);
	}//dto하면서 reply에 데이터 빌드해주는 메서드임
	
	
}
