package com.shopjoy.service.impl;

import com.shopjoy.aspect.Auditable;
import com.shopjoy.dto.mapper.UserMapperStruct;
import com.shopjoy.dto.request.ChangePasswordRequest;
import com.shopjoy.dto.request.CreateUserRequest;
import com.shopjoy.dto.request.LoginRequest;
import com.shopjoy.dto.response.UserResponse;
import com.shopjoy.entity.User;
import com.shopjoy.entity.UserType;
import com.shopjoy.exception.AuthenticationException;
import com.shopjoy.exception.DuplicateResourceException;
import com.shopjoy.exception.ResourceNotFoundException;
import com.shopjoy.exception.ValidationException;
import com.shopjoy.repository.UserRepository;
import com.shopjoy.service.AuthService;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementation of AuthService for authentication-related operations.
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapperStruct userMapper;

    @Override
    @Transactional
    @Auditable(action = "USER_REGISTRATION", description = "Registering new user")
    public UserResponse registerUser(CreateUserRequest request) {
        validateCreateUserRequest(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = userMapper.toUser(request);
        user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User createdUser = userRepository.save(user);

        return userMapper.toUserResponse(createdUser);
    }

    @Override
    @Transactional
    @Auditable(action = "USER_REGISTRATION", description = "Registering new user with specific type")
    public UserResponse registerUser(CreateUserRequest request, UserType userType) {
        validateCreateUserRequest(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = userMapper.toUser(request, userType);
        user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User createdUser = userRepository.save(user);

        return userMapper.toUserResponse(createdUser);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new AuthenticationException();
        }

        User user = userOpt.get();

        if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            throw new AuthenticationException();
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!BCrypt.checkpw(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        validatePassword(request.getNewPassword());

        String hashedNewPassword = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
        user.setPasswordHash(hashedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new ValidationException("User data cannot be null");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new ValidationException("username", "must not be empty");
        }

        if (request.getUsername().length() < 3 || request.getUsername().length() > 50) {
            throw new ValidationException("username", "must be between 3 and 50 characters");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("email", "must not be empty");
        }

        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("email", "must be a valid email address");
        }

        validatePassword(request.getPassword());
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("password", "must not be empty");
        }

        if (password.length() < 8) {
            throw new ValidationException("password", "must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("password", "must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("password", "must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("password", "must contain at least one digit");
        }
    }
}
