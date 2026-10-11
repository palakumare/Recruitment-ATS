package com.ats.recruitment.repository;

import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByHrOrderByCreatedAtDesc(HR hr);

    List<Job> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);

    long countByHr(HR hr);

    long countByHrAndStatus(HR hr, String status);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR :location = '' OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:jobType IS NULL OR :jobType = '' OR j.jobType = :jobType) AND " +
           "(:experience IS NULL OR :experience = '' OR LOWER(j.experienceRequired) LIKE LOWER(CONCAT('%', :experience, '%'))) AND " +
           "(:company IS NULL OR :company = '' OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :company, '%'))) " +
           "ORDER BY j.createdAt DESC")
    List<Job> searchActiveJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") String jobType,
            @Param("experience") String experience,
            @Param("company") String company);

    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.jobType) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY j.createdAt DESC")
    List<Job> searchAllJobs(@Param("keyword") String keyword);
}
