package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.model.Account;
import com.gorselprog.bankmanagementv3.model.Transaction;
import com.gorselprog.bankmanagementv3.model.TransferRequest;
import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.service.AccountService;
import com.gorselprog.bankmanagementv3.service.TransactionService;
import com.gorselprog.bankmanagementv3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/user/{tckimlik}")
    public String getUserAccounts(@PathVariable String tckimlik, Model model) {
        Optional<User> userOpt = userService.findByTckimlik(tckimlik);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Account> accounts = user.getAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("tckimlik", tckimlik);
            return "useraccounts";  // Ensure this template exists
        } else {
            return "redirect:/login/customer";
        }
    }

    @GetMapping("/admin/createaccount")
    public String showCreateAccountForm(Model model) {
        model.addAttribute("account", new Account());
        return "create-account";
    }

    @PostMapping("/admin/createaccount")
    public String createAccount(@ModelAttribute Account account, @RequestParam Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            account.setUser(user);
            accountService.saveAccount(account);
            return "redirect:/accounts/user/" + user.getTckimlik();
        } else {
            return "redirect:/login/customer";
        }
    }

    @GetMapping("/update-balance/{id}")
    public String showUpdateBalanceForm(@PathVariable Long id, Model model) {
        Optional<Account> accountOpt = accountService.getAccountById(id);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            model.addAttribute("account", account);
            return "update-balance";
        } else {
            return "redirect:/admin/accounts";
        }
    }

    @PostMapping("/update-balance")
    public String updateBalance(@ModelAttribute Account account, @RequestParam Double newBalance) {
        Optional<Account> accountOpt = accountService.getAccountById(account.getId());
        if (accountOpt.isPresent()) {
            Account existingAccount = accountOpt.get();
            Double oldBalance = existingAccount.getBalance();
            existingAccount.setBalance(newBalance);
            accountService.saveAccount(existingAccount);

            // Transaction kaydet
            Transaction transaction = new Transaction();
            transaction.setAccount(existingAccount);
            transaction.setAmount(newBalance - oldBalance);
            transaction.setType(newBalance > oldBalance ? "Deposit" : "Withdrawal");
            transactionService.saveTransaction(transaction);

            return "redirect:/accounts/user/" + existingAccount.getUser().getTckimlik();
        } else {
            return "redirect:/accounts";
        }
    }

    @GetMapping("/transactions/{accountId}")
    public String getAccountTransactions(@PathVariable Long accountId, Model model) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    @GetMapping("/admin/accounts")
    public String getAllAccounts(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admin-accounts";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("transfer", new TransferRequest());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute TransferRequest transferRequest, Model model) {
        boolean success = accountService.transferMoney(
            transferRequest.getFromAccountId(),
            transferRequest.getToAccountId(),
            transferRequest.getAmount()
        );
        if (success) {
            return "redirect:/accounts/user/" + transferRequest.getFromUsername();
        } else {
            model.addAttribute("error", "Transfer işlemi başarısız oldu. Yeterli bakiye bulunmamaktadır.");
            return "transfer";
        }
    }
    @GetMapping("/admin/delete")
    public String showDeleteAccountForm(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "delete-account";
    }

    @PostMapping("/admin/delete")
    public String deleteAccount(@RequestParam Long accountId) {
        accountService.deleteAccount(accountId);
        return "redirect:/admin/accounts";
    }
}
