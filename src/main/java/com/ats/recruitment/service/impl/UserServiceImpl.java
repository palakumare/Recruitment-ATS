package com.ats.recruitment.service.impl;

import com.ats.recruitment.dto.PasswordChangeDto;
import com.ats.recruitment.dto.UserDashboardStatsDto;
import com.ats.recruitment.dto.UserProfileDto;
import com.ats.recruitment.dto.UserRegistrationDto;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.exception.BadRequestException;
import com.ats.recruitment.exception.DuplicateResourceException;
import com.ats.recruitment.exception.ResourceNotFoundException;
import com.ats.recruitment.repository.ApplicationRepository;
import com.ats.recruitment.repository.InterviewRepository;
import com.ats.recruitment.repository.JobRepository;
import com.ats.recruitment.repository.UserRepository;
import com.ats.recruitment.service.FileStorageService;
import com.ats.recruitment.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           JobRepository jobRepository,
                           ApplicationRepository applicationRepository,
                           InterviewRepository interviewRepository,
                           FileStorageService fileStorageService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match.");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already registered.");
        }

        if (dto.getResumeFile() == null || dto.getResumeFile().isEmpty()) {
            throw new BadRequestException("Resume upload is mandatory for candidate registration.");
        }

        String storedFileName = fileStorageService.storeFile(dto.getResumeFile());
        String originalFileName = dto.getResumeFile().getOriginalFilename();

        User user = new User();
        user.setName(dto.getName().trim());
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setMobile(dto.getMobile().trim());
        user.setResume(storedFileName);
        user.setResumeOriginalName(originalFileName);
        user.setExperience(dto.getExperience());
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        user.setLocation(dto.getLocation().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsernameOrEmail(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username/email: " + identifier));
    }

    @Override
    public void updateProfile(Long userId, UserProfileDto dto) {
        User user = findById(userId);

        if (!user.getUsername().equalsIgnoreCase(dto.getUsername()) &&
                userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already in use.");
        }

        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email '" + dto.getEmail() + "' is already in use.");
        }

        user.setName(dto.getName().trim());
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setMobile(dto.getMobile().trim());
        user.setExperience(dto.getExperience());
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        user.setLocation(dto.getLocation().trim());

        if (dto.getNewResumeFile() != null && !dto.getNewResumeFile().isEmpty()) {
            String storedFileName = fileStorageService.storeFile(dto.getNewResumeFile());
            user.setResume(storedFileName);
            user.setResumeOriginalName(dto.getNewResumeFile().getOriginalFilename());
        }

        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, PasswordChangeDto dto) {
        User user = findById(userId);

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BadRequestException("New password and confirm password do not match.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = findById(userId);
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDashboardStatsDto getUserStats(User user) {
        long totalJobsAvailable = jobRepository.countByStatus("ACTIVE");
        long jobsApplied = applicationRepository.countByUser(user);
        long applicationsPending = applicationRepository.countByUserAndStatus(user, "PENDING");
        long applicationsShortlisted = applicationRepository.countByUserAndStatus(user, "SHORTLISTED");
        long interviewsScheduled = interviewRepository.countByCandidate(user);
        long applicationsRejected = applicationRepository.countByUserAndStatus(user, "REJECTED");

        return new UserDashboardStatsDto(
                totalJobsAvailable,
                jobsApplied,
                applicationsPending,
                applicationsShortlisted,
                interviewsScheduled,
                applicationsRejected
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
