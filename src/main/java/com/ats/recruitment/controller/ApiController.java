package com.ats.recruitment.controller;

import com.ats.recruitment.dto.AdminDashboardStatsDto;
import com.ats.recruitment.dto.HrDashboardStatsDto;
import com.ats.recruitment.dto.UserDashboardStatsDto;
import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final JobService jobService;
    private final UserService userService;
    private final HRService hrService;
    private final AdminService adminService;
    private final ApplicationService applicationService;

    public ApiController(JobService jobService,
                         UserService userService,
                         HRService hrService,
                         AdminService adminService,
                         ApplicationService applicationService) {
        this.jobService = jobService;
        this.userService = userService;
        this.hrService = hrService;
        this.adminService = adminService;
        this.applicationService = applicationService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getActiveJobs(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "location", required = false) String location,
                                                   @RequestParam(value = "jobType", required = false) String jobType,
                                                   @RequestParam(value = "experience", required = false) String experience,
                                                   @RequestParam(value = "company", required = false) String company) {
        List<Job> jobs = jobService.searchActiveJobs(keyword, location, jobType, experience, company);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/stats/admin")
    public ResponseEntity<AdminDashboardStatsDto> getAdminStats() {
        return ResponseEntity.ok(adminService.getAdminStats());
    }

    @GetMapping("/stats/hr")
    public ResponseEntity<HrDashboardStatsDto> getHrStats(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        HR hr = hrService.findByUsernameOrEmail(authentication.getName());
        return ResponseEntity.ok(hrService.getHrStats(hr));
    }

    @GetMapping("/stats/user")
    public ResponseEntity<UserDashboardStatsDto> getUserStats(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User user = userService.findByUsernameOrEmail(authentication.getName());
        return ResponseEntity.ok(userService.getUserStats(user));
    }

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<Map<String, Object>> applyJob(@PathVariable Long jobId, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication == null) {
            response.put("success", false);
            response.put("message", "User must be authenticated.");
            return ResponseEntity.status(401).body(response);
        }

        try {
            User user = userService.findByUsernameOrEmail(authentication.getName());
            Application app = applicationService.applyForJob(user, jobId);
            response.put("success", true);
            response.put("message", "Application submitted successfully.");
            response.put("applicationId", app.getId());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
