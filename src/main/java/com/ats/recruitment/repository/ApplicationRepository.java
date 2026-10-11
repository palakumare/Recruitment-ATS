package com.ats.recruitment.repository;

import com.ats.recruitment.entity.Application;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.Job;
import com.ats.recruitment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserOrderByApplicationDateDesc(User user);

    List<Application> findByJobOrderByApplicationDateDesc(Job job);

    @Query("SELECT a FROM Application a WHERE a.job.hr = :hr ORDER BY a.applicationDate DESC")
    List<Application> findByHr(@Param("hr") HR hr);

    @Query("SELECT a FROM Application a WHERE a.job.hr = :hr AND " +
           "(:status IS NULL OR :status = '' OR a.status = :status) AND " +
           "(:jobId IS NULL OR a.job.id = :jobId) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(a.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(a.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(a.job.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY a.applicationDate DESC")
    List<Application> searchHrApplications(@Param("hr") HR hr,
                                           @Param("status") String status,
                                           @Param("jobId") Long jobId,
                                           @Param("keyword") String keyword);

    Optional<Application> findByUserAndJob(User user, Job job);

    boolean existsByUserAndJob(User user, Job job);

    long countByStatus(String status);

    long countByUser(User user);

    long countByUserAndStatus(User user, String status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.hr = :hr")
    long countByHr(@Param("hr") HR hr);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.hr = :hr AND a.status = :status")
    long countByHrAndStatus(@Param("hr") HR hr, @Param("status") String status);

    @Query("SELECT a FROM Application a WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(a.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(a.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(a.job.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(a.job.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY a.applicationDate DESC")
    List<Application> searchAllApplications(@Param("keyword") String keyword);
}
