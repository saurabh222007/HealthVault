package com.healthvault.controller;

import com.healthvault.dto.RegisterRequest;
import com.healthvault.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AuthController - Handles authentication-related HTTP requests.
 *
 * Key Java Concepts:
 * - @Controller: Marks this as an MVC controller (returns view names, not JSON)
 * - @GetMapping / @PostMapping: Maps HTTP GET and POST requests to methods
 * - @ModelAttribute: Binds form data to a Java object automatically
 * - @Valid + BindingResult: Triggers Bean Validation and captures errors
 * - Model: Passes data from controller to Thymeleaf HTML templates
 * - RedirectAttributes: Passes flash messages after a redirect
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    // ========== HOME ==========
    @GetMapping("/")
    public String home() {
        return "index"; // → templates/index.html
    }

    // ========== LOGIN ==========
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMsg", "You have been logged out successfully.");
        }
        return "login"; // → templates/login.html
    }

    // ========== REGISTER (GET) ==========
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register"; // → templates/register.html
    }

    // ========== REGISTER (POST) ==========
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // If Bean Validation fails, return to registration form with errors
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(request);
            // Flash message shown on the login page after redirect
            redirectAttributes.addFlashAttribute("successMsg",
                    "Account created successfully! Please log in.");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            // Business rule violation (e.g., email already exists)
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }

    // ========== DASHBOARD ROUTER ==========
    // After login, redirect user to their role-specific dashboard
    @GetMapping("/dashboard")
    public String dashboard(org.springframework.security.core.Authentication auth) {
        if (auth == null) return "redirect:/login";

        String role = auth.getAuthorities().iterator().next().getAuthority();
        return switch (role) {
            case "ROLE_PATIENT" -> "redirect:/patient/dashboard";
            case "ROLE_DOCTOR"  -> "redirect:/doctor/dashboard";
            case "ROLE_ADMIN"   -> "redirect:/admin/dashboard";
            default             -> "redirect:/login";
        };
    }
}
