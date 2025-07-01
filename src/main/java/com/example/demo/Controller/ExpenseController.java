package com.example.demo.Controller;

import com.example.demo.Entity.Expense;
import com.example.demo.Entity.User;
import com.example.demo.Service.ExpenseService;
import com.example.demo.Service.IncomeService;
import com.example.demo.Service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final IncomeService incomeService;

    public ExpenseController(ExpenseService expenseService,
                             UserService userService,
                             IncomeService incomeService) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.incomeService = incomeService;
    }

    @GetMapping("/add")
    public String showAddExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    @PostMapping("/add")
    public String processAddExpense(@ModelAttribute Expense expense,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        if (userDetails == null) return "redirect:/login?error";

        User user = userService.findByEmail(userDetails.getUsername());

        if (expense.getDate() == null) expense.setDate(LocalDate.now());

        double totalIncome = incomeService.findByUser(user).stream()
                .filter(i -> i.getDate().getMonthValue() == expense.getDate().getMonthValue())
                .mapToDouble(i -> i.getAmount())
                .sum();

        double totalExpense = expenseService.getExpensesByUser(user).stream()
                .filter(e -> e.getDate().getMonthValue() == expense.getDate().getMonthValue())
                .mapToDouble(e -> e.getAmount())
                .sum();

        if (totalExpense + expense.getAmount() > totalIncome) {
            model.addAttribute("expense", expense);
            model.addAttribute("errorMessage", "⚠️ Adding this expense will exceed your monthly income.");
            return "add-expense";
        }

        expenseService.addExpense(expense, user);  // This automatically saves the transaction too

        return "redirect:/expense/list";
    }

    @GetMapping("/list")
    public String viewExpenses(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("expenses", expenseService.getExpensesByUser(user));
        }
        return "expense-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            Expense expense = expenseService.getExpensesByUser(user).stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (expense != null) {
            	  expenseService.deleteById(expense.getId());
            }
        }
        return "redirect:/expense/list";
    }
}
