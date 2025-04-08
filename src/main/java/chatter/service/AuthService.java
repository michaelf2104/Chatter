package com.chatter.service;

import com.chatter.model.User;
import com.chatter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public AuthService(UserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.messageSource = messageSource;
    }

    /**
     * registres a new user in the system
     */
    // TODO: do password encryption
    public String registerUser(User user) {
        try {
            if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
                logger.warn("Registration failed: Phone number already in use");
                return messageSource.getMessage("auth.register.phoneExists", null, LocaleContextHolder.getLocale());
            }

            userRepository.save(user);
            logger.info("Registration successful");
            return messageSource.getMessage("auth.register.success", null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            logger.error("Registration failed", e);
            return messageSource.getMessage("auth.register.failure", null, LocaleContextHolder.getLocale());
        }
    }

    /**
     * verifies login credentials
     */
    public boolean verifyUser(String phoneNumber, String password) {
        try {
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                logger.info("Login successful for {}", phoneNumber);
                return true;
            }
            logger.warn("Login failed for {}", phoneNumber);
            return false;
        } catch (Exception e) {
            logger.error("Login verification failed", e);
            return false;
        }
    }

    /**
     * sends 5 digit verification token
     */
    public String sendVerificationCode(String phoneNumber) {
        try {
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isEmpty()) {
                logger.warn("User not found for phone number: {}", phoneNumber);
                return messageSource.getMessage("auth.verification.userNotFound", null, LocaleContextHolder.getLocale());
            }

            String code = String.format("%05d", new Random().nextInt(100000));
            user.get().setVerificationCode(code);
            userRepository.save(user.get());
            logger.info("Verification code for {} is {}", phoneNumber, code);

            logger.info("Verification code sent to {}", phoneNumber);
            return messageSource.getMessage("auth.verification.codeSent", null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            logger.error("Failed to send verification code", e);
            return messageSource.getMessage("auth.verification.sendFail", null, LocaleContextHolder.getLocale());
        }
    }

    /**
     * resends verification code
     */
    public String resendVerificationCode(String phoneNumber) {
        return sendVerificationCode(phoneNumber);
    }

    /**
     * verifies that the code matches the one stored for the user
     */
    public boolean verifyCode(String phoneNumber, String code) {
        try {
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isPresent()) {
                String storedCode = user.get().getVerificationCode();
                logger.info("Comparing code {} with stored code {}", code, storedCode);
                return code.equals(storedCode);
            } else {
                logger.warn("User not found for phone number: {}", phoneNumber);
                return false;
            }
        } catch (Exception e) {
            logger.error("Verification failed", e);
            return false;
        }
    }

}
