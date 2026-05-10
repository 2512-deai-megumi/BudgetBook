package com.example.householdaccountbook.Controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.householdaccountbook.entity.BudgetBook;
import com.example.householdaccountbook.entity.Users;
import com.example.householdaccountbook.repository.BudgetBookRepository;
import com.example.householdaccountbook.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/budgetbook")
public class BudgetBookController {
	
	private final BudgetBookRepository budgetBookRepository;
	private final UsersRepository usersRepository;
	private static final String TYPE_INCOME = "収入";
	private static final String TYPE_OUTCOME = "支出";
	private static final String VIEW_LIST = "budgetbook/list";
	private static final String VIEW_NEW = "budgetbook/new";
	private static final String VIEW_EDIT = "budgetbook/edit";
	private static final String VIEW_ARCHIVE = "budgetbook/archive";
	private static final String REDIRECT_LIST = "redirect:/budgetbook";
	
	//一覧表示：ログイン中ユーザーのデータだけ出す
	@GetMapping
	public String list(Model model, Authentication authentication) {
		
		Users loginUser = getLoginUser(authentication);
		
		LocalDate now = LocalDate.now();
		
		LocalDate startDate = now.withDayOfMonth(1);
		
		LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
		
		List<BudgetBook> items = budgetBookRepository.findByUserAndDateBetweenOrderByDateDesc(loginUser, startDate, endDate);
		
		//支出額、収支の計算
		addTotals(model, items);
		
		//ユーザーネームの表示
		addUsername(model, loginUser);
		
		//1か月分のデータの表示
		model.addAttribute("items", budgetBookRepository.findByUserAndDateBetweenOrderByDateDesc(loginUser, startDate, endDate));
		
		//支出金額の表示
		
		return VIEW_LIST;
	} 
	
	//家計簿追加画面表示
	@GetMapping("/new")
	public String newForm(Model model,  Authentication authentication) {
		BudgetBook form = new BudgetBook();
		Users loginUser = getLoginUser(authentication);
		
		//デフォルトの日付を格納
		form.setDate(LocalDate.now());
		//ユーザーネームの表示
		addUsername(model, loginUser);;
		
		model.addAttribute("budgetbook",form);
		return VIEW_NEW;
	}
	
	//登録処理
	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("budgetbook") BudgetBook budgetbook, BindingResult bindingResult, Authentication authentication, Model model) {
		Users loginUser = getLoginUser(authentication);
		
		if (bindingResult.hasErrors()) {
			addUsername(model, loginUser);
			model.addAttribute("budgetbook", budgetbook);
			return VIEW_NEW;
		}
		
		 // ログイン中ユーザーを紐づける
		budgetbook.setUser(loginUser);	
		budgetBookRepository.save(budgetbook);
		
		
		return REDIRECT_LIST;
	}
	
	//編集画面表示
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
		
		Users loginUser = getLoginUser(authentication);
		
		 Optional<BudgetBook> budgetbookOpt = budgetBookRepository.findByIdAndUser(id, loginUser);
		 //存在しない編集IDのページに遷移しようとしたら一覧画面に遷移
		 if (budgetbookOpt.isEmpty()) {
			 redirectAttributes.addFlashAttribute("errorMessage", "データが見つかりません");
			 return REDIRECT_LIST;
		 }
		
		
		
		BudgetBook budgetbook = budgetBookRepository.findByIdAndUser(id, loginUser).orElseThrow(() -> new RuntimeException("データが見つかりません"));
		
		
		model.addAttribute("budgetbook", budgetbook);
		//ユーザーネームの表示
		addUsername(model, loginUser);;
		
		
		return VIEW_EDIT;
	}
	
	//更新処理
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("budgetbook") BudgetBook form, BindingResult bindingResult ,@PathVariable Long id, Model model, Authentication authentication) {
		
		Users loginUser = getLoginUser(authentication);
	
		BudgetBook budgetbook = budgetBookRepository.findByIdAndUser(id, loginUser).orElseThrow(() -> new RuntimeException("データが見つかりません"));
		
		if (bindingResult.hasErrors()) {
			form.setId(id);
			addUsername(model, loginUser);;
			model.addAttribute("budgetbook", form);
			return VIEW_EDIT;
		}
		
		//更新したい項目だけ上書き
		budgetbook.setDate(form.getDate());
		budgetbook.setType(form.getType());
		budgetbook.setCategory(form.getCategory());
		budgetbook.setAmount(form.getAmount());
		
		
		budgetBookRepository.save(budgetbook);
		
		return REDIRECT_LIST;
	
	}
	
	//削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, Model model, Authentication authentication) {
		
		Users loginUser = getLoginUser(authentication);
		
		BudgetBook budgetbook = budgetBookRepository.findByIdAndUser(id, loginUser).orElseThrow(() -> new RuntimeException("データが見つかりません"));
		
		budgetBookRepository.delete(budgetbook);
		
		return REDIRECT_LIST;
		
	}
	
	@GetMapping("/archive")
	public String archive(@RequestParam(required = false) String month, Model model, Authentication authentication) {
		
		Users loginUser = getLoginUser(authentication);
		
		YearMonth yearMonth;
		
		//monthが未指定なら先月を初期表示
		if(month == null || month.isBlank()) {
			yearMonth = YearMonth.now().minusMonths(1);
		}else {
			yearMonth = YearMonth.parse(month);
		}
		
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();
		
		List<BudgetBook> items = budgetBookRepository.findByUserAndDateBetweenOrderByDateDesc(loginUser, startDate, endDate);
		
		//支出額、収支の計算
		addTotals(model, items);
		
		//ユーザーネームの表示
		addUsername(model, loginUser);
		
		model.addAttribute("items", items);
		model.addAttribute("selectedMonth", yearMonth.toString());
		
		
		return  VIEW_ARCHIVE;
	}
	
	//ログインユーザーの取得
	private Users getLoginUser(Authentication authentication) {
		return usersRepository.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("ログインユーザーが見つかりません"));
	}
	
	//ユーザーネームの表示
	private void addUsername(Model model, Users loginUser) {
		model.addAttribute("username", loginUser.getUsername());
	}
	
	//収入、支出合計の計算
	private void addTotals(Model model, List<BudgetBook> items) {
		int incomeTotal = 0;
		int outcomeTotal = 0;
		
		//支出額、収支の計算
		for(BudgetBook item : items) {
			if(TYPE_INCOME.equals(item.getType())) {
				incomeTotal += item.getAmount();
			} else if(TYPE_OUTCOME.equals(item.getType())) {
				outcomeTotal += item.getAmount();
			}
		}
						
		//それぞれの合計金額の表示
		model.addAttribute("incomeTotal", incomeTotal);
		model.addAttribute("outcomeTotal", outcomeTotal);
	}
}


