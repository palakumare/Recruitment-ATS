package com.ats.recruitment.repository;

import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Interview;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByCandidateOrderByInterviewDateDesc(User candidate);

    List<Interview> findByHrOrderByInterviewDateDesc(HR hr);

    List<Interview> findByJobOrderByInterviewDateDesc(Job job);

    Optional<Interview> findByApplication(Application application);

    long countByStatus(String status);

    long countByCandidate(User candidate);

    long countByCandidateAndStatus(User candidate, String status);

    long countByHr(HR hr);

    long countByHrAndStatus(HR hr, String status);

    @Query("SELECT i FROM Interview i WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(i.candidate.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.job.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.job.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.interviewType) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY i.interviewDate DESC")
    List<Interview> searchAllInterviews(@Param("keyword") String keyword);

    @Query("SELECT i FROM Interview i WHERE i.hr = :hr AND " +
           "(:status IS NULL OR :status = '' OR i.status = :status) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(i.candidate.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.job.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(i.interviewType) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY i.interviewDate DESC")
    List<Interview> searchHrInterviews(@Param("hr") HR hr,
                                       @Param("status") String status,
                                       @Param("keyword") String keyword);
}
