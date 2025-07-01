package com.example.demo.Entity;

import java.util.List;
import jakarta.persistence.*;



@Entity
@Table(name= "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	private String username;
	 @Column(nullable = false, unique = true)
	private String email;
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Expense> expenses;
	
	  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	    private List<Transaction> transactions;
	  
	  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	  private List<Income> incomes;

	  public List<Income> getIncomes() {
	      return incomes;
	  }

	  public void setIncomes(List<Income> incomes) {
	      this.incomes = incomes;
	  }

//	public User(String username, String password) {
//		super();
//		this.username = username;
//		this.password = password;
//	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	@Override
//	public String toString() {
//		return "User [id=" + id + ", username=" + username + ", password=" + password + ", transactions=" + transactions
//				+ "]";
//	}
	
	  
	

}
