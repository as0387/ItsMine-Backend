package com.dongs.jwt.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.config.oauth.provider.FacebookUserInfo;
import com.dongs.jwt.config.oauth.provider.GoogleUserInfo;
import com.dongs.jwt.config.oauth.provider.KakaoUserInfo;
import com.dongs.jwt.config.oauth.provider.NaverUserInfo;
import com.dongs.jwt.config.oauth.provider.OAuth2UserInfo;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//구글로 부터 받은  userRequest 데이터에 대한 후처리되는 함수
	//함수 종료시 @AuthenticationPricipal 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration());//registrationId로 어떤 Oauth로 로그인 했는지 파악
		System.out.println("getAccess Token: " + userRequest.getAccessToken());
		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		//구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-Client라이브러리) -> AccessTorken 요청
		//userReauest정보 -> 회원프로필 받아야함(loadUser함수 호출) -> 구글로부터 회원프로필을 받아준다.
		System.out.println("getAttributes: " + oauth2User);
		
		//강제 회원가입
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo(oauth2User.getAttributes());
		}else {
			System.out.println("우리는 구글과 페이스북, 네이버, 카카오만 지원해요!");
		}
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("잇츠마인일까나");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.roles(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
			System.out.println(userEntity.getUsername());
			System.out.println(userEntity.getProviderId());
			System.out.println(userEntity.getEmail());
		} else {
			System.out.println(" 로그인을 이미 한적이 있습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
