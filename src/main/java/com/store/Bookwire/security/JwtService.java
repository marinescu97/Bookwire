package com.store.Bookwire.security;

import com.store.Bookwire.models.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(User user);
    boolean validateToken(String token, UserDetails userDetails);
    Long extractUserId(String token);
}