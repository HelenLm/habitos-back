package com.proyecto.habitos.controller;

import com.proyecto.habitos.dto.EmailRequestDTO;
import com.proyecto.habitos.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailRequestDTO request) {
        try {
            // Llamamos al método dinámico que agregamos en el paso anterior
            emailService.enviarCorreoDinamico(
                    request.destinatario(),
                    request.asunto(),
                    request.mensaje()
            );
            return ResponseEntity.ok("Correo enviado exitosamente a " + request.destinatario());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el envío de correo: " + e.getMessage());
        }
    }
}