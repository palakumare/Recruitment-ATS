package com.ats.recruitment.controller;

import com.ats.recruitment.dto.HrRegistrationDto;
import com.ats.recruitment.dto.UserRegistrationDto;
import com.ats.recruitment.exception.BadRequestException;
import com.ats.recruitment.exception.DuplicateResourceException;
import com.ats.recruitment.service.HRService;
import com.ats.recruitment.service.JobService;
import com.ats.recruitment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final HRService hrService;
    private final JobService jobService;

    public AuthController(UserService userService, HRService hrService, JobService jobService) {
        this.userService = userService;
        this.hrService = hrService;
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentJobs", jobService.getActiveJobs());
        model.addAttribute("totalJobs", jobService.getActiveJobs().size());
        model.addAttribute("featuredCities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai"});
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username/email or password, or account is disabled.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/register-user")
    public String showUserRegistrationForm(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserRegistrationDto());
        }
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "register-user";
    }

    @PostMapping("/register-user")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto dto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (dto.getResumeFile() == null || dto.getResumeFile().isEmpty()) {
            bindingResult.rejectValue("resumeFile", "required", "Resume upload is mandatory (PDF, DOC, DOCX).");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Password and Re-enter Password must match.");
        }

        if (userService.existsByUsername(dto.getUsername())) {
            bindingResult.rejectValue("username", "exists", "Username is already taken.");
        }

        if (userService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "exists", "Email is already registered.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "register-user";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (BadRequestException | DuplicateResourceException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "register-user";
        }
    }

    @GetMapping("/register-hr")
    public String showHrRegistrationForm(Model model) {
        if (!model.containsAttribute("hrDto")) {
            model.addAttribute("hrDto", new HrRegistrationDto());
        }
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "register-hr";
    }

    @PostMapping("/register-hr")
    public String registerHr(@Valid @ModelAttribute("hrDto") HrRegistrationDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Password and Re-enter Password must match.");
        }

        if (hrService.existsByUsername(dto.getUsername())) {
            bindingResult.rejectValue("username", "exists", "Username is already taken.");
        }

        if (hrService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "exists", "Email is already registered.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "register-hr";
        }

        try {
            hrService.registerHR(dto);
            redirectAttributes.addFlashAttribute("successMessage", "HR Registration successful. Please login.");
            return "redirect:/login";
        } catch (BadRequestException | DuplicateResourceException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "register-hr";
        }
    }
}
