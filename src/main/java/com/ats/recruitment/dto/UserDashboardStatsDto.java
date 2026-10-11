package com.ats.recruitment.dto;

public class UserDashboardStatsDto {
    private long totalJobsAvailable;
    private long jobsApplied;
    private long applicationsPending;
    private long applicationsShortlisted;
    private long interviewsScheduled;
    private long applicationsRejected;

    public UserDashboardStatsDto() {}

    public UserDashboardStatsDto(long totalJobsAvailable, long jobsApplied, long applicationsPending,
                                 long applicationsShortlisted, long interviewsScheduled, long applicationsRejected) {
        this.totalJobsAvailable = totalJobsAvailable;
        this.jobsApplied = jobsApplied;
        this.applicationsPending = applicationsPending;
        this.applicationsShortlisted = applicationsShortlisted;
        this.interviewsScheduled = interviewsScheduled;
        this.applicationsRejected = applicationsRejected;
    }

    public long getTotalJobsAvailable() { return totalJobsAvailable; }
    public void setTotalJobsAvailable(long totalJobsAvailable) { this.totalJobsAvailable = totalJobsAvailable; }

    public long getJobsApplied() { return jobsApplied; }
    public void setJobsApplied(long jobsApplied) { this.jobsApplied = jobsApplied; }

    public long getApplicationsPending() { return applicationsPending; }
    public void setApplicationsPending(long applicationsPending) { this.applicationsPending = applicationsPending; }

    public long getApplicationsShortlisted() { return applicationsShortlisted; }
    public void setApplicationsShortlisted(long applicationsShortlisted) { this.applicationsShortlisted = applicationsShortlisted; }

    public long getInterviewsScheduled() { return interviewsScheduled; }
    public void setInterviewsScheduled(long interviewsScheduled) { this.interviewsScheduled = interviewsScheduled; }

    public long getApplicationsRejected() { return applicationsRejected; }
    public void setApplicationsRejected(long applicationsRejected) { this.applicationsRejected = applicationsRejected; }
}
