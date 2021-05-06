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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends GenericFilterBean {

    private final String AUTHORIZATION = "Authorization";
    private final List<String> IGNORING_PATHS = Arrays.asList("/register", "/auth");
    private final List<String> IGNORING_URLS;

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final String contextPath;

    public JwtFilter(JwtProvider jwtProvider,
                     CustomUserDetailsService userDetailsService,
                     @Value("${server.servlet.context-path}") String contextPath) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.contextPath = contextPath;
        IGNORING_URLS = new ArrayList<>();
        IGNORING_PATHS.forEach(path -> IGNORING_URLS.add(this.contextPath + path));
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (isWhitelisted(servletRequest, response)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Optional<String> tokenOptional =
                getTokenFromRequest((HttpServletRequest) servletRequest)
                        .filter(jwtProvider::validateToken);

        if (tokenOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No valid JWT token found");
            return;
        }

        setAuthentication(tokenOptional.get());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhitelisted(ServletRequest servletRequest,
                                  HttpServletResponse response) throws IOException {
        try {
            String url = ((HttpServletRequest) servletRequest).getRequestURL().toString();
            String path = new URL(url).getPath();
            return IGNORING_URLS.contains(path);
        } catch (MalformedURLException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization error");
            return false;
        }
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
