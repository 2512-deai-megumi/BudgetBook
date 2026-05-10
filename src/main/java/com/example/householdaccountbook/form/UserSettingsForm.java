package com.example.householdaccountbook.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserSettingsForm {
	@NotBlank(message = "ユーザー名を入力してください")
	private String username;
	
	@Email(message = "メールアドレスの形式で入力してください")
	@NotBlank(message = "メールアドレスを入力してください")
	private String email;

}
