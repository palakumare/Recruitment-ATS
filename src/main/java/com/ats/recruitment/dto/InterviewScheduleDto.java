package com.ats.recruitment.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class InterviewScheduleDto {

    private Long id;

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Interview Date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate interviewDate;

    @NotBlank(message = "Interview Time is required")
    private String interviewTime;

    @NotBlank(message = "Interview Type is required")
    private String interviewType;

    @NotBlank(message = "Meeting Link or Location is required")
    private String locationOrMeetingLink;

    private String notes;

    private String status = "SCHEDULED";

    public InterviewScheduleDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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
}
