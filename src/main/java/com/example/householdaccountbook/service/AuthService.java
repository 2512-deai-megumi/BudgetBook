package com.example.householdaccountbook.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.householdaccountbook.dto.RegisterForm;
import com.example.householdaccountbook.entity.Users;
import com.example.householdaccountbook.repository.UsersRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    
    public void register(RegisterForm form) {
    	
    	Users user = new Users();
    	
    	user.setUsername(form.getUsername());
    	user.setEmail(form.getEmail());
    	
    	//パスワードをハッシュ化
    	user.setPassword(passwordEncoder.encode(form.getPassword()));
    	
    	usersRepository.save(user);
    }

}
