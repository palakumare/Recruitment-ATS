package com.ats.recruitment.service;

import com.ats.recruitment.dto.JobDto;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;

import java.util.List;

public interface JobService {
    Job createJob(JobDto dto, HR hr);
    Job updateJob(Long jobId, JobDto dto);
    Job getJobById(Long id);
    List<Job> getAllJobs();
    List<Job> getActiveJobs();
    List<Job> getJobsByHr(HR hr);
    List<Job> searchActiveJobs(String keyword, String location, String jobType, String experience, String company);
    List<Job> searchAllJobs(String keyword);
    void toggleJobStatus(Long jobId);
    void deleteJob(Long jobId);
    Job save(Job job);
}
