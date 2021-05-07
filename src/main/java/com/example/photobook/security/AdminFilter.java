package com.example.photobook.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class AdminFilter extends OncePerRequestFilter {

    private final String ADMIN_HOST = "localhost";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURL().toString();
        String host = new URL(url).getHost();
        if (!host.equals(ADMIN_HOST)) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

