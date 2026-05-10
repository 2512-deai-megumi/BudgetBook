package com.example.householdaccountbook.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.householdaccountbook.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	//メールアドレスで検索(ログイン用)
	Optional<Users> findByEmail(String email);

}
