package com.example.householdaccountbook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterForm {
	
	@NotBlank(message = "ユーザー名を入力してください")
	private String username;
	
	@Email(message = "メールアドレスの形式で入力してください")
	@NotBlank(message = "メールアドレスを入力してください")
	private String email;
	
	@Size(min = 6, message = "パスワードは6文字以上で入力してください")
	private String password;

}
