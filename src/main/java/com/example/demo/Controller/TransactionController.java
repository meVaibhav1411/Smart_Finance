package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Transaction;
import com.example.demo.Entity.TransactionType;
import com.example.demo.Entity.User;
import com.example.demo.Service.TransactionService;
import com.example.demo.Service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("types", TransactionType.values());
        return "add-transaction";
    }

    @PostMapping("/add")
    public String saveTransaction(@ModelAttribute Transaction transaction,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login?error";
        }

        User user = userService.findByEmail(userDetails.getUsername());
        transaction.setUser(user);

        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }

        transactionService.save(transaction);
        return "redirect:/transactions/list";
    }

    @GetMapping("/list")
    public String listTransactions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login?error";
        }

        User user = userService.findByEmail(userDetails.getUsername());
        List<Transaction> transactions = transactionService.findByUser(user);
        model.addAttribute("transactions", transactions);
        return "transaction-list";
    }
    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable int id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login?error";

        transactionService.deleteById(id);  // This will handle cascade deletion
        return "redirect:/dashboard";  // Or /transactions/list if that fits better
    }
    @GetMapping("/export/monthly")
    public void exportMonthlyCsv(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("month") int month,
                                 @RequestParam("year") int year,
                                 HttpServletResponse response) throws IOException {
        if (userDetails == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        User user = userService.findByEmail(userDetails.getUsername());
        YearMonth yearMonth = YearMonth.of(year, month);

        response.setContentType("text/csv");
        String filename = "transactions-" + yearMonth + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        transactionService.exportTransactionsWithBalance(user, yearMonth, response.getWriter());
    }


}
