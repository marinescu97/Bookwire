package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.auth.AuthResponse;
import com.store.Bookwire.models.dtos.auth.LoginRequest;
import com.store.Bookwire.models.dtos.auth.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}