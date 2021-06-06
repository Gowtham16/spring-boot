package com.practice.spring.boot.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.exception.ServiceException;
import com.practice.spring.boot.utils.JWTtokenUtil;

@Service
@Transactional
public class LoginServiceImpl implements LoginService  {
	
	@Autowired
	private JWTtokenUtil jWTtokenUtil;

	@Autowired
	private LoginUser loginUser;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public User onUserLogin(User user) throws ServiceException {
		try {
			Authentication authenticate = authenticate(user.getEmailId(), user.getPassword());
			User vUser = (User) authenticate.getPrincipal();
			vUser.setAuthToken(jWTtokenUtil.generateToken(vUser));
			vUser.setAuthTokenDate(new Date());
			vUser.setLastLoginDate(new Date());
			return vUser;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public User getCurrentUser() throws ServiceException {
		try {
			return loginUser.getUser();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	} 
	
	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
