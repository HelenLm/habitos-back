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

        // 2. Validar si existe y si la contraseña coincide
        if (usuario != null && (request.getPassword().equals(usuario.getPassword()) || passwordEncoder.matches(request.getPassword(), usuario.getPassword()))) {
            // 3. Generar token y responder
            String token = jwtUtil.generarToken(usuario);
            return ResponseEntity.ok(new AuthResponse(token));
        }

        // Si falla, devolvemos un 401 Unauthorized sin dar pistas
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String email = request.get("email");
        String password = request.get("password");

        // 1. Validar si el correo ya existe
        boolean existe = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getCorreo().equalsIgnoreCase(email));

        if (existe) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "El correo electrónico ya está registrado.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // 2. Crear el nuevo usuario y setear sus datos
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCorreo(email);

        // 🎯 ¡AQUÍ ESTÁ LA SOLUCIÓN! Le asignamos la fecha y hora actual del sistema
        nuevoUsuario.setFechaRegistro(java.time.LocalDate.now());
        // 💡 Nota: Si en tu modelo 'fechaRegistro' es de tipo LocalDateTime, usa:
        // nuevoUsuario.setFechaRegistro(java.time.LocalDateTime.now());

        // Encriptamos la contraseña con BCrypt
        nuevoUsuario.setPassword(passwordEncoder.encode(password));

        // 3. Guardar en YugabyteDB
        usuarioRepository.save(nuevoUsuario);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("mensaje", "¡Usuario creado exitosamente!");
        return ResponseEntity.ok(successResponse);
    }

}