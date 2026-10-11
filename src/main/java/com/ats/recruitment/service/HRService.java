package com.ats.recruitment.service;

import com.ats.recruitment.dto.HrDashboardStatsDto;
import com.ats.recruitment.dto.HrProfileDto;
import com.ats.recruitment.dto.HrRegistrationDto;
import com.ats.recruitment.dto.PasswordChangeDto;
import com.ats.recruitment.entity.HR;

import java.util.List;

public interface HRService {
    HR registerHR(HrRegistrationDto dto);
    HR findById(Long id);
    HR findByUsernameOrEmail(String identifier);
    void updateProfile(Long hrId, HrProfileDto dto);
    void changePassword(Long hrId, PasswordChangeDto dto);
    List<HR> getAllHR();
    List<HR> searchHR(String keyword);
    void toggleHrStatus(Long hrId);
    void deleteHR(Long hrId);
    HrDashboardStatsDto getHrStats(HR hr);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    HR save(HR hr);
}
