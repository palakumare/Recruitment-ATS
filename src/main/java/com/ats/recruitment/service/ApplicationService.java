package com.ats.recruitment.service;

import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;

import java.util.List;

public interface ApplicationService {
    Application applyForJob(User user, Long jobId);
    boolean hasUserApplied(User user, Long jobId);
    List<Application> getApplicationsByUser(User user);
    List<Application> getApplicationsByHr(HR hr);
    List<Application> getApplicationsByJob(Job job);
    List<Application> getAllApplications();
    Application getApplicationById(Long id);
    void updateApplicationStatus(Long applicationId, String status, String notes);
    List<Application> searchHrApplications(HR hr, String status, Long jobId, String keyword);
    List<Application> searchAllApplications(String keyword);
    Application save(Application application);
}
