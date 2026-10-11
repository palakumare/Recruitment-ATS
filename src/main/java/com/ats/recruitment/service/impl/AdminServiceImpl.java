package com.ats.recruitment.service.impl;

import com.ats.recruitment.dto.AdminDashboardStatsDto;
import com.ats.recruitment.entity.Admin;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.*;
import com.ats.recruitment.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final HRRepository hrRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public AdminServiceImpl(AdminRepository adminRepository,
                            UserRepository userRepository,
                            HRRepository hrRepository,
                            JobRepository jobRepository,
                            ApplicationRepository applicationRepository,
                            InterviewRepository interviewRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.hrRepository = hrRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getAdminStats() {
        long totalUsers = userRepository.count();
        long totalHr = hrRepository.count();
        long totalJobs = jobRepository.count();
        long totalApplications = applicationRepository.count();
        long scheduledInterviews = interviewRepository.countByStatus("SCHEDULED");
        long selectedCandidates = applicationRepository.countByStatus("SELECTED");
        long rejectedCandidates = applicationRepository.countByStatus("REJECTED");

        return new AdminDashboardStatsDto(
                totalUsers,
                totalHr,
                totalJobs,
                totalApplications,
                scheduledInterviews,
                selectedCandidates,
                rejectedCandidates
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Admin findByUsernameOrEmail(String identifier) {
        return adminRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with: " + identifier));
    }

    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}
