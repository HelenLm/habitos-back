package com.proyecto.habitos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el encabezado llamado Authorization
        final String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        final String jwt;
        final String userEmail;

        // 🎯 MODIFICADO: Si es la raíz exacta "/" o empieza con "/api/auth/", ignoramos la validación de JWT
        if (path.equals("/") || path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Si no viene el token o no empieza con "Bearer ", ignoramos y dejamos pasar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cortamos la palabra "Bearer "
        jwt = authHeader.substring(7);
        try {
            userEmail = jwtUtil.extraerCorreo(jwt);

            // 4. Si encontramos el correo y el contexto de seguridad está libre
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.esTokenValido(jwt, userEmail)) {

                    // Creamos el token de autenticación pasándole el email como principal
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            new ArrayList<>() // Lista vacía de roles/autoridades
                    );

                    // Es crucial asociar los detalles de la petición actual (IP, sesión, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Seteamos la autenticación en el contexto global
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println(">>> FILTRO JWT: No se pudo validar el token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}