package com.practice.spring.boot.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.exception.ServiceException;
import com.practice.spring.boot.repository.UserRepository;
import com.practice.spring.boot.utils.JWTtokenUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private JWTtokenUtil jWTtokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User addUser(User user) throws Exception {
		
		try {
			String hashedPassword = passwordEncoder.encode(user.getPassword()); 
			Date currentDate = new Date();
			
			user.setPassword(hashedPassword);
			user.setAuthToken(jWTtokenUtil.generateToken(user));
			user.setAuthTokenDate(currentDate);
			user.setLastLoginDate(currentDate);
			user.setCreatedOn(currentDate);
			user.setUpdateOn(currentDate);
			user = userRepository.save(user);
			
			return user;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
	}
}
