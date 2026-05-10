package com.example.householdaccountbook.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "budgetbook")
@Data
public class BudgetBook {
	
		
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
		
	// Usersとの関連
	@ManyToOne
	@JoinColumn(name= "user_id", nullable = false)
	private Users user;
	
	@NotNull(message = "日付を入力してください")
	@Column(nullable = false)
	 private LocalDate date;
	
	@NotBlank(message = "収支を選択して下さい")
	@Column(nullable = false, length = 10)
	private String type;
	
	@NotNull(message = "金額を入力してください")
	@Min(value = 1, message = "金額は0以上の数字を入力してください")
	@Column(nullable = false)
	private Integer amount;
	
	@NotBlank(message = "カテゴリを選択してください")
	@Column(nullable = false, length = 10)
	private String category;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		 if (createdAt == null) {
		      createdAt = LocalDateTime.now();
		 }
		 if (updatedAt == null) {
			  updatedAt = LocalDateTime.now();
		}
				   
	}
	
  // 新規登録の時点では更新日時も同じにしておく

  @PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now();
  }

	
	
		
	

}
