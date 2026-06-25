package com.proyecto.habitos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
public class RootController { // 🎯 Controlador independiente SIN @RequestMapping en la clase

    @GetMapping("/") // Ahora sí, esto mapea directo a la raíz principal del servidor
    public Map<String, String> saludoRevision() {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "ONLINE");
        response.put("proyecto", "Sistema de Seguimiento de Habitos - Backend");
        response.put("mensaje", "Servidor desplegado con exito en Render y conectado a YugabyteDB.");
        response.put("documentacion", "https://habitos-back-5gll.onrender.com/swagger-ui.html");
        return response;
    }
}