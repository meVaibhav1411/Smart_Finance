package com.example.demo.Controller;

import com.example.demo.DTO.TransactionDTO;
import com.example.demo.Entity.Expense;
import com.example.demo.Entity.Transaction;
import com.example.demo.Entity.User;
import com.example.demo.Service.ExpenseService;
import com.example.demo.Service.IncomeService;
import com.example.demo.Service.TransactionService;
import com.example.demo.Service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final UserService userService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final TransactionService transactionService;

    public DashboardController(UserService userService,
                               IncomeService incomeService,
                               ExpenseService expenseService,
                               TransactionService transactionService) {
        this.userService = userService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.transactionService = transactionService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login?error";
        }

        User user = userService.findByEmail(userDetails.getUsername());

        // Summary Cards
        double totalIncome = incomeService.getTotalIncome(user);
        double totalExpense = expenseService.getTotalExpense(user);
        double balance = totalIncome - totalExpense;

        model.addAttribute("email", user.getEmail());
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", balance);

        // Bar Chart
        List<String> chartLabels = new ArrayList<>();
        List<Double> incomeData = new ArrayList<>();
        List<Double> expenseData = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            Month month = Month.of(i);
            YearMonth ym = YearMonth.of(LocalDate.now().getYear(), i);
            chartLabels.add(month.name().substring(0, 3));

            incomeData.add(incomeService.getIncomeByMonth(user, ym));
            expenseData.add(expenseService.getExpenseByMonth(user, ym));
        }

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("incomeData", incomeData);
        model.addAttribute("expenseData", expenseData);

        // Donut Chart
        YearMonth currentMonth = YearMonth.now();
        double currentIncome = incomeService.getIncomeByMonth(user, currentMonth);

        List<Expense> currentExpenses = expenseService.getExpensesByUser(user).stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                .collect(Collectors.toList());

        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense e : currentExpenses) {
            categoryTotals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }

        List<String> donutLabels = new ArrayList<>();
        List<Double> donutData = new ArrayList<>();
        List<String> donutColors = new ArrayList<>();

        donutLabels.add("Income");
        donutData.add(currentIncome);
        donutColors.add("#198754");

        String[] colorPalette = { "#dc3545", "#fd7e14", "#0d6efd", "#ffc107", "#6f42c1", "#20c997" };
        int colorIndex = 0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            donutLabels.add(entry.getKey());
            donutData.add(entry.getValue());
            donutColors.add(colorPalette[colorIndex % colorPalette.length]);
            colorIndex++;
        }

        double totalExpenseThisMonth = currentExpenses.stream().mapToDouble(Expense::getAmount).sum();
        double currentMonthBalance = currentIncome - totalExpenseThisMonth;

        model.addAttribute("donutLabels", donutLabels);
        model.addAttribute("donutData", donutData);
        model.addAttribute("donutColors", donutColors);
        model.addAttribute("currentMonthBalance", currentMonthBalance);

        // Convert to DTOs for table display
        List<Transaction> transactions = transactionService.findByUser(user);
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getType().toString(),
                        t.getAmount(),
                        t.getDescription(),
                        t.getDate().format(formatter)
                ))
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactionDTOs);

        return "dashboard";
    }
}
