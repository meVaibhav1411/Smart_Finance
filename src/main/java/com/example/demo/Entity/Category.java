package com.example.demo.Entity;


import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

	  @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	  
	  private String name;
	  
	  @OneToMany(mappedBy = "category")
	    private List<Transaction> transactions;

	public Category(String name) {
		super();
		this.name = name;
	}

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", transactions=" + transactions + "]";
	}
	  
	  
	  
	
}
