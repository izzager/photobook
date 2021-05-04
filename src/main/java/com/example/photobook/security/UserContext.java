package com.example.photobook.security;

import org.springframework.security.core.Authentication;

public interface UserContext {
    Authentication getAuthentication();
}
