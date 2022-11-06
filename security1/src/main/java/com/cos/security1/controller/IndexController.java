package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;


@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication, 
			@AuthenticationPrincipal PrincipalDetails userDetails) {	// 의존성 주입 
		System.out.println("/test/login ============");
		PrincipalDetails PrincipalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println("authentication : " + PrincipalDetails.getUser());
		
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {	// 의존성 주입 
		System.out.println("/test/oauth/login ============");
		OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println("authentication : " + oauth2User.getAttributes());
		System.out.println("OAuth2User : " + oauth.getAttributes());
		
		return "OAuth 세션정보 확인하기";
	}
	
	@GetMapping({"","/"})
	public String Index() {
		// 머스테치 기본폴더 src/main/resources/
		// viewResolver 설정 : templates, mustache skip
		return "index";
	}
	
	// OAuth 로그인을 해도 PrincipalDetails
	// 일반 로그인을 해도 PrincipalDetails
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails PrincipalDetails) {
		System.out.println("principalDetails : " + PrincipalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// security configure 작동안
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user.toString());
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user); // 회원가입에는 문제가 없지만 비밀번호 그대로 들어가고 security로 로그온 불가능 
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") // 특정 메소드에 권한을 걸거나 조건을 하나만 걸고 싶을때 사용 
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 메소드가 실행되기전에 실행됨
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
}
