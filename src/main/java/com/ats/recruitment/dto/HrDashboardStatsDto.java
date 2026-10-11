package com.ats.recruitment.dto;

public class HrDashboardStatsDto {
    private long totalJobsPosted;
    private long activeJobs;
    private long totalCandidates;
    private long totalApplications;
    private long shortlistedCandidates;
    private long interviewsScheduled;
    private long selectedCandidates;

    public HrDashboardStatsDto() {}

    public HrDashboardStatsDto(long totalJobsPosted, long activeJobs, long totalCandidates, long totalApplications,
                               long shortlistedCandidates, long interviewsScheduled, long selectedCandidates) {
        this.totalJobsPosted = totalJobsPosted;
        this.activeJobs = activeJobs;
        this.totalCandidates = totalCandidates;
        this.totalApplications = totalApplications;
        this.shortlistedCandidates = shortlistedCandidates;
        this.interviewsScheduled = interviewsScheduled;
        this.selectedCandidates = selectedCandidates;
    }

    public long getTotalJobsPosted() { return totalJobsPosted; }
    public void setTotalJobsPosted(long totalJobsPosted) { this.totalJobsPosted = totalJobsPosted; }

    public long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(long activeJobs) { this.activeJobs = activeJobs; }

    public long getTotalCandidates() { return totalCandidates; }
    public void setTotalCandidates(long totalCandidates) { this.totalCandidates = totalCandidates; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public long getShortlistedCandidates() { return shortlistedCandidates; }
    public void setShortlistedCandidates(long shortlistedCandidates) { this.shortlistedCandidates = shortlistedCandidates; }

    public long getInterviewsScheduled() { return interviewsScheduled; }
    public void setInterviewsScheduled(long interviewsScheduled) { this.interviewsScheduled = interviewsScheduled; }

    public long getSelectedCandidates() { return selectedCandidates; }
    public void setSelectedCandidates(long selectedCandidates) { this.selectedCandidates = selectedCandidates; }
}
