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

    // 🔹 Ajouter ici les endpoints publics
    private static final String[] PUBLIC_URLS = {
            "/api/auth/",
            "/api/carburants/",
            "/error"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔹 Vérifie si le chemin est public
        boolean isPublic = false;
        for (String url : PUBLIC_URLS) {
            if (path.startsWith(url)) {
                isPublic = true;
                break;
            }
        }

        if (!isPublic) {
            String authHeader = request.getHeader("Authorization");
            try {
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
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
                }
            } catch (Exception e) {
                System.out.println("JWT processing failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
