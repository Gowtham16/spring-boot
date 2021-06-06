package com.practice.spring.boot.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "usermtab")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails, Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "user_int_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIntId;
	
	@Column(name = "email_id")
    private String emailId;
	
	@Column(name = "phone_number")
    private String phoneNo;
	
	@Column(name = "user_type")
    private String userType;
	
	@Column(name = "status")
    private String status;
	
	@Column(name = "password")
    private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_date")
    private Date lastLoginDate;
	
	@Column(name = "auth_token")
    private String authToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "auth_token_date")
    private Date authTokenDate;
	
	@Column(name = "name")
    private String userFullName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
    private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_on")
    private Date updateOn;
	
	@Transient
	private boolean isEnabled;
	
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(this.userType));
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.emailId;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getUserIntId() {
		return userIntId;
	}

	public void setUserIntId(Long userIntId) {
		this.userIntId = userIntId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Date getAuthTokenDate() {
		return authTokenDate;
	}

	public void setAuthTokenDate(Date authTokenDate) {
		this.authTokenDate = authTokenDate;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	

}
