package com.ats.recruitment.service.impl;

import com.ats.recruitment.dto.HrDashboardStatsDto;
import com.ats.recruitment.dto.HrProfileDto;
import com.ats.recruitment.dto.HrRegistrationDto;
import com.ats.recruitment.dto.PasswordChangeDto;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.exception.BadRequestException;
import com.ats.recruitment.exception.DuplicateResourceException;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.ApplicationRepository;
import com.ats.recruitment.repository.HRRepository;
import com.ats.recruitment.repository.InterviewRepository;
import com.ats.recruitment.repository.JobRepository;
import com.ats.recruitment.service.HRService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HRServiceImpl implements HRService {

    private final HRRepository hrRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final PasswordEncoder passwordEncoder;

    public HRServiceImpl(HRRepository hrRepository,
                         JobRepository jobRepository,
                         ApplicationRepository applicationRepository,
                         InterviewRepository interviewRepository,
                         PasswordEncoder passwordEncoder) {
        this.hrRepository = hrRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public HR registerHR(HrRegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match.");
        }

        if (hrRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken.");
        }

        if (hrRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }

        HR hr = new HR();
        hr.setName(dto.getName().trim());
        hr.setUsername(dto.getUsername().trim());
        hr.setEmail(dto.getEmail().trim().toLowerCase());
        hr.setMobile(dto.getMobile().trim());
        hr.setCompanyName(dto.getCompanyName().trim());
        hr.setGender(dto.getGender());
        hr.setLocation(dto.getLocation().trim());
        hr.setPassword(passwordEncoder.encode(dto.getPassword()));
        hr.setRole("ROLE_HR");
        hr.setEnabled(true);

        return hrRepository.save(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public HR findById(Long id) {
        return hrRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HR not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public HR findByUsernameOrEmail(String identifier) {
        return hrRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("HR not found with username/email: " + identifier));
    }

    @Override
    public void updateProfile(Long hrId, HrProfileDto dto) {
        HR hr = findById(hrId);

        if (!hr.getUsername().equalsIgnoreCase(dto.getUsername()) &&
                hrRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already in use.");
        }

        if (!hr.getEmail().equalsIgnoreCase(dto.getEmail()) &&
                hrRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already in use.");
        }

        hr.setName(dto.getName().trim());
        hr.setUsername(dto.getUsername().trim());
        hr.setEmail(dto.getEmail().trim().toLowerCase());
        hr.setMobile(dto.getMobile().trim());
        hr.setCompanyName(dto.getCompanyName().trim());
        hr.setGender(dto.getGender());
        hr.setLocation(dto.getLocation().trim());

        hrRepository.save(hr);
    }

    @Override
    public void changePassword(Long hrId, PasswordChangeDto dto) {
        HR hr = findById(hrId);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), hr.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BadRequestException("New password and confirm password do not match.");
        }

        hr.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        hrRepository.save(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HR> getAllHR() {
        return hrRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HR> searchHR(String keyword) {
        return hrRepository.searchHR(keyword);
    }

    @Override
    public void toggleHrStatus(Long hrId) {
        HR hr = findById(hrId);
        hr.setEnabled(!hr.getEnabled());
        hrRepository.save(hr);
    }

    @Override
    public void deleteHR(Long hrId) {
        HR hr = findById(hrId);
        hrRepository.delete(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public HrDashboardStatsDto getHrStats(HR hr) {
        long totalJobsPosted = jobRepository.countByHr(hr);
        long activeJobs = jobRepository.countByHrAndStatus(hr, "ACTIVE");
        long totalApplications = applicationRepository.countByHr(hr);
        long shortlistedCandidates = applicationRepository.countByHrAndStatus(hr, "SHORTLISTED");
        long interviewsScheduled = interviewRepository.countByHr(hr);
        long selectedCandidates = applicationRepository.countByHrAndStatus(hr, "SELECTED");

        // Count distinct candidates who applied for this HR's jobs
        long totalCandidates = applicationRepository.findByHr(hr)
                .stream()
                .map(app -> app.getUser().getId())
                .distinct()
                .count();

        return new HrDashboardStatsDto(
                totalJobsPosted,
                activeJobs,
                totalCandidates,
                totalApplications,
                shortlistedCandidates,
                interviewsScheduled,
                selectedCandidates
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return hrRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return hrRepository.existsByEmail(email);
    }

    @Override
    public HR save(HR hr) {
        return hrRepository.save(hr);
    }
}
