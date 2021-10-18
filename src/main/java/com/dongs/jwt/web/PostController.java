package com.dongs.jwt.web;

import java.io.File;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.product.MD5Generator;
import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.domain.product.Post;
import com.dongs.jwt.dto.PhotoDto;
import com.dongs.jwt.dto.ReplySaveRequestDto;
import com.dongs.jwt.dto.ResponseDto;
import com.dongs.jwt.service.PhotoService;
import com.dongs.jwt.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final PhotoService photoService;

	// 모두 접근 가능
	@GetMapping({ "/products" })
	public ResponseEntity<?> home(
			@PageableDefault(size = 30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {// 페이징 안할거면
																											// 지우던가
		String path = System.getProperty("user.dir");
		System.out.println(path);
		return new ResponseEntity<Page<Post>>(postService.글목록(pageable), HttpStatus.OK);
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> detail(@PathVariable int id) {
		return new ResponseEntity<Post>(postService.글상세(id), HttpStatus.OK);
	}

	// JWT 토큰만 있으면 접근 가능
	@PostMapping("/post")
	public ResponseEntity<?> save(@RequestBody Post post, @AuthenticationPrincipal PrincipalDetails principal) {
		postService.글쓰기(post, principal.getUser());
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}

	@PostMapping("/nomalAuctionPost")
	public ResponseEntity<?> auctionSave(@RequestPart("post1") Post post,
			@RequestPart("post2") Map<String, Object> photoList, @AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println(photoList.get("fileIdList").getClass().getName());
		postService.일반경매상품등록(post, photoList, principal.getUser());
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}

	// JWT 토큰으로 동일인 체크 후 접근 가능
	@DeleteMapping("/post/{id}")
	public ResponseEntity<?> delete(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principal) {
		int result = postService.글삭제(id, principal.getUser());

		if (result == 1) {
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}
	}

	// JWT 토큰으로 동일인 체크 후 접근 가능
	@PutMapping("/post/{id}")
	public ResponseEntity<?> update(@RequestBody Post post, @PathVariable int id,
			@AuthenticationPrincipal PrincipalDetails principal) {
		int result = postService.글수정(post, id, principal.getUser());

		if (result == 1) {
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}
	}

	@PutMapping("/bidPost/{id}")
	public ResponseEntity<?> bidPost(@RequestBody Post post, @PathVariable int id) {
		int result = postService.입찰하기(post, id);
		if (result == 1) {
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}
	}

	// image관련 mapping

	@PostMapping("/image")
	public Long write(@RequestPart("image") MultipartFile[] files) {
		for (MultipartFile file : files) {
			System.out.println(file);
		}
		;
		Long photoIds = null;
		for (MultipartFile file : files) {
			try {
				System.out.println(file);
				String origFilename = file.getOriginalFilename();

				// 파일의 확장자 추출
				String originalFileExtension = null;
				String contentType = file.getContentType();

				// 확장자가 jpeg, png인 파일들만 받아서 처리
				if (contentType.contains("image/jpeg"))
					originalFileExtension = ".jpg";
				else if (contentType.contains("image/png"))
					originalFileExtension = ".png";

				String filename = new MD5Generator(origFilename).toString() + System.nanoTime() + originalFileExtension;

				String path = System.getProperty("user.dir");
				System.out.println(path);
				/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
				String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\upload";
				/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
				if (!new File(savePath).exists()) {
					try {
						new File(savePath).mkdir();
					} catch (Exception e) {
						e.getStackTrace();
					}
				}
				String filePath = savePath + "\\" + filename;
				String imageUrl = "/upload/" + filename;
				file.transferTo(new File(filePath));

				PhotoDto photoDto = new PhotoDto();
				photoDto.setOrigFilename(origFilename);
				photoDto.setFilename(filename);
				photoDto.setImageUrl(imageUrl);
				Photo savePhoto = photoService.saveFile(photoDto);
				photoIds = savePhoto.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return photoIds;
	}

	@PostMapping("/image/profile")
	public String writeProfile(@RequestParam("image") MultipartFile files) {
		String imageUrl = null;
		try {
			String origFilename = files.getOriginalFilename();

			// 파일의 확장자 추출
			String originalFileExtension = null;
			String contentType = files.getContentType();

			// 확장자가 jpeg, png인 파일들만 받아서 처리
			if (contentType.contains("image/jpeg"))
				originalFileExtension = ".jpg";
			else if (contentType.contains("image/png"))
				originalFileExtension = ".png";

			String filename = new MD5Generator(origFilename).toString() + System.nanoTime() + originalFileExtension;

			String path = System.getProperty("user.dir");
			System.out.println(path);
			/* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
			String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\upload\\profile";
			/* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdir();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			String filePath = savePath + "\\" + filename;
			imageUrl = "/upload/profile/" + filename;
			files.transferTo(new File(filePath));

			PhotoDto photoDto = new PhotoDto();
			photoDto.setOrigFilename(origFilename);
			photoDto.setFilename(filename);
			photoDto.setImageUrl(imageUrl);

			photoService.saveFile(photoDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageUrl;
	}

	@PostMapping("/product/{postId}/reply")
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {

		postService.댓글쓰기(replySaveRequestDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@DeleteMapping("/product/{postId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId) {
		System.out.println("삭제 시작");
		postService.댓글삭제하기(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

}
