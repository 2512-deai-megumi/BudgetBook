package com.example.householdaccountbook.Controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import  org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.householdaccountbook.dto.RegisterForm;
import com.example.householdaccountbook.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	 private final AuthService authService;
	 
	 @GetMapping("/login")
	 public String login() {
		 return "login";
	 }
	 
	 //登録画面表示
	 @GetMapping("/register")
	 public String showRegister(Model model) {
		 model.addAttribute("registerForm", new RegisterForm());
		 return "auth/register";
	 }
	 
	 //登録処理
	 @PostMapping("register")
	 public String register(@Valid @ModelAttribute RegisterForm registerForm, BindingResult bindingResult, Model model) {
		 
		 if (bindingResult.hasErrors()) {
				return "auth/register";
			}
		 
		 authService.register(registerForm);
		 
		 return "redirect:/login";
	 }
}
