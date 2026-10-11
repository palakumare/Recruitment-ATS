package com.ats.recruitment.service.impl;

import com.ats.recruitment.dto.JobDto;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.JobRepository;
import com.ats.recruitment.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(JobDto dto, HR hr) {
        Job job = new Job();
        job.setJobTitle(dto.getJobTitle().trim());
        job.setCompanyName(dto.getCompanyName().trim());
        job.setDescription(dto.getDescription().trim());
        job.setRequiredSkills(dto.getRequiredSkills().trim());
        job.setExperienceRequired(dto.getExperienceRequired().trim());
        job.setQualification(dto.getQualification().trim());
        job.setLocation(dto.getLocation().trim());
        job.setSalary(dto.getSalary().trim());
        job.setJobType(dto.getJobType().trim());
        job.setOpenings(dto.getOpenings());
        job.setDeadline(dto.getDeadline());
        job.setStatus("ACTIVE");
        job.setHr(hr);
        job.setCreatedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long jobId, JobDto dto) {
        Job job = getJobById(jobId);
        job.setJobTitle(dto.getJobTitle().trim());
        job.setCompanyName(dto.getCompanyName().trim());
        job.setDescription(dto.getDescription().trim());
        job.setRequiredSkills(dto.getRequiredSkills().trim());
        job.setExperienceRequired(dto.getExperienceRequired().trim());
        job.setQualification(dto.getQualification().trim());
        job.setLocation(dto.getLocation().trim());
        job.setSalary(dto.getSalary().trim());
        job.setJobType(dto.getJobType().trim());
        job.setOpenings(dto.getOpenings());
        job.setDeadline(dto.getDeadline());
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            job.setStatus(dto.getStatus());
        }

        return jobRepository.save(job);
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getActiveJobs() {
        return jobRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getJobsByHr(HR hr) {
        return jobRepository.findByHrOrderByCreatedAtDesc(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> searchActiveJobs(String keyword, String location, String jobType, String experience, String company) {
        return jobRepository.searchActiveJobs(keyword, location, jobType, experience, company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> searchAllJobs(String keyword) {
        return jobRepository.searchAllJobs(keyword);
    }

    @Override
    public void toggleJobStatus(Long jobId) {
        Job job = getJobById(jobId);
        if ("ACTIVE".equalsIgnoreCase(job.getStatus())) {
            job.setStatus("CLOSED");
        } else {
            job.setStatus("ACTIVE");
        }
        jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId) {
        Job job = getJobById(jobId);
        jobRepository.delete(job);
    }

    @Override
    public Job save(Job job) {
        return jobRepository.save(job);
    }
}
