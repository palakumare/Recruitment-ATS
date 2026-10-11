package com.ats.recruitment.service.impl;

import com.ats.recruitment.dto.InterviewScheduleDto;
import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Interview;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.ApplicationRepository;
import com.ats.recruitment.repository.InterviewRepository;
import com.ats.recruitment.service.InterviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository,
                                ApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Interview scheduleInterview(InterviewScheduleDto dto, HR hr) {
        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + dto.getApplicationId()));

        // Automatically set application to SHORTLISTED when scheduling interview if it was PENDING
        if ("PENDING".equalsIgnoreCase(application.getStatus())) {
            application.setStatus("SHORTLISTED");
            applicationRepository.save(application);
        }

        Interview interview = interviewRepository.findByApplication(application).orElse(new Interview());
        interview.setApplication(application);
        interview.setCandidate(application.getUser());
        interview.setJob(application.getJob());
        interview.setHr(hr != null ? hr : application.getJob().getHr());
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewTime(dto.getInterviewTime().trim());
        interview.setInterviewType(dto.getInterviewType().trim());
        interview.setLocationOrMeetingLink(dto.getLocationOrMeetingLink().trim());
        interview.setNotes(dto.getNotes() != null ? dto.getNotes().trim() : "");
        interview.setStatus(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED");
        interview.setCreatedAt(LocalDateTime.now());

        return interviewRepository.save(interview);
    }

    @Override
    public Interview updateInterview(Long interviewId, InterviewScheduleDto dto) {
        Interview interview = getInterviewById(interviewId);
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewTime(dto.getInterviewTime().trim());
        interview.setInterviewType(dto.getInterviewType().trim());
        interview.setLocationOrMeetingLink(dto.getLocationOrMeetingLink().trim());
        if (dto.getNotes() != null) {
            interview.setNotes(dto.getNotes().trim());
        }
        if (dto.getStatus() != null) {
            interview.setStatus(dto.getStatus());
        }

        return interviewRepository.save(interview);
    }

    @Override
    public void updateInterviewStatus(Long interviewId, String status, String notes) {
        Interview interview = getInterviewById(interviewId);
        interview.setStatus(status.toUpperCase());
        if (notes != null && !notes.isBlank()) {
            interview.setNotes(notes);
        }

        // Synchronize application status if SELECTED or REJECTED
        if ("SELECTED".equalsIgnoreCase(status)) {
            interview.getApplication().setStatus("SELECTED");
            applicationRepository.save(interview.getApplication());
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            interview.getApplication().setStatus("REJECTED");
            applicationRepository.save(interview.getApplication());
        }

        interviewRepository.save(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByCandidate(User user) {
        return interviewRepository.findByCandidateOrderByInterviewDateDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByHr(HR hr) {
        return interviewRepository.findByHrOrderByInterviewDateDesc(hr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Interview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> searchHrInterviews(HR hr, String status, String keyword) {
        return interviewRepository.searchHrInterviews(hr, status, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interview> searchAllInterviews(String keyword) {
        return interviewRepository.searchAllInterviews(keyword);
    }

    @Override
    public Interview save(Interview interview) {
        return interviewRepository.save(interview);
    }
}
