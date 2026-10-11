package com.ats.recruitment.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class JobDto {

    private Long id;

    @NotBlank(message = "Job Title is required")
    private String jobTitle;

    @NotBlank(message = "Company Name is required")
    private String companyName;

    @NotBlank(message = "Job Description is required")
    private String description;

    @NotBlank(message = "Required Skills are required")
    private String requiredSkills;

    @NotBlank(message = "Experience Required is required")
    private String experienceRequired;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Salary/CTC is required")
    private String salary;

    @NotBlank(message = "Job Type is required")
    private String jobType;

    @NotNull(message = "Number of Openings is required")
    @Min(value = 1, message = "Openings must be at least 1")
    private Integer openings;

    @NotNull(message = "Application Deadline is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    private String status = "ACTIVE";

    public JobDto() {
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

    public String location() {
        return location;
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
}
