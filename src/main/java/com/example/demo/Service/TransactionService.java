package com.example.demo.Service;

import java.io.IOException;
import java.io.Writer;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Expense;
import com.example.demo.Entity.Transaction;
import com.example.demo.Entity.TransactionType;
import com.example.demo.Entity.User;
import com.example.demo.Repository.TransactionRepository;

@Service
public class TransactionService {
	  @Autowired
	    private TransactionRepository transactionRepository;

	    public void save(Transaction transaction) {
	        transactionRepository.save(transaction);
	    }

	    public List<Transaction> findByUser(User user) {
	        return transactionRepository.findByUser(user);
	    }
	    public void deleteByUserAndAmountAndType(User user, double amount, TransactionType type) {
	        List<Transaction> transactions = transactionRepository.findByUser(user);
	        for (Transaction txn : transactions) {
	            if (txn.getAmount() == amount && txn.getType() == type && txn.getUser().equals(user)) {
	                transactionRepository.delete(txn);
	                break; // delete only one matching transaction
	            }
	        }
	    }
	   
	    public void deleteById(int id) {
	        Transaction txn = transactionRepository.findById(id).orElse(null);
	        if (txn != null) {
	            transactionRepository.delete(txn);  // This will delete linked income/expense via cascade
	        }
	    }
 



	    
	    public void delete(Transaction transaction) {
	        transactionRepository.delete(transaction);
	    }
	    public void exportTransactionsWithBalance(User user, YearMonth month, Writer writer) throws IOException {
	        List<Transaction> transactions = findByUser(user).stream()
	                .filter(txn -> txn.getDate() != null && YearMonth.from(txn.getDate()).equals(month))
	                .sorted(Comparator.comparing(Transaction::getDate))
	                .toList();

	        double incomeTotal = 0, expenseTotal = 0;

	        writer.write("Date,Type,Amount,Description\n");

	        for (Transaction txn : transactions) {
	            writer.write(String.format("%s,%s,%.2f,%s\n",
	                    txn.getDate(),
	                    txn.getType(),
	                    txn.getAmount(),
	                    txn.getDescription().replace(",", " ")
	            ));

	            if (txn.getType() == TransactionType.INCOME)
	                incomeTotal += txn.getAmount();
	            else if (txn.getType() == TransactionType.EXPENSE)
	                expenseTotal += txn.getAmount();
	        }

	        double balance = incomeTotal - expenseTotal;

	        writer.write("\nRemaining Balance:,,," + String.format("₹%.2f", balance) + "\n");
	    }



}
