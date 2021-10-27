package com.dongs.jwt.service.liveAuctionService;

import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.LiveBidMessageDto;
import com.dongs.jwt.dto.LiveChatMessageDto;
import com.dongs.jwt.repository.PhotoRepository;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.repository.live.LiveAuctionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LiveAuctionPostService {
    private final LiveAuctionPostRepository liveAuctionPostRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;


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

//        int ret = check(principal);//본인이 만든 라이브 경매가 있는지 확인하고 만들어줌
//        if (ret != 0) {
//            //이미 존재하는 방이면 해당 방 번호 리턴
//            return ret;
//        }
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

//    public int check(User principal) {
//        LiveAuctionPost post = liveAuctionPostRepository.findByUser(principal);
//        if (post != null) {
//            if(post.getEndType() != 1) {
//                return post.getId();
//            }else{
//                return 0;
//            }
//        }
//        return 0;
//    }

    @Transactional //동시성 제어
    synchronized public LiveChatMessageDto 라이브경매입찰하기(int liveAuctionPostId, int price, int id) {
        System.out.println("경매입찰 실행");
        LiveChatMessageDto message = new LiveChatMessageDto();
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + "는 존재하지 않습니다."));
        LiveAuctionPost post = liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
        System.out.println(user);
        if (post.getUser().getId() != user.getId()) {
            System.out.println("경매입찰 완전 실행");
            if (post.getBidder() != null) {
                if (post.getBidder().getId() != user.getId()) {
                    if (post.getBid() < price) {//입찰연속으로 못하게 해야함. if문 추가
                        post.setBid(price);
                        post.setBidder(user);
                        message.setLivePostId(liveAuctionPostId);
                        message.setSender(user.getNickname());
                        message.setMessage("ok");
                        return message;
                    } else {
                        message.setLivePostId(liveAuctionPostId);
                        message.setSender(user.getNickname());
                        message.setMessage("low");
                        return message;
                    }
                } else {
                    message.setLivePostId(liveAuctionPostId);
                    message.setSender(user.getNickname());
                    message.setMessage("again");
                    return message;
                }
            } else {
                if (post.getBid() < price) {
                    post.setBid(price);
                    post.setBidder(user);
                    message.setLivePostId(liveAuctionPostId);
                    message.setSender(user.getNickname());
                    message.setMessage("ok");
                    return message;
                }
            }
        }
        message.setLivePostId(liveAuctionPostId);
        message.setSender(user.getNickname());
        message.setMessage("same");
        return message;
    }

    @Transactional
    public String 라이브경매시작(int liveAuctionPostId, User principal) {
        LiveAuctionPost postEntity = liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
        if (postEntity == null) {
            return "fail";
        } else if (postEntity.getUser().getId() != principal.getId()) {
            return "Not Same User";
        } else if (postEntity.getBidEntryCount() <= 0) {
            return "No Bid Entry";
        } else if (postEntity.getStartType() == 1) {
            return "already";
        }
        postEntity.setStartType(1);
        postEntity.setAuctionStartDate(LocalDateTime.now());
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

    @Transactional
    public void 라이브경매입장(int liveAuctionPostId) {
        LiveAuctionPost post = liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
        post.setBidEntryCount(post.getBidEntryCount() + 1);
    }

    @Transactional
    public void 라이브경매퇴장(int liveAuctionPostId) {
        LiveAuctionPost post = liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
        if (post.getBidEntryCount() > 0) {
            post.setBidEntryCount(post.getBidEntryCount() - 1);
        }
    }

    public List<LiveAuctionPost> 나의라이브경매목록(int id) {
        return liveAuctionPostRepository.findByBidderId(id);
    }
}
