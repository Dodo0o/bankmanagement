package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.model.Account;
import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/admin/accounts")
    public String getAllAccounts(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admin-accounts";
    }

    @GetMapping("/admin/update-balance/{accountId}")
    public String showUpdateBalanceForm(@PathVariable Long accountId, Model model) {
        Account account = accountService.getAccountById(accountId).orElse(null);
        if (account != null) {
            model.addAttribute("account", account);
            return "update-balance";
        } else {
            return "redirect:/admin/accounts";
        }
    }

    @PostMapping("/admin/update-balance")
    public String updateBalance(@RequestParam Long accountId, @RequestParam Double newBalance) {
        Account account = accountService.getAccountById(accountId).orElse(null);
        if (account != null) {
            account.setBalance(newBalance);
            accountService.saveAccount(account);
        }
        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null && (user.getRole().equals("ADMIN") || user.getRole().equals("STAFF"))) {
            model.addAttribute("user", user);
            return "admin-home";
        } else {
            return "redirect:/";
        }
    }
}
