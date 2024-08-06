package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransferController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/transfer")
    public String showTransferForm() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount, Model model) {
        boolean success = accountService.transferMoney(fromAccountId, toAccountId, amount);
        if (success) {
            model.addAttribute("message", "Transfer başarılı!");
        } else {
            model.addAttribute("message", "Transfer başarısız! Yetersiz bakiye.");
        }
        return "transfer";
    }
}
