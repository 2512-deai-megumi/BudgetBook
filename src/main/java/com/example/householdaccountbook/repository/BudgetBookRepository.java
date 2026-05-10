package com.example.householdaccountbook.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.householdaccountbook.entity.BudgetBook;
import com.example.householdaccountbook.entity.Users;

public interface BudgetBookRepository extends JpaRepository<BudgetBook, Long>{
	//ユーザーごとの家計簿一覧取得
		List<BudgetBook> findByUser(Users user);
		
		Optional<BudgetBook> findByIdAndUser(Long id, Users user);
		
		List<BudgetBook> findByUserAndDateBetweenOrderByDateDesc(Users user, LocalDate startDate, LocalDate endDate);
		
		void deleteByUser(Users user);

}
