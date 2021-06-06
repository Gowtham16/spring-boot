package com.practice.spring.boot.service;

import org.springframework.stereotype.Service;

import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.exception.ServiceException;

@Service
public interface LoginService {

	public User onUserLogin(User user) throws ServiceException;
	
	public User getCurrentUser() throws ServiceException;

}
