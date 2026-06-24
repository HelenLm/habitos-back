package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.service.HabitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/habitos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class HabitoController {

    private final HabitoService habitoService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Habito>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(habitoService.obtenerPorUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Habito> crearHabito(@RequestBody Habito habito) {
        return ResponseEntity.ok(habitoService.guardar(habito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabito(@PathVariable Integer id) {
        habitoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/alternar")
    public ResponseEntity<Habito> alternarHabito(@PathVariable Integer id) {
        // Llama de forma directa al servicio que repara los estados e historiales en YugabyteDB
        return ResponseEntity.ok(habitoService.alternarEstado(id));
    }
}