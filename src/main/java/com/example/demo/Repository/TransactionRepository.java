package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Transaction;
import com.example.demo.Entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

	 List<Transaction> findByUser(User user);
}
