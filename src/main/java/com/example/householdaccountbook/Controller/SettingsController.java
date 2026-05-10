package com.example.householdaccountbook.Controller;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import  org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.householdaccountbook.entity.Users;
import com.example.householdaccountbook.form.UserSettingsForm;
import com.example.householdaccountbook.repository.BudgetBookRepository;
import com.example.householdaccountbook.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SettingsController {

	private final UsersRepository usersRepository;
	private final BudgetBookRepository budgetBookRepository;
	
	
	//設定画面表示
	@GetMapping("/settings")
	public String showSettings(Model model, Authentication authentication) {
		
		Users loginUser = getLoginUser(authentication);
	
		UserSettingsForm form = new UserSettingsForm();
		
		form.setUsername(loginUser.getUsername());
		form.setEmail(loginUser.getEmail());
		
		model.addAttribute("settingsForm", form);
		model.addAttribute("username", loginUser.getUsername());
		
		return "settings/index";
		
	}
	
	//更新処理
	@PostMapping("/settings")
	public String updateSettings(@Valid @ModelAttribute("settingsForm") UserSettingsForm settingsForm, BindingResult bindingResult, Authentication authentication, Model model) {
		Users loginUser = getLoginUser(authentication);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("username", loginUser.getUsername());

			return "settings/index";
		}
		loginUser.setUsername(settingsForm.getUsername());
		loginUser.setEmail(settingsForm.getEmail());
		
		usersRepository.save(loginUser);
		
		addUsername(model, loginUser);;
		model.addAttribute("settingsForm", settingsForm);
		model.addAttribute("message", "設定を変更しました");
		
		//設定変更後のログインユーザー情報の取得
			//loginUser = getLoginUser(authentication);
		
		return "settings/index";
		
	}
	
	//退会処理
	@Transactional
	@PostMapping("settings/delete")
	public String deleteAccount(Authentication authentication) {
		
		//ログイン中のユーザーを取得
		Users loginUser = getLoginUser(authentication);
		
		//そのユーザーの家計簿データを削除
		budgetBookRepository.deleteByUser(loginUser);
		
		//ユーザーを削除
		usersRepository.delete(loginUser);
		
		//ログイン状態に戻す
		return "redirect:/login?deleted";
		
	}
	
	//ログインユーザーの取得
	private Users getLoginUser(Authentication authentication) {
		return usersRepository.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("ログインユーザーが見つかりません"));
	}
	
	//ユーザーネームの表示
	private void addUsername(Model model, Users loginUser) {
		model.addAttribute("username", loginUser.getUsername());
	}

}
