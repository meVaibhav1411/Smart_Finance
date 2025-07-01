package com.example.demo.Controller;

import com.example.demo.Entity.Income;
import com.example.demo.Entity.User;
import com.example.demo.Service.IncomeService;
import com.example.demo.Service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String showAddIncomeForm(Model model) {
        model.addAttribute("income", new Income());
        return "add-income";
    }

    @PostMapping("/add")
    public String processAddIncome(@ModelAttribute Income income, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            income.setUser(user);
            if (income.getDate() == null) income.setDate(LocalDate.now());
            incomeService.save(income);  // Handles both income + transaction creation
        }
        return "redirect:/income/list";
    }

    @GetMapping("/list")
    public String viewIncomeList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername());
            model.addAttribute("incomes", incomeService.findByUser(user));
        }
        return "income-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login?error";

        User user = userService.findByEmail(userDetails.getUsername());
        Income income = incomeService.findById(id);
        if (income != null && income.getUser().getId() == user.getId()) {
            incomeService.deleteById(id);
        }
        return "redirect:/income/list";
    }
}
