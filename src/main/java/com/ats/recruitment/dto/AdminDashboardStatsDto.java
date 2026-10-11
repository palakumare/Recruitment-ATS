package com.ats.recruitment.dto;

public class AdminDashboardStatsDto {
    private long totalUsers;
    private long totalHr;
    private long totalJobs;
    private long totalApplications;
    private long scheduledInterviews;
    private long selectedCandidates;
    private long rejectedCandidates;

    public AdminDashboardStatsDto() {}

    public AdminDashboardStatsDto(long totalUsers, long totalHr, long totalJobs, long totalApplications,
                                  long scheduledInterviews, long selectedCandidates, long rejectedCandidates) {
        this.totalUsers = totalUsers;
        this.totalHr = totalHr;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
        this.scheduledInterviews = scheduledInterviews;
        this.selectedCandidates = selectedCandidates;
        this.rejectedCandidates = rejectedCandidates;
    }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalHr() { return totalHr; }
    public void setTotalHr(long totalHr) { this.totalHr = totalHr; }

    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public long getScheduledInterviews() { return scheduledInterviews; }
    public void setScheduledInterviews(long scheduledInterviews) { this.scheduledInterviews = scheduledInterviews; }

    public long getSelectedCandidates() { return selectedCandidates; }
    public void setSelectedCandidates(long selectedCandidates) { this.selectedCandidates = selectedCandidates; }

    public long getRejectedCandidates() { return rejectedCandidates; }
    public void setRejectedCandidates(long rejectedCandidates) { this.rejectedCandidates = rejectedCandidates; }
}
