package com.example.householdaccountbook.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails {
	private final Long userId;
	private final String email;
	private final String password;
	
	public CustomUserDetails(Long userId, String email, String password) {
		this.userId = userId;
		this.email = email;
		this.password = password;
	}
	

	
	public Long getUserId() {
		return userId;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return Collections.emptyList();
	}
	

}
