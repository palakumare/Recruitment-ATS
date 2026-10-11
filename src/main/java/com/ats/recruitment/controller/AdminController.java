package com.ats.recruitment.controller;

import com.ats.recruitment.dto.AdminDashboardStatsDto;
import com.ats.recruitment.dto.HrProfileDto;
import com.ats.recruitment.dto.JobDto;
import com.ats.recruitment.dto.UserProfileDto;
import com.ats.recruitment.entity.*;
import com.ats.recruitment.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final HRService hrService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public AdminController(AdminService adminService,
                           UserService userService,
                           HRService hrService,
                           JobService jobService,
                           ApplicationService applicationService,
                           InterviewService interviewService) {
        this.adminService = adminService;
        this.userService = userService;
        this.hrService = hrService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        AdminDashboardStatsDto stats = adminService.getAdminStats();
        List<User> recentUsers = userService.getAllUsers();
        List<Job> recentJobs = jobService.getAllJobs();
        List<Application> recentApplications = applicationService.getAllApplications();

        model.addAttribute("adminName", authentication != null ? authentication.getName() : "Administrator");
        model.addAttribute("stats", stats);
        model.addAttribute("recentUsers", recentUsers.stream().limit(5).toList());
        model.addAttribute("recentJobs", recentJobs.stream().limit(5).toList());
        model.addAttribute("recentApplications", recentApplications.stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ==================== USER MANAGEMENT ====================
    @GetMapping("/users")
    public String users(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<User> userList = userService.searchUsers(keyword);
        model.addAttribute("users", userList);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setExperience(user.getExperience());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setLocation(user.getLocation());
        dto.setResumeOriginalName(user.getResumeOriginalName());

        model.addAttribute("user", user);
        model.addAttribute("profileDto", dto);
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable("id") Long id,
                           @Valid @ModelAttribute("profileDto") UserProfileDto profileDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findById(id));
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "admin/user-edit";
        }

        try {
            userService.updateProfile(id, profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "User details updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "User status changed successfully.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/applications")
    public String userApplications(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        List<Application> applications = applicationService.getApplicationsByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("applications", applications);
        return "admin/user-applications";
    }

    // ==================== HR MANAGEMENT ====================
    @GetMapping("/hr")
    public String hrList(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<HR> hrs = hrService.searchHR(keyword);
        model.addAttribute("hrList", hrs);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/hr-list";
    }

    @GetMapping("/hr/{id}/edit")
    public String showEditHrForm(@PathVariable("id") Long id, Model model) {
        HR hr = hrService.findById(id);
        HrProfileDto dto = new HrProfileDto();
        dto.setId(hr.getId());
        dto.setName(hr.getName());
        dto.setUsername(hr.getUsername());
        dto.setEmail(hr.getEmail());
        dto.setMobile(hr.getMobile());
        dto.setCompanyName(hr.getCompanyName());
        dto.setGender(hr.getGender());
        dto.setLocation(hr.getLocation());

        model.addAttribute("hr", hr);
        model.addAttribute("profileDto", dto);
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "admin/hr-edit";
    }

    @PostMapping("/hr/{id}/edit")
    public String editHr(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("profileDto") HrProfileDto profileDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hr", hrService.findById(id));
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "admin/hr-edit";
        }

        try {
            hrService.updateProfile(id, profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "HR profile updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/hr";
    }

    @PostMapping("/hr/{id}/toggle")
    public String toggleHr(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        hrService.toggleHrStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "HR status updated successfully.");
        return "redirect:/admin/hr";
    }

    @PostMapping("/hr/{id}/delete")
    public String deleteHr(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        hrService.deleteHR(id);
        redirectAttributes.addFlashAttribute("successMessage", "HR account deleted successfully.");
        return "redirect:/admin/hr";
    }

    // ==================== JOB MANAGEMENT ====================
    @GetMapping("/jobs")
    public String jobs(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Job> jobList = jobService.searchAllJobs(keyword);
        model.addAttribute("jobs", jobList);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/jobs";
    }

    @GetMapping("/jobs/{id}/edit")
    public String showEditJobForm(@PathVariable("id") Long id, Model model) {
        Job job = jobService.getJobById(id);
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setJobTitle(job.getJobTitle());
        dto.setCompanyName(job.getCompanyName());
        dto.setDescription(job.getDescription());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setExperienceRequired(job.getExperienceRequired());
        dto.setQualification(job.getQualification());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setJobType(job.getJobType());
        dto.setOpenings(job.getOpenings());
        dto.setDeadline(job.getDeadline());
        dto.setStatus(job.getStatus());

        model.addAttribute("job", job);
        model.addAttribute("jobDto", dto);
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "admin/job-edit";
    }

    @PostMapping("/jobs/{id}/edit")
    public String editJob(@PathVariable("id") Long id,
                          @Valid @ModelAttribute("jobDto") JobDto jobDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("job", jobService.getJobById(id));
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "admin/job-edit";
        }

        jobService.updateJob(id, jobDto);
        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully.");
        return "redirect:/admin/jobs";
    }

    @PostMapping("/jobs/{id}/toggle")
    public String toggleJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobService.toggleJobStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job status updated successfully.");
        return "redirect:/admin/jobs";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobService.deleteJob(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully.");
        return "redirect:/admin/jobs";
    }

    // ==================== APPLICATION MANAGEMENT ====================
    @GetMapping("/applications")
    public String applications(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Application> applicationList = applicationService.searchAllApplications(keyword);
        model.addAttribute("applications", applicationList);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/applications";
    }

    // ==================== INTERVIEW MANAGEMENT ====================
    @GetMapping("/interviews")
    public String interviews(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Interview> interviewList = interviewService.searchAllInterviews(keyword);
        model.addAttribute("interviews", interviewList);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/interviews";
    }
}
