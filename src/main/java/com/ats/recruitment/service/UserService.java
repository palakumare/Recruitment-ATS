package com.ats.recruitment.service;

import com.ats.recruitment.dto.PasswordChangeDto;
import com.ats.recruitment.dto.UserDashboardStatsDto;
import com.ats.recruitment.dto.UserProfileDto;
import com.ats.recruitment.dto.UserRegistrationDto;
import com.ats.recruitment.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDto dto);
    User findById(Long id);
    User findByUsernameOrEmail(String identifier);
    void updateProfile(Long userId, UserProfileDto dto);
    void changePassword(Long userId, PasswordChangeDto dto);
    List<User> getAllUsers();
    List<User> searchUsers(String keyword);
    void toggleUserStatus(Long userId);
    void deleteUser(Long userId);
    UserDashboardStatsDto getUserStats(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
}
