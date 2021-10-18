package com.dongs.jwt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.domain.product.Post;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.ReplySaveRequestDto;
import com.dongs.jwt.repository.PhotoRepository;
import com.dongs.jwt.repository.PostRepository;
import com.dongs.jwt.repository.ReplyRepository;
import com.dongs.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final PhotoRepository photoRepository;
	private final ReplyRepository replyRepository;
	
	@Transactional
	public void 글쓰기(Post post, User principal) {
		post.setUser(principal);
		postRepository.save(post);
	}
	
	@Transactional
	public void 일반경매상품등록(Post post, Map<String, Object>  photoList, User principal) {
		
		List<Integer> fileIdList = (List<Integer>)photoList.get("fileIdList");
		Post post2 = post;
		post2.setPhotos(null);
		List<Photo> photoEntityList = new ArrayList<Photo>();
		for(int i : fileIdList) {
			Number n = (Number)i;
			Long i2 = n.longValue();
			photoEntityList.add(photoRepository.findById(i2).orElseThrow(()-> new IllegalArgumentException(i2+"는 존재하지 않습니다.")));
		}
		for(Photo p : photoEntityList) {
			p.setPost(post2);
		}
		post2.setUser(principal);
		post2.setPhotos(photoEntityList);
		postRepository.save(post2);
	}
	
	@Transactional(readOnly = true)
	public Page<Post> 글목록(Pageable pageable) {
		return postRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Post 글상세(int id) {
		return postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));
	}
	
	@Transactional
	public int 글수정(Post post, int id, User principal) {
		Post postEntity = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));
		
		if(postEntity.getUser().getId() == principal.getId()) {
			postEntity.setTitle(post.getTitle());
			postEntity.setDescription(post.getDescription());
			postEntity.setPrice(post.getPrice());
			return 1;
		}else {
			return 0;
		}
	}
	
//	@Transactional
//	public int 입찰하기(Post post, int id) {
//		Post postEntity = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));
//			postEntity.setBid(post.getBid());
//			postEntity.setBidderId(post.getBidderId());
//			return 1;
//	}
	
	@Transactional
	public int 입찰하기(Post post, int id) {
		Post postEntity = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));
		User BidderEntity = userRepository.findById(post.getBidderId()).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));;
			postEntity.setBid(post.getBid());
			postEntity.setBidder(BidderEntity);
			return 1;
	}
	
	@Transactional
	public int 글삭제(int id, User principal) {
		// 동일인 체크
		Post postEntity = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(id+"는 존재하지 않습니다."));
		if(postEntity.getUser().getId() == principal.getId()) {
			postRepository.deleteById(id);
			return 1;
		}else {
			return 0;
		}
	}
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getPostId(), replySaveRequestDto.getContent());
	}
	
	@Transactional
	public void 댓글삭제하기(int replyId) {
		replyRepository.deleteById(replyId);		
	}
}
