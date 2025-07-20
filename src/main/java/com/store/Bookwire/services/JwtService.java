package com.store.Bookwire.services;

import com.store.Bookwire.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
}