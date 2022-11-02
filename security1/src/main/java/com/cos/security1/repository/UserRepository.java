package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// CRUD 함수를 JpaRepositroy가 들고있음 
// @Repository라는 어노테이션없이 사용 가능 -> JpaRepository를 상속받았기 때문 
public interface UserRepository extends JpaRepository<User, Integer>{
	
	
	public User findByUsername(String userName);
}
