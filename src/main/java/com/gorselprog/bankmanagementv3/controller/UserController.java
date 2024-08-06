package com.gorselprog.bankmanagementv3.controller;

import com.gorselprog.bankmanagementv3.model.User;
import com.gorselprog.bankmanagementv3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        user.setRole("CUSTOMER");
        boolean isUserSaved = userService.saveUser(user);
        if (isUserSaved) {
            model.addAttribute("successMessage", "Kayıt başarılı!");
            return "redirect:/login/customer";
        } else {
            model.addAttribute("errorMessage", "Bu T.C. Kimlik Numarası zaten kullanılıyor.");
            return "register";
        }
    }

    @GetMapping("/register/admin")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register-admin";
    }

    @PostMapping("/register/admin")
    public String registerAdmin(@ModelAttribute User user, Model model) {
        user.setRole("ADMIN");
        boolean isAdminSaved = userService.saveUser(user);
        if (isAdminSaved) {
            model.addAttribute("successMessage", "Yönetici kaydı başarılı!");
            return "redirect:/login/staff";
        } else {
            model.addAttribute("errorMessage", "Bu T.C. Kimlik Numarası zaten kullanılıyor.");
            return "register-admin";
        }
    }
    @GetMapping("/user/details/{tckimlik}")
    public String getUserDetails(@PathVariable String tckimlik, Model model) {
        User user = userService.findByTckimlik(tckimlik).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            return "admin-user-details"; // Şablon adını burada doğru şekilde kullanıyoruz.
        } else {
            model.addAttribute("errorMessage", "Kullanıcı bulunamadı");
            return "error";
        }
    }
    @GetMapping("/admin/delete/user")
    public String showDeleteUserForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "delete-user";
    }

    @PostMapping("/admin/delete/user")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/accounts";
    }
}
