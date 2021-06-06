package com.practice.spring.boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practice.spring.boot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	public User findByUserIntId(Long userIntId);
	
	public Optional<User> findUserByEmailId(String emailId);
}
