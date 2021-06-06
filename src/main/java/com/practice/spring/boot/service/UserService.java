package com.practice.spring.boot.service;

import org.springframework.stereotype.Service;

import com.practice.spring.boot.entity.User;

@Service
public interface UserService {
	
	User addUser(User user) throws Exception;

}
