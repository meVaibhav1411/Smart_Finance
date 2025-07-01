package com.example.demo.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class Income {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Double amount;

	    private String source;

	    private LocalDate date;

	    private String note;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;
	    
	    @OneToOne(mappedBy = "income", cascade = CascadeType.ALL, orphanRemoval = true)
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

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
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

		public Income(Double amount, String source, String note, User user) {
			super();
			this.amount = amount;
			this.source = source;
			this.note = note;
			this.user = user;
		}

		public Income() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "Income [id=" + id + ", amount=" + amount + ", source=" + source + ", date=" + date + ", note="
					+ note + ", user=" + user + "]";
		} 
	    
	    


}
