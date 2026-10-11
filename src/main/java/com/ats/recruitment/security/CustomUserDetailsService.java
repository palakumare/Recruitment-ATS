package com.ats.recruitment.security;

import com.ats.recruitment.entity.Admin;
import com.ats.recruitment.entity.HR;
import com.ats.recruitment.entity.User;
import com.ats.recruitment.repository.AdminRepository;
import com.ats.recruitment.repository.HRRepository;
import com.ats.recruitment.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final HRRepository hrRepository;
    private final UserRepository userRepository;

    public CustomUserDetailsService(AdminRepository adminRepository,
                                    HRRepository hrRepository,
                                    UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.hrRepository = hrRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        String identifier = usernameOrEmail.trim();

        // 1. Check Admin
        Optional<Admin> adminOpt = adminRepository.findByUsernameOrEmail(identifier, identifier);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(
                    admin.getId(),
                    admin.getName(),
                    admin.getUsername(),
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.getRole(),
                    true
            );
        }

        // 2. Check HR
        Optional<HR> hrOpt = hrRepository.findByUsernameOrEmail(identifier, identifier);
        if (hrOpt.isPresent()) {
            HR hr = hrOpt.get();
            return new CustomUserDetails(
                    hr.getId(),
                    hr.getName(),
                    hr.getUsername(),
                    hr.getEmail(),
                    hr.getPassword(),
                    hr.getRole(),
                    hr.getEnabled()
            );
        }

        // 3. Check Applicant / User
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(identifier, identifier);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new CustomUserDetails(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    user.getEnabled()
            );
        }

        throw new UsernameNotFoundException("User not found with identifier: " + identifier);
    }
}
