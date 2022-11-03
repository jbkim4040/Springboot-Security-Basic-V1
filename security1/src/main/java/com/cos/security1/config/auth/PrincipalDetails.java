package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// security가 login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 security session을 만들어준다. (Security ContextHolder에 session 정보를 저장함)
// 오브젝트 -> Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 함 
// User 오브젝트타입 -> UserDetails 타입 객체 

// Security Session -> Authentication -> UserDetails (PrincipalDetails)

@Data
@Getter
@Setter
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; // 콤포지션 
	private Map<String, Object> attributes;
	
	// 일반 로그인 
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// OAuth 로그인 
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public Map<String, Object> getAttributes(){
		return null;
	}
	
	@Override
	public String getName() {
		return null;
	}
}
