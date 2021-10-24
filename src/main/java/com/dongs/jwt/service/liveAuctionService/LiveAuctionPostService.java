package com.dongs.jwt.service.liveAuctionService;

import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.LiveBidMessageDto;
import com.dongs.jwt.repository.PhotoRepository;
import com.dongs.jwt.repository.live.LiveAuctionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LiveAuctionPostService {
    private final LiveAuctionPostRepository liveAuctionPostRepository;
    private final PhotoRepository photoRepository;


    @Transactional
    public LiveAuctionPost openLiveAuctionPost(int liveAuctionPostId) {//1:1채팅
        return liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<LiveAuctionPost> findById(int id) {
        return liveAuctionPostRepository.findById(id);
    }

    @Transactional
    public int 라이브경매상품등록(LiveAuctionPost liveAuctionPost, Map<String, Object> photoList, User principal) {

        int ret = check(principal);//본인이 만든 라이브 경매가 있는지 확인하고 만들어줌
        if (ret != 0) {
            //이미 존재하는 방이면 해당 방 번호 리턴
            return ret;
        }
        List<Integer> fileIdList = (List<Integer>) photoList.get("fileIdList");
        LiveAuctionPost saveLiveAuctionPost = liveAuctionPost;
        saveLiveAuctionPost.setLivePhotos(null);
        List<Photo> photoEntityList = new ArrayList<Photo>();
        for (int i : fileIdList) {
            Number n = (Number) i;
            Long i2 = n.longValue();
            photoEntityList.add(photoRepository.findById(i2).orElseThrow(() -> new IllegalArgumentException(i2 + "는 존재하지 않습니다.")));
        }
        for (Photo p : photoEntityList) {
            p.setLiveAuctionPost(saveLiveAuctionPost);
        }
        saveLiveAuctionPost.setUser(principal);
        saveLiveAuctionPost.setLivePhotos(photoEntityList);
        LiveAuctionPost post = liveAuctionPostRepository.save(saveLiveAuctionPost);

        return post.getId();
    }

    public int check(User principal) {
        LiveAuctionPost post = liveAuctionPostRepository.findByUser(principal);
        if (post != null) {
            return post.getId();
        }
        return 0;
    }

    @Transactional //동시성 제어
     synchronized public String 라이브경매입찰하기(LiveAuctionPost postEntity, int price, User principal) {
        if (postEntity.getUser().getId() != principal.getId()) {
            if(postEntity.getBid() < price){
                postEntity.setBid(price);
                postEntity.setBidder(principal);
                return "ok";
            }
            return "low";
        } else {
            return "same";
        }
    }

    @Transactional
    public String 라이브경매시작(int liveAuctionPostId, User principal) {
        LiveAuctionPost postEntity = liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
        if(postEntity == null){
            return "fail";
        }else if(postEntity.getUser().getId() != principal.getId()){
            return "Not Same User";
        }else if(postEntity.getBidEntryCount() <= 0){
            return "No Bid Entry";
        }
        postEntity.setStartType(1);
        return "ok";
    }

    @Transactional
    public User 라이브경매종료(LiveAuctionPost post) {
        post.setEndType(1);
        return post.getBidder();
    }

    @Transactional(readOnly = true)
    public Page<LiveAuctionPost> 라이브경매목록(Pageable pageable) {
        return liveAuctionPostRepository.findAll(pageable);
    }
}
