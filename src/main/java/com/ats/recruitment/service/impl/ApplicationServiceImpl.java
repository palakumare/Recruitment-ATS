package com.ats.recruitment.service.impl;

import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.exception.BadRequestException;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.ApplicationRepository;
import com.ats.recruitment.repository.JobRepository;
import com.ats.recruitment.service.ApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public Application applyForJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (!"ACTIVE".equalsIgnoreCase(job.getStatus())) {
            throw new BadRequestException("This job is currently closed and not accepting new applications.");
        }

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new BadRequestException("You have already submitted an application for this position.");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("PENDING");

        return applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserApplied(User user, Long jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) return false;
        return applicationRepository.existsByUserAndJob(user, job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByUser(User user) {
        return applicationRepository.findByUserOrderByApplicationDateDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByHr(HR hr) {
        return applicationRepository.findByHr(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByJob(Job job) {
        return applicationRepository.findByJobOrderByApplicationDateDesc(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    @Override
    public void updateApplicationStatus(Long applicationId, String status, String notes) {
        Application application = getApplicationById(applicationId);
        application.setStatus(status.toUpperCase());
        if (notes != null && !notes.isBlank()) {
            application.setNotes(notes);
        }
        applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> searchHrApplications(HR hr, String status, Long jobId, String keyword) {
        return applicationRepository.searchHrApplications(hr, status, jobId, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> searchAllApplications(String keyword) {
        return applicationRepository.searchAllApplications(keyword);
    }

    @Override
    public Application save(Application application) {
        return applicationRepository.save(application);
    }
}
