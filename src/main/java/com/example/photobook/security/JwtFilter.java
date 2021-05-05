package com.example.photobook.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends GenericFilterBean {

    private final String AUTHORIZATION = "Authorization";
    private final List<String> IGNORING_URLS = Arrays.asList("/register", "/auth");

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final String contextPath;

    public JwtFilter(JwtProvider jwtProvider,
                     CustomUserDetailsService userDetailsService,
                     @Value("${server.servlet.context-path}") String contextPath) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.contextPath = contextPath;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (ignoringUrl(servletRequest, servletResponse, filterChain)) return;

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Optional<String> tokenOptional = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            if (jwtProvider.validateToken(token)) {
                setAuthentication(token);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Empty token");
        }
    }

    private boolean ignoringUrl(ServletRequest servletRequest,
                                ServletResponse servletResponse,
                                FilterChain filterChain) throws IOException, ServletException {
        String url = ((HttpServletRequest) servletRequest).getRequestURL().toString();
        boolean isUrlIgnoring = IGNORING_URLS.stream()
                .anyMatch(ignoring -> url.endsWith(contextPath + ignoring));
        if (isUrlIgnoring) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        return isUrlIgnoring;
    }

    private void setAuthentication(String token) {
        String username = jwtProvider.getUsernameFromToken(token);
        CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return Optional.of(bearer.substring(7));
        }
        return Optional.empty();
    }
}
