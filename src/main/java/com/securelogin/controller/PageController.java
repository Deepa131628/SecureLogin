package com.securelogin.controller;

import com.securelogin.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {
    private final AuthService authService;

    public PageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("form") RegistrationForm form,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) return "register";
        try {
            authService.register(form.getUsername().trim(),
                    form.getEmail().trim().toLowerCase(),
                    form.getPassword());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    public static class RegistrationForm {
        @NotBlank @Size(min = 3, max = 50)
        private String username;

        @NotBlank @Email @Size(max = 120)
        private String email;

        @NotBlank @Size(min = 12, max = 72)
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
