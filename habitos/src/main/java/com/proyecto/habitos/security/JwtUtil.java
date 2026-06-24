package com.proyecto.habitos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import com.proyecto.habitos.model.Usuario;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Una clave secreta robusta de al menos 256 bits para firmar los tokens de forma segura
    private final String SECRET_KEY = "MiClaveSecretaSsuperSeguraParaElProyectoDeHabitosEscom2026";

    // El token expirará en 24 horas (en milisegundos)
    private final long EXPIRATION_TIME = 86400000;

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 🎯 Cambiamos el parámetro para recibir al objeto Usuario completo
    public String generarToken(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("idUsuario", usuario.getIdUsuario());

        return Jwts.builder()
                .claims(extraClaims) // 🎯 PRIMERO LOS CLAIMS
                .subject(usuario.getCorreo()) // 🎯 LUEGO EL SUBJECT (Así no se pisa)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // Extraer el correo del usuario de adentro del token
    public String extraerCorreo(String token) {
        return extraerTodosLosClaims(token).getSubject();
    }

    // Verificar si el token ya caducó
    public boolean esTokenValido(String token, String correoUsuario) {
        final String correoToken = extraerCorreo(token);
        return (correoToken.equals(correoUsuario) && !esTokenExpirado(token));
    }

    private boolean esTokenExpirado(String token) {
        return extraerTodosLosClaims(token).getExpiration().before(new Date());
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}