package com.dongs.jwt.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
	
	private Map<String, Object> attributes;
	
	public KakaoUserInfo( Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		
		Map<String, Object> temp = (Map)attributes.get("profile");
		return temp.get("id").toString();
	}

	@Override
	public String getProvider() {
		return "kakao";
	}
	
	@Override
	public String getEmail() {
		Map<String, Object> temp = (Map)attributes.get("profile");
		Map<String, Object> temp2 = (Map)temp.get("kakao_account");
		return (String)temp2.get("email");
	}

	@Override
	public String getName() {
		Map<String, Object> temp = (Map)attributes.get("profile");
		Map<String, Object> temp2 = (Map)temp.get("properties");
		return (String)temp2.get("nickname");
		}
	
	@Override
	public String getProfileUrl() {
		Map<String, Object> temp = (Map)attributes.get("profile");
		Map<String, Object> temp2 = (Map)temp.get("properties");
		return (String)temp2.get("profile_image");
		}
}
