package com.fintech.enterprise.service;

import com.fintech.enterprise.model.User;
import com.fintech.enterprise.model.Role;
import com.fintech.enterprise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder; // Required Import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Provides the concrete implementation for the UserService interface, handling business
 * logic related to User entities, including security considerations like password encoding
 * and retrieving the currently authenticated user.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- WRITE Operations ---

    @Override
    public User createUser(User user) {
        // CRITICAL: Encode the plain text password before saving it to the database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // 1. Update Username
        if (userDetails.getUsername() != null && !userDetails.getUsername().trim().isEmpty()) {
            user.setUsername(userDetails.getUsername());
        }

        // 2. Update Role
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        // 3. Update Password (only if a new password string is provided)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // 4. Update Department
        if (userDetails.getDepartment() != null) {
            user.setDepartment(userDetails.getDepartment());
        }

        return userRepository.save(user);
    }

    // --- READ Operations ---

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // --- CRITICAL FIX: Method to retrieve the authenticated user ---
    @Override
    public User getCurrentAuthenticatedUser() {
        // Get the username (principal name) from the Spring Security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the corresponding User entity from the database
        return findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found in database: " + username));
    }

    // --- DELETE Operation ---

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
