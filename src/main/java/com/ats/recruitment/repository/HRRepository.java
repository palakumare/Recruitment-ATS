package com.ats.recruitment.repository;

import com.ats.recruitment.entity.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {
    Optional<HR> findByUsername(String username);
    Optional<HR> findByEmail(String email);
    Optional<HR> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT h FROM HR h WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<HR> searchHR(@Param("keyword") String keyword);
}
