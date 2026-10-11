package com.ats.recruitment.controller;

import com.ats.recruitment.dto.*;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hr")
public class HRController {

    private final HRService hrService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final UserService userService;

    public HRController(HRService hrService,
                        JobService jobService,
                        ApplicationService applicationService,
                        InterviewService interviewService,
                        UserService userService) {
        this.hrService = hrService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
        this.userService = userService;
    }

    private HR resolveCurrentHr(Authentication authentication) {
        if (authentication == null) return null;
        try {
            return hrService.findByUsernameOrEmail(authentication.getName());
        } catch (Exception ex) {
            List<HR> hrs = hrService.getAllHR();
            return hrs.isEmpty() ? null : hrs.get(0);
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        HrDashboardStatsDto stats = hrService.getHrStats(hr);
        List<Application> recentApplications = applicationService.getApplicationsByHr(hr);
        List<Interview> upcomingInterviews = interviewService.getInterviewsByHr(hr);

        model.addAttribute("currentHr", hr);
        model.addAttribute("stats", stats);
        model.addAttribute("recentApplications", recentApplications.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("upcomingInterviews", upcomingInterviews.stream().limit(5).collect(Collectors.toList()));
        return "hr/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        HrProfileDto profileDto = new HrProfileDto();
        profileDto.setId(hr.getId());
        profileDto.setName(hr.getName());
        profileDto.setUsername(hr.getUsername());
        profileDto.setEmail(hr.getEmail());
        profileDto.setMobile(hr.getMobile());
        profileDto.setCompanyName(hr.getCompanyName());
        profileDto.setGender(hr.getGender());
        profileDto.setLocation(hr.getLocation());

        model.addAttribute("currentHr", hr);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("passwordDto", new PasswordChangeDto());
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "hr/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("profileDto") HrProfileDto profileDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentHr", hr);
            model.addAttribute("passwordDto", new PasswordChangeDto());
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "hr/profile";
        }

        try {
            hrService.updateProfile(hr.getId(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/hr/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication authentication,
                                 @Valid @ModelAttribute("passwordDto") PasswordChangeDto passwordDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters.");
            return "redirect:/hr/profile";
        }

        try {
            hrService.changePassword(hr.getId(), passwordDto);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/hr/profile";
    }

    @GetMapping("/jobs")
    public String jobs(Authentication authentication, Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        List<Job> jobList = jobService.getJobsByHr(hr);
        model.addAttribute("currentHr", hr);
        model.addAttribute("jobs", jobList);
        return "hr/jobs";
    }

    @GetMapping("/jobs/new")
    public String showCreateJobForm(Authentication authentication, Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        JobDto jobDto = new JobDto();
        jobDto.setCompanyName(hr.getCompanyName());
        jobDto.setLocation(hr.getLocation());

        model.addAttribute("currentHr", hr);
        model.addAttribute("jobDto", jobDto);
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "hr/job-create";
    }

    @PostMapping("/jobs/new")
    public String createJob(Authentication authentication,
                            @Valid @ModelAttribute("jobDto") JobDto jobDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentHr", hr);
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "hr/job-create";
        }

        jobService.createJob(jobDto, hr);
        redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully.");
        return "redirect:/hr/jobs";
    }

    @GetMapping("/jobs/{id}/edit")
    public String showEditJobForm(Authentication authentication,
                                  @PathVariable("id") Long id,
                                  Model model) {
        HR hr = resolveCurrentHr(authentication);
        Job job = jobService.getJobById(id);

        JobDto jobDto = new JobDto();
        jobDto.setId(job.getId());
        jobDto.setJobTitle(job.getJobTitle());
        jobDto.setCompanyName(job.getCompanyName());
        jobDto.setDescription(job.getDescription());
        jobDto.setRequiredSkills(job.getRequiredSkills());
        jobDto.setExperienceRequired(job.getExperienceRequired());
        jobDto.setQualification(job.getQualification());
        jobDto.setLocation(job.getLocation());
        jobDto.setSalary(job.getSalary());
        jobDto.setJobType(job.getJobType());
        jobDto.setOpenings(job.getOpenings());
        jobDto.setDeadline(job.getDeadline());
        jobDto.setStatus(job.getStatus());

        model.addAttribute("currentHr", hr);
        model.addAttribute("jobDto", jobDto);
        model.addAttribute("job", job);
        model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
        return "hr/job-edit";
    }

    @PostMapping("/jobs/{id}/edit")
    public String editJob(Authentication authentication,
                          @PathVariable("id") Long id,
                          @Valid @ModelAttribute("jobDto") JobDto jobDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentHr", hr);
            model.addAttribute("job", jobService.getJobById(id));
            model.addAttribute("cities", new String[]{"Nagpur", "Pune", "Hyderabad", "Mumbai", "Bangalore", "Delhi", "Chennai"});
            return "hr/job-edit";
        }

        jobService.updateJob(id, jobDto);
        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully.");
        return "redirect:/hr/jobs";
    }

    @PostMapping("/jobs/{id}/toggle")
    public String toggleJobStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobService.toggleJobStatus(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job status updated successfully.");
        return "redirect:/hr/jobs";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobService.deleteJob(id);
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully.");
        return "redirect:/hr/jobs";
    }

    @GetMapping("/candidates")
    public String candidates(Authentication authentication,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "jobId", required = false) Long jobId,
                             Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        List<Application> applications = applicationService.searchHrApplications(hr, status, jobId, keyword);
        List<Job> hrJobs = jobService.getJobsByHr(hr);

        model.addAttribute("currentHr", hr);
        model.addAttribute("applications", applications);
        model.addAttribute("hrJobs", hrJobs);
        model.addAttribute("selectedStatus", status != null ? status : "");
        model.addAttribute("selectedJobId", jobId);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "hr/candidates";
    }

