package com.practice.spring.boot.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.exception.ServiceException;

@Service
public class LoginUser {
	
	public User getUser() throws ServiceException {
		User user = null;
		try {
			user = (User) getAuthentication().getPrincipal();
			
			if(user == null) {
				throw new UsernameNotFoundException("USER_NOT_FOUND");
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return user;
	}
	
	public Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
