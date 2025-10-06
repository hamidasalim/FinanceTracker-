package com.fintech.enterprise.service;

import com.fintech.enterprise.model.User;
import com.fintech.enterprise.model.Role;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing User entities.
 */
public interface UserService {

    // --- Core CRUD Operations ---
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);

    // --- Read Operations ---
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    List<User> findUsersByRole(Role role);

    Optional<User> findByUsername(String username);

    User getCurrentAuthenticatedUser();
}