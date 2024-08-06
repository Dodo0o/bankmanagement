package com.gorselprog.bankmanagementv3.service;

import com.gorselprog.bankmanagementv3.model.Account;
import com.gorselprog.bankmanagementv3.model.Transaction;
import com.gorselprog.bankmanagementv3.repository.AccountRepository;
import com.gorselprog.bankmanagementv3.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public boolean transferMoney(Long fromAccountId, Long toAccountId, Double amount) {
        Optional<Account> fromAccountOpt = accountRepository.findById(fromAccountId);
        Optional<Account> toAccountOpt = accountRepository.findById(toAccountId);

        if (fromAccountOpt.isPresent() && toAccountOpt.isPresent() && fromAccountOpt.get().getBalance() >= amount) {
            Account fromAccount = fromAccountOpt.get();
            Account toAccount = toAccountOpt.get();

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Transaction kayıt işlemleri
            Transaction withdrawTransaction = new Transaction();
            withdrawTransaction.setAccount(fromAccount);
            withdrawTransaction.setAmount(-amount);
            withdrawTransaction.setType("Withdrawal");
            withdrawTransaction.setTransactionDate(LocalDateTime.now());

            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(toAccount);
            depositTransaction.setAmount(amount);
            depositTransaction.setType("Deposit");
            depositTransaction.setTransactionDate(LocalDateTime.now());

            transactionRepository.save(withdrawTransaction);
            transactionRepository.save(depositTransaction);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            transactionRepository.deleteByAccountId(id);
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
