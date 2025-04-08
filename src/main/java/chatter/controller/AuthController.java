package com.chatter.controller;

import com.chatter.model.User;
import com.chatter.repository.UserRepository;
import com.chatter.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final MessageSource messageSource;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository, MessageSource messageSource) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    /**
     * handles user registration
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            String result = authService.registerUser(user);
            if (messageSource.getMessage("auth.register.success", null, LocaleContextHolder.getLocale()).equals(result)) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());}
    }

    /**
     * handles user login
     */
    @PostMapping("/login")
    public ResponseEntity<String> requestCode(@RequestBody Map<String, String> payload) {
        try {
            String phoneNumber = payload.get("phoneNumber");
            String result = authService.sendVerificationCode(phoneNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error sending verification code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestBody Map<String, String> payload) {
        try {
            String phoneNumber = payload.get("phoneNumber");
            authService.resendVerificationCode(phoneNumber);
            return ResponseEntity.ok("Verification code resent");
        } catch (Exception e) {
            logger.error("Error resending verification code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to resend code");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> payload) {
        try {
            String phoneNumber = payload.get("phoneNumber");
            String code = payload.get("code");
            logger.info("Verifying code {} for phone {}", code, phoneNumber);
            boolean verified = authService.verifyCode(phoneNumber, code);
            if (verified) {
                logger.info("code was correct");
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid code");
            }
        } catch (Exception e) {
            logger.error("Error verifying code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed");
        }
    }

}
