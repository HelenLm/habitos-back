package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.repository.UsuarioRepository;
import com.proyecto.habitos.security.AuthRequest;
import com.proyecto.habitos.security.AuthResponse;
import com.proyecto.habitos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // 1. Buscar usuario por correo
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(request.getCorreo()))
                .findFirst()
                .orElse(null);

        // 2. Validar si existe y si la contraseña coincide (usando matchers de BCrypt o texto plano temporal)
        if (usuario != null && (request.getPassword().equals(usuario.getPassword()) || passwordEncoder.matches(request.getPassword(), usuario.getPassword()))) {
            // 3. Generar token y responder
            String token = jwtUtil.generarToken(usuario);
            return ResponseEntity.ok(new AuthResponse(token));
        }

        // Si falla, devolvemos un 401 Unauthorized sin dar pistas
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}