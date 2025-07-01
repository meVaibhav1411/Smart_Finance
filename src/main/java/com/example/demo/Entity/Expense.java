package com.example.demo.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String category;

    private LocalDate date;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Expense() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Expense [id=" + id + ", amount=" + amount + ", category=" + category + ", date=" + date + ", note="
				+ note + ", user=" + user + "]";
	}

    
}
