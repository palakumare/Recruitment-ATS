package com.ats.recruitment.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr_id", nullable = false)
    private HR hr;

    @Column(name = "interview_date", nullable = false)
    private LocalDate interviewDate;

    @Column(name = "interview_time", nullable = false, length = 50)
    private String interviewTime;

    @Column(name = "interview_type", nullable = false, length = 100)
    private String interviewType;

    @Column(name = "location_or_meeting_link", nullable = false, length = 500)
    private String locationOrMeetingLink;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false, length = 30)
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED, SELECTED, REJECTED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Interview() {
    }

    public Interview(Application application, User candidate, Job job, HR hr, LocalDate interviewDate,
                     String interviewTime, String interviewType, String locationOrMeetingLink, String notes) {
        this.application = application;
        this.candidate = candidate;
        this.job = job;
        this.hr = hr;
        this.interviewDate = interviewDate;
        this.interviewTime = interviewTime;
        this.interviewType = interviewType;
        this.locationOrMeetingLink = locationOrMeetingLink;
        this.notes = notes;
        this.status = "SCHEDULED";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public HR getHr() {
        return hr;
    }

    public void setHr(HR hr) {
        this.hr = hr;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public String getLocationOrMeetingLink() {
        return locationOrMeetingLink;
    }

    public void setLocationOrMeetingLink(String locationOrMeetingLink) {
        this.locationOrMeetingLink = locationOrMeetingLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
