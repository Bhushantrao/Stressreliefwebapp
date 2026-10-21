package com.stressless.service;

import com.stressless.dto.request.SignupRequest;
import com.stressless.dto.request.UpdateProfileRequest;
import com.stressless.entity.Role;
import com.stressless.entity.User;
import com.stressless.exception.BadRequestException;
import com.stressless.exception.ResourceNotFoundException;
import com.stressless.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BadRequestException("No authenticated user found in session");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("Email is already registered: " + signupRequest.getEmail());
        }

        Role role = Role.ROLE_USER;
        if (signupRequest.getRole() != null && signupRequest.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            role = Role.ROLE_ADMIN;
        }

        User user = new User(
                signupRequest.getName().trim(),
                signupRequest.getEmail().trim().toLowerCase(),
                passwordEncoder.encode(signupRequest.getPassword()),
                role
        );
        user.setAvatarUrl("https://api.dicebear.com/7.x/bottts/svg?seed=" + user.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfile(UpdateProfileRequest request) {
        User user = getCurrentAuthenticatedUser();

        user.setName(request.getName().trim());
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(request.getAvatarUrl().trim());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getOldPassword() == null || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new BadRequestException("Current password does not match");
            }
            if (request.getNewPassword().length() < 6) {
                throw new BadRequestException("New password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
