package com.example.demo.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.Income;
import com.example.demo.Entity.User;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);  // 👈 clear and simple
}
