package com.example.demo.Entity;


import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name= "transactions")
public class Transaction {

	      @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	      
	      private String description;

	      private double amount;

	      @Enumerated(EnumType.STRING)
	      private TransactionType type;  

	      private LocalDate date;

	      @ManyToOne
	      @JoinColumn(name = "user_id")
	      private User user;

	      @ManyToOne
	      @JoinColumn(name = "category_id")
	      private Category category;
	      
	      @OneToOne(cascade = CascadeType.ALL)
	      @JoinColumn(name = "expense_id", referencedColumnName = "id")
	      private Expense expense;

	      
	      public Expense getExpense() {
	    	    return expense;
	    	}

	    	public void setExpense(Expense expense) {
	    	    this.expense = expense;
	    	}
	    	@OneToOne
	    	@JoinColumn(name = "income_id", referencedColumnName = "id", nullable = true)
	    	private Income income;

	    	public Income getIncome() {
	    	    return income;
	    	}

	    	public void setIncome(Income income) {
	    	    this.income = income;
	    	}




	     

		public Transaction(double amount, TransactionType type) {
			super();
			this.amount = amount;
			this.type = type;
		}

		public Transaction() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public TransactionType getType() {
			return type;
		}

		public void setType(TransactionType type) {
			this.type = type;
		}

		public LocalDate getDate() {
			return date;
		}

		public void setDate(LocalDate date) {
			this.date = date;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		@Override
		public String toString() {
			return "Transaction [id=" + id + ", description=" + description + ", amount=" + amount + ", type=" + type
					+ ", date=" + date + ", user=" + user + ", category=" + category + "]";
		}


	
}
