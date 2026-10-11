package com.ats.recruitment.service;

import com.ats.recruitment.dto.AdminDashboardStatsDto;
import com.ats.recruitment.entity.Admin;

public interface AdminService {
    AdminDashboardStatsDto getAdminStats();
    Admin findByUsernameOrEmail(String identifier);
    Admin save(Admin admin);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
