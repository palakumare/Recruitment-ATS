package com.ats.recruitment.service;

import com.ats.recruitment.dto.InterviewScheduleDto;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Interview;
import com.ats.recruitment.entity.User;

import java.util.List;

public interface InterviewService {
    Interview scheduleInterview(InterviewScheduleDto dto, HR hr);
    Interview updateInterview(Long interviewId, InterviewScheduleDto dto);
    void updateInterviewStatus(Long interviewId, String status, String notes);
    List<Interview> getInterviewsByCandidate(User user);
    List<Interview> getInterviewsByHr(HR hr);
    List<Interview> getAllInterviews();
    Interview getInterviewById(Long id);
    List<Interview> searchHrInterviews(HR hr, String status, String keyword);
    List<Interview> searchAllInterviews(String keyword);
    Interview save(Interview interview);
}
