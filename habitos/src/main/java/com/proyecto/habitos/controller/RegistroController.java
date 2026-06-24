package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Registro;
import com.proyecto.habitos.service.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistroController {

    private final RegistroService registroService;

    @GetMapping("/habito/{idHabito}")
    public ResponseEntity<List<Registro>> listarPorHabito(@PathVariable Integer idHabito) {
        return ResponseEntity.ok(registroService.obtenerPorHabito(idHabito));
    }

    @PostMapping
    public ResponseEntity<Registro> crearRegistro(@RequestBody Registro registro) {
        return ResponseEntity.ok(registroService.guardar(registro));
    }
}