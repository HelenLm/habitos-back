package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Meta;
import com.proyecto.habitos.service.MetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/metas")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://habitos-front.netlify.app")
public class MetaController {

    private final MetaService metaService;

    @GetMapping("/habito/{idHabito}")
    public ResponseEntity<List<Meta>> listarPorHabito(@PathVariable Integer idHabito) {
        return ResponseEntity.ok(metaService.obtenerPorHabito(idHabito));
    }

    @PostMapping
    public ResponseEntity<Meta> crearMeta(@RequestBody Meta meta) {
        return ResponseEntity.ok(metaService.guardar(meta));
    }
}