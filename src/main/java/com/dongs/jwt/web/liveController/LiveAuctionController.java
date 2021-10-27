package com.dongs.jwt.web.liveController;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.product.Post;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.dto.ChatListDto;
import com.dongs.jwt.dto.LiveBidMessageDto;
import com.dongs.jwt.service.liveAuctionService.LiveAuctionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LiveAuctionController {

    private final LiveAuctionPostService liveAuctionPostService;



    //라이브 경매 리스트 요청
    @GetMapping("/live-auction/list")
    public ResponseEntity<?> home(
            @PageableDefault(size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {// 페이징 안할거면 지우던가
        return new ResponseEntity<Page<LiveAuctionPost>>(liveAuctionPostService.라이브경매목록(pageable), HttpStatus.OK);
    }
    //라이브 경매 리스트 요청
    @GetMapping("/live-auction/my-list")
    public ResponseEntity<?> myList(@AuthenticationPrincipal PrincipalDetails principal) {// 페이징 안할거면 지우던가
        return new ResponseEntity<List<LiveAuctionPost>>(liveAuctionPostService.나의라이브경매목록(principal.getUser().getId()), HttpStatus.OK);
    }

    //라이브 경매 업로드 요청
    @PostMapping("/live-auction/new")
    public ResponseEntity<?> createLiveAuction(@RequestPart("live-auction-post") LiveAuctionPost liveAuctionPost,
                                            @RequestPart("photo") Map<String, Object> photoList,
                                            @AuthenticationPrincipal PrincipalDetails principal){

        return new ResponseEntity<Integer>( liveAuctionPostService.라이브경매상품등록(liveAuctionPost, photoList, principal.getUser()), HttpStatus.OK);
    }

    //라이브 경매 상세페이지요청
    @GetMapping("/live-auction/detail/{liveAuctionPostId}")
    public ResponseEntity<?> openLiveAuctionPost(@PathVariable int liveAuctionPostId){
        return new ResponseEntity<LiveAuctionPost>(liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId), HttpStatus.OK);
    }

    //라이브 경매 시작 요청
    @GetMapping("/live-auction/start/{liveAuctionPostId}")
    public ResponseEntity<?> startLiveAuction(@PathVariable int liveAuctionPostId,@AuthenticationPrincipal PrincipalDetails principal){
        User user = principal.getUser();
        String result = liveAuctionPostService.라이브경매시작(liveAuctionPostId, user);
        if(!result.equals("ok")){
            if(result.equals("Not Same User")){
                return new ResponseEntity<String>("게시물 작성자가 아닙니다.", HttpStatus.FORBIDDEN);
            }else if(result.equals("No Bid Entry")){
                return new ResponseEntity<String>("아직 경매참여인원이 없습니다.", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<String>("이미시작한경매", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    //라이브 경매 종료 요청
    @GetMapping("/live-auction/end/{liveAuctionPostId}")
    public ResponseEntity<?> endLiveAuction(@PathVariable int liveAuctionPostId,@AuthenticationPrincipal PrincipalDetails principal){
        LiveAuctionPost post  = liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
        if(post.getEndType() != 0){
            if(post.getUser().getId() != principal.getUser().getId()){
                return new ResponseEntity<String>("게시물 작성자가 아닙니다.", HttpStatus.OK);
            }
            return new ResponseEntity<String>("이미 종료한경매", HttpStatus.OK);
        }
        return new ResponseEntity<User>(liveAuctionPostService.라이브경매종료(post), HttpStatus.OK);
    }

//    @PostMapping("/live-auction/bidding/{liveAuctionPostId}") //여기서 경매 입찰 처리
//    public ResponseEntity<?> biding(@RequestBody HashMap<String, Integer> map,
//                                    @PathVariable int liveAuctionPostId,
//                                    @AuthenticationPrincipal PrincipalDetails principal) throws Exception {
//        System.out.println("라이브 입찰  실행");
//        int price = map.get("price");
//        LiveAuctionPost post =  liveAuctionPostService.openLiveAuctionPost(liveAuctionPostId);
//        String result = liveAuctionPostService.라이브경매입찰하기(post, price, principal.getUser());
//        if(result.equals("low")){
//            return new ResponseEntity<String>(result, HttpStatus.FORBIDDEN);
//        }else if(result.equals("same")){
//            return new ResponseEntity<String>(result, HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<String>(result, HttpStatus.OK);
//    }

}
