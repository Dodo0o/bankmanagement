package com.gorselprog.bankmanagementv3.service;

import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.model.Account;
import com.gorselprog.bankmanagementv3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public Optional<User> findByTckimlik(String tckimlik) {
        return userRepository.findByTckimlik(tckimlik);
    }

    public boolean saveUser(User user) {
        if (userRepository.findByTckimlik(user.getTckimlik()).isPresent()) {
            return false; // T.C. Kimlik Numarası zaten mevcut
        }
        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Account> accounts = user.getAccounts();
            for (Account account : accounts) {
                accountService.deleteAccount(account.getId());
            }
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
