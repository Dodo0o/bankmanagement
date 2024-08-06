package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/account/transactions")
    public String viewTransactions(@RequestParam Long accountId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("transactions", transactionService.getTransactionsByAccountId(accountId));
            return "transactions";
        } else {
            return "redirect:/";
        }
    }
}
