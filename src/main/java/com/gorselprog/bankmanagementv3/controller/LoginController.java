package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showLoginOptions() {
        return "index";
    }

    @GetMapping("/login/customer")
    public String showCustomerLoginForm() {
        return "login-customer";
    }

    @PostMapping("/login/customer")
    public String customerLogin(@RequestParam String tckimlik, @RequestParam String password, HttpSession session, Model model) {
        Optional<User> userOpt = userService.findByTckimlik(tckimlik);
        if (userOpt.isPresent() && userService.checkPassword(password, userOpt.get().getPassword()) && userOpt.get().getRole().equals("CUSTOMER")) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Geçersiz T.C. Kimlik Numarası veya Şifre");
            return "login-customer";
        }
    }

    @GetMapping("/login/staff")
    public String showStaffLoginForm() {
        return "login-staff";
    }

    @PostMapping("/login/staff")
    public String loginStaff(@RequestParam String tckimlik, @RequestParam String password, HttpSession session, Model model) {
        Optional<User> userOpt = userService.findByTckimlik(tckimlik);
        if (userOpt.isPresent() && userService.checkPassword(password, userOpt.get().getPassword()) && (userOpt.get().getRole().equals("STAFF") || userOpt.get().getRole().equals("ADMIN"))) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/admin/home";
        } else {
            model.addAttribute("error", "Geçersiz T.C. Kimlik Numarası veya Şifre");
            return "login-staff";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
