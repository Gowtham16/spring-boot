package com.practice.spring.boot.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.practice.spring.boot.filter.JwtTokenRequestFilter;
import com.practice.spring.boot.repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenRequestFilter jwtTokenRequestFilter;
	
	@Autowired
	private UserRepository userRepository;
	
	public SecurityConfiguration(JwtTokenRequestFilter jwtTokenRequestFilter,
			UserRepository userRepository) {
		this.jwtTokenRequestFilter = jwtTokenRequestFilter;
		this.userRepository = userRepository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(username -> userRepository
                .findUserByEmailId(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"))
                );
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http = http.csrf().disable();
		
		http = http
	            .sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and();
		
		http = http
	            .exceptionHandling()
	            .authenticationEntryPoint(authenticationEntryPoint())
	            .and();
		
		http.authorizeRequests()
			.antMatchers("/api/account/**").permitAll()
			.anyRequest().authenticated();
		
		http.addFilterBefore(jwtTokenRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
					AuthenticationException e) throws IOException, ServletException {
				httpServletResponse.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        e.getMessage()
                    );
			}
		};
	}

}
