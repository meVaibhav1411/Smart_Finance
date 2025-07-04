package com.example.demo.Repository;

import com.example.demo.Entity.Expense;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}
