package com.ats.recruitment.controller;

import com.ats.recruitment.dto.PasswordChangeDto;
import com.ats.recruitment.dto.UserDashboardStatsDto;
import com.ats.recruitment.dto.UserProfileDto;
import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.Interview;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.exception.BadRequestException;
import com.ats.recruitment.service.ApplicationService;
import com.ats.recruitment.service.FileStorageService;
import com.ats.recruitment.service.InterviewService;
import com.ats.recruitment.service.JobService;
import com.ats.recruitment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final FileStorageService fileStorageService;

    public UserController(UserService userService,
                          JobService jobService,
                          ApplicationService applicationService,
                          InterviewService interviewService,
                          FileStorageService fileStorageService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
        this.fileStorageService = fileStorageService;
    }

    private User resolveCurrentUser(Authentication authentication) {
        if (authentication == null) return null;
        try {
            return userService.findByUsernameOrEmail(authentication.getName());
        } catch (Exception ex) {
            List<User> users = userService.getAllUsers();
            return users.isEmpty() ? null : users.get(0);
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        UserDashboardStatsDto stats = userService.getUserStats(user);
        List<Application> recentApplications = applicationService.getApplicationsByUser(user);
        List<Interview> upcomingInterviews = interviewService.getInterviewsByCandidate(user);

        model.addAttribute("currentUser", user);
        model.addAttribute("stats", stats);
        model.addAttribute("recentApplications", recentApplications.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("upcomingInterviews", upcomingInterviews.stream().limit(5).collect(Collectors.toList()));
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setId(user.getId());
        profileDto.setName(user.getName());
        profileDto.setUsername(user.getUsername());
        profileDto.setEmail(user.getEmail());
        profileDto.setMobile(user.getMobile());
        profileDto.setExperience(user.getExperience());
        profileDto.setGender(user.getGender());
        profileDto.setAge(user.getAge());
        profileDto.setLocation(user.getLocation());
        profileDto.setResumeOriginalName(user.getResumeOriginalName());

        model.addAttribute("currentUser", user);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("passwordDto", new PasswordChangeDto());
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("profileDto") UserProfileDto profileDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUser", user);
            model.addAttribute("passwordDto", new PasswordChangeDto());
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "user/profile";
        }

        try {
            userService.updateProfile(user.getId(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication authentication,
                                 @Valid @ModelAttribute("passwordDto") PasswordChangeDto passwordDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters.");
            return "redirect:/user/profile";
        }

        try {
            userService.changePassword(user.getId(), passwordDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/resume")
    public String replaceResume(Authentication authentication,
                                @RequestParam("resumeFile") MultipartFile resumeFile,
                                RedirectAttributes redirectAttributes) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        if (resumeFile == null || resumeFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/user/profile";
        }

        try {
            String storedFileName = fileStorageService.storeFile(resumeFile);
            user.setResume(storedFileName);
            user.setResumeOriginalName(resumeFile.getOriginalFilename());
            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Resume updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/jobs")
    public String jobs(Authentication authentication,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "location", required = false) String location,
                       @RequestParam(value = "jobType", required = false) String jobType,
                       @RequestParam(value = "experience", required = false) String experience,
                       @RequestParam(value = "company", required = false) String company,
                       Model model) {
        User user = resolveCurrentUser(authentication);
        List<Job> jobList = jobService.searchActiveJobs(keyword, location, jobType, experience, company);

        Set<Long> appliedJobIds = new HashSet<>();
        if (user != null) {
            appliedJobIds = applicationService.getApplicationsByUser(user)
                    .stream()
                    .map(app -> app.getJob().getId())
                    .collect(Collectors.toSet());
        }

        model.addAttribute("currentUser", user);
        model.addAttribute("jobs", jobList);
        model.addAttribute("appliedJobIds", appliedJobIds);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("selectedLocation", location != null ? location : "");
        model.addAttribute("selectedJobType", jobType != null ? jobType : "");
        model.addAttribute("selectedExperience", experience != null ? experience : "");
        model.addAttribute("selectedCompany", company != null ? company : "");
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai"});
        return "user/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(Authentication authentication,
                             @PathVariable("id") Long id,
                             Model model) {
        User user = resolveCurrentUser(authentication);
        Job job = jobService.getJobById(id);
        boolean alreadyApplied = user != null && applicationService.hasUserApplied(user, id);

        model.addAttribute("currentUser", user);
        model.addAttribute("job", job);
        model.addAttribute("alreadyApplied", alreadyApplied);
        return "user/job-details";
    }

    @PostMapping("/jobs/{id}/apply")
    public String applyForJob(Authentication authentication,
                              @PathVariable("id") Long id,
                              RedirectAttributes redirectAttributes) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        try {
            applicationService.applyForJob(user, id);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully.");
        } catch (BadRequestException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting application: " + ex.getMessage());
        }
        return "redirect:/user/applications";
    }

    @GetMapping("/applications")
    public String applications(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        List<Application> applicationList = applicationService.getApplicationsByUser(user);
        model.addAttribute("currentUser", user);
        model.addAttribute("applications", applicationList);
        return "user/applications";
    }

    @GetMapping("/interviews")
    public String interviews(Authentication authentication, Model model) {
        User user = resolveCurrentUser(authentication);
        if (user == null) return "redirect:/login";

        List<Interview> interviewList = interviewService.getInterviewsByCandidate(user);
        model.addAttribute("currentUser", user);
        model.addAttribute("interviews", interviewList);
        return "user/interviews";
    }
}
