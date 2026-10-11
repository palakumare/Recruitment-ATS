package com.ats.recruitment.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(columnDefinition = "TEXT", length = 5000, nullable = false)
    private String description;

    @Column(name = "required_skills", nullable = false)
    private String requiredSkills;

    @Column(name = "experience_required", nullable = false)
    private String experienceRequired;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String salary;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(nullable = false)
    private Integer openings;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE or CLOSED

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr_id", nullable = false)
    private HR hr;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public Job() {
    }

    public Job(String jobTitle, String companyName, String description, String requiredSkills,
               String experienceRequired, String qualification, String location, String salary,
               String jobType, Integer openings, LocalDate deadline, HR hr) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.experienceRequired = experienceRequired;
        this.qualification = qualification;
        this.location = location;
        this.salary = salary;
        this.jobType = jobType;
        this.openings = openings;
        this.deadline = deadline;
        this.status = "ACTIVE";
        this.hr = hr;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getExperienceRequired() {
        return experienceRequired;
    }

    public void setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HR getHr() {
        return hr;
    }

    public void setHr(HR hr) {
        this.hr = hr;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
