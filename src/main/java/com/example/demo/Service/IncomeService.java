package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;

    public IncomeService(IncomeRepository incomeRepository,
                         TransactionService transactionService,
                         CategoryRepository categoryRepository) {
        this.incomeRepository = incomeRepository;
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }

    public List<Income> findByUser(User user) {
        return incomeRepository.findByUser(user);
    }

    public void save(Income income) {
        // Set today's date if not provided
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }

        // Create Transaction
        Transaction txn = new Transaction();
        txn.setAmount(income.getAmount());
        txn.setType(TransactionType.INCOME);
        txn.setDate(income.getDate());
        txn.setUser(income.getUser());
        txn.setDescription(income.getNote() != null ? income.getNote() : income.getSource());

        // Set category
        Category incomeCategory = categoryRepository.findByName("Income");
        if (incomeCategory == null) {
            incomeCategory = new Category("Income");
            categoryRepository.save(incomeCategory);
        }
        txn.setCategory(incomeCategory);

        // 🔁 Bidirectional Link
        txn.setIncome(income);
        income.setTransaction(txn);

        // 💾 Save only income; transaction will be saved via cascade
        incomeRepository.save(income);
    }


    public double getTotalIncome(User user) {
        return incomeRepository.findByUser(user)
                .stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    public void deleteById(Long id) {
        Income income = incomeRepository.findById(id).orElse(null);
        if (income != null) {
            incomeRepository.delete(income); // 💥 Transaction auto-deletes via orphanRemoval
        }
    }



    public Income findById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public double getIncomeByMonth(User user, YearMonth month) {
        return incomeRepository.findByUser(user).stream()
                .filter(i -> i.getDate() != null &&
                        YearMonth.from(i.getDate()).equals(month))
                .mapToDouble(Income::getAmount)
                .sum();
    }
}
