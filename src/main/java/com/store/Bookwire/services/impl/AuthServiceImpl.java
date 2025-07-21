package com.store.Bookwire.services.impl;

import com.store.Bookwire.exceptions.InvalidCredentialsException;
import com.store.Bookwire.mappers.UserMapper;
import com.store.Bookwire.models.dtos.auth.AuthResponse;
import com.store.Bookwire.models.dtos.auth.LoginRequest;
import com.store.Bookwire.models.dtos.auth.RegisterRequest;
import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.repositories.UserRepository;
import com.store.Bookwire.services.AuthService;
import com.store.Bookwire.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder encoder, JwtService jwtService, UserMapper userMapper) {
        this.userRepository = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public String register(RegisterRequest request) {
        saveUser(request);

        return "User registered successfully!";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = findUser(request);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    private User findUser(LoginRequest request) {
        String errorMessage = "Invalid username or password";
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(errorMessage));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(errorMessage);
        }

        return user;
    }

    private void saveUser(RegisterRequest request) {
        User user = userMapper.registerRequestToUser(request, encoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
