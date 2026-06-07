package com.unir.socialhabits.security;

import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path =
                request.getRequestURI();

        System.out.println(
                "PATH -> " + path
        );

        // Skip auth endpoints

        if (path.startsWith("/auth/")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        System.out.println(
                "HEADER -> " + authHeader
        );

        if (
                authHeader == null ||
                        !authHeader.startsWith("Bearer ")
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        try {

            String token =
                    authHeader.substring(7);

            System.out.println(
                    "TOKEN -> " + token
            );

            String email =
                    jwtService.extractUsername(
                            token
                    );

            System.out.println(
                    "EMAIL -> " + email
            );

            if (
                    email != null &&
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    == null
            ) {

                UserDetails userDetails =

                        userDetailsService
                                .loadUserByUsername(
                                        email
                                );

                System.out.println(
                        "USER FOUND -> " +
                                userDetails.getUsername()
                );

                if (
                        jwtService.isValid(
                                token
                        )
                ) {

                    UsernamePasswordAuthenticationToken auth =

                            new UsernamePasswordAuthenticationToken(

                                    userDetails,

                                    null,

                                    userDetails.getAuthorities()

                            );

                    auth.setDetails(

                            new WebAuthenticationDetailsSource()

                                    .buildDetails(request)

                    );

                    SecurityContextHolder

                            .getContext()

                            .setAuthentication(auth);

                    System.out.println(
                            "AUTHENTICATED"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT ERROR -> " +
                            e.getMessage()
            );
        }

        System.out.println(
                "SECURITY CONTEXT -> " +
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
        );

        filterChain.doFilter(
                request,
                response
        );
    }
}