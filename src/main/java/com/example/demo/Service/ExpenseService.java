package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          TransactionService transactionService,
                          CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }

    public void addExpense(Expense expense, User user) {
        expense.setUser(user);
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        // Create Transaction
        Transaction txn = new Transaction();
        txn.setAmount(expense.getAmount());
        txn.setType(TransactionType.EXPENSE);
        txn.setDate(expense.getDate());
        txn.setUser(user);
        txn.setDescription(expense.getNote());

        // Set static "Expense" category
        Category expenseCategory = categoryRepository.findByName("Expense");
        if (expenseCategory == null) {
            expenseCategory = new Category("Expense");
            categoryRepository.save(expenseCategory);
        }
        txn.setCategory(expenseCategory);

        // 🔄 Set bidirectional link
        txn.setExpense(expense);
        expense.setTransaction(txn); // <-- important!

        // 💾 Save only expense; transaction will be saved via cascade
        expenseRepository.save(expense);
    }





    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public double getTotalExpense(User user) {
        return expenseRepository.findByUser(user)
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public void deleteById(Long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            expenseRepository.delete(expense); // ✅ Will delete linked transaction via cascade
        }
    }

    


    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public double getExpenseByMonth(User user, YearMonth month) {
        return expenseRepository.findByUser(user).stream()
                .filter(e -> e.getDate() != null &&
                        YearMonth.from(e.getDate()).equals(month))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
