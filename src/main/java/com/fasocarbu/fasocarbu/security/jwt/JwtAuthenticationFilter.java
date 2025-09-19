package com.fasocarbu.fasocarbu.security.jwt;

import com.fasocarbu.fasocarbu.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // ✅ Endpoints publics qui ne nécessitent pas de JWT
    private static final String[] PUBLIC_URLS = {
            "/api/auth/forgot-password",
            "/api/auth/reset-password",
            "/api/auth/login",
            "/api/auth/register",
            "/api/carburants"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔹 Ignore les routes publiques
        for (String url : PUBLIC_URLS) {
            if (path.startsWith(url)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 🔹 JWT validation pour les autres routes
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String username = jwtUtils.getUsernameFromJwtToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtils.validateJwtToken(token)) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                System.out.println("JWT processing failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
