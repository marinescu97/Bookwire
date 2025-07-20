package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.AuthResponse;
import com.store.Bookwire.models.dtos.LoginRequest;
import com.store.Bookwire.models.dtos.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}