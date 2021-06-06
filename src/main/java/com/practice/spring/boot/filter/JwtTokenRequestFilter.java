package com.practice.spring.boot.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.practice.spring.boot.constants.ApplicationConstants;
import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.repository.UserRepository;
import com.practice.spring.boot.utils.JWTtokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JWTtokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		String username = null;
		try {
			final String requestTokenHeader = request.getHeader(ApplicationConstants.X_Auth_Token);
			
			if (requestTokenHeader == null || requestTokenHeader.isEmpty() || !requestTokenHeader.startsWith("Bearer ")) {
	            chain.doFilter(request, response);
	            return;
	        }
			
			String jwtToken = requestTokenHeader.substring(7);
			
			if(jwtTokenUtil.validateToken(jwtToken)){
				if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
					jwtToken = requestTokenHeader.substring(7);
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);	
				} 
		
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		
					User user = userRepository.findUserByEmailId(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
		
					if (jwtTokenUtil.validateTokenWithUser(jwtToken, user)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								user, null, user.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}else {
						chain.doFilter(request, response);
						return;
					}
					
				}
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			String errorMessage = ApplicationConstants.INVALID_TOKEN;
			
			if (e instanceof SignatureException){
				errorMessage = ApplicationConstants.INVALID_JWT_SIGNATURE;
		    } else if (e instanceof MalformedJwtException){
		    	errorMessage = ApplicationConstants.INVALID_TOKEN;
		    } else if (e instanceof ExpiredJwtException){
		    	errorMessage = ApplicationConstants.TOKEN_EXPIRED;
		    } else if (e instanceof UnsupportedJwtException){
		    	errorMessage = ApplicationConstants.TOKEN_UNSUPPORTED;
		    }
			
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
		}
	}
}
