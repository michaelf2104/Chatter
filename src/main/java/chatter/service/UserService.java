package com.chatter.service;

import com.chatter.model.User;
import com.chatter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all users from database.", e);
            return List.of();
        }
    }

    public Optional<User> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error retrieving user with ID: {}", id, e);
            return Optional.empty();
        }
    }

    public Optional<User> createUser(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            logger.error("Error creating user with ID: {}", user.getId(), e);
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                logger.warn("Deletion failed: User with ID {} does not exist.", id);
                return false;
            }
            userRepository.deleteById(id);
            logger.info("User with ID {} was successfully deleted.", id);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            return false;
        }
    }
}