    @GetMapping("/applications")
    public String applications(Authentication authentication,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "status", required = false) String status,
                               @RequestParam(value = "jobId", required = false) Long jobId,
                               Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        List<Application> applications = applicationService.searchHrApplications(hr, status, jobId, keyword);
        List<Job> hrJobs = jobService.getJobsByHr(hr);

        model.addAttribute("currentHr", hr);
        model.addAttribute("applications", applications);
        model.addAttribute("hrJobs", hrJobs);
        model.addAttribute("selectedStatus", status != null ? status : "");
        model.addAttribute("selectedJobId", jobId);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "hr/applications";
    }

    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable("id") Long id,
                                          @RequestParam("status") String status,
                                          @RequestParam(value = "notes", required = false) String notes,
                                          RedirectAttributes redirectAttributes) {
        applicationService.updateApplicationStatus(id, status, notes);
        redirectAttributes.addFlashAttribute("successMessage", "Candidate application status changed to " + status + ".");
        return "redirect:/hr/applications";
    }

    @GetMapping("/interviews")
    public String interviews(Authentication authentication,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (hr == null) return "redirect:/login";

        List<Interview> interviewList = interviewService.searchHrInterviews(hr, status, keyword);
        model.addAttribute("currentHr", hr);
        model.addAttribute("interviews", interviewList);
        model.addAttribute("selectedStatus", status != null ? status : "");
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "hr/interviews";
    }

    @GetMapping("/interviews/schedule")
    public String showScheduleInterviewForm(Authentication authentication,
                                            @RequestParam("applicationId") Long applicationId,
                                            Model model) {
        HR hr = resolveCurrentHr(authentication);
        Application application = applicationService.getApplicationById(applicationId);

        InterviewScheduleDto scheduleDto = new InterviewScheduleDto();
        scheduleDto.setApplicationId(applicationId);

        model.addAttribute("currentHr", hr);
        model.addAttribute("application", application);
        model.addAttribute("scheduleDto", scheduleDto);
        return "hr/schedule-interview";
    }

    @PostMapping("/interviews/schedule")
    public String scheduleInterview(Authentication authentication,
                                    @Valid @ModelAttribute("scheduleDto") InterviewScheduleDto scheduleDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        HR hr = resolveCurrentHr(authentication);
        if (bindingResult.hasErrors()) {
            Application application = applicationService.getApplicationById(scheduleDto.getApplicationId());
            model.addAttribute("currentHr", hr);
            model.addAttribute("application", application);
            return "hr/schedule-interview";
        }

        interviewService.scheduleInterview(scheduleDto, hr);
        redirectAttributes.addFlashAttribute("successMessage", "Interview scheduled successfully.");
        return "redirect:/hr/interviews";
    }

    @PostMapping("/interviews/{id}/status")
    public String updateInterviewStatus(@PathVariable("id") Long id,
                                        @RequestParam("status") String status,
                                        @RequestParam(value = "notes", required = false) String notes,
                                        RedirectAttributes redirectAttributes) {
        interviewService.updateInterviewStatus(id, status, notes);
        redirectAttributes.addFlashAttribute("successMessage", "Interview status updated to " + status + ".");
        return "redirect:/hr/interviews";
    }
}
