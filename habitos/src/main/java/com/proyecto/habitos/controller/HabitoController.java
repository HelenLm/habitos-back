package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.repository.HabitoRepository;
import com.proyecto.habitos.repository.UsuarioRepository;
import com.proyecto.habitos.service.HabitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/habitos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HabitoController {

    private final HabitoRepository habitoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitoService habitoService;

    // 🎯 GET: Traer los hábitos privados del usuario logueado
    @GetMapping
    public ResponseEntity<List<Habito>> obtenerMisHabitos(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String correoUsuarioLogueado = principal.getName();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoUsuarioLogueado)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        // 🎯 SOLUCIÓN: Convertimos el Long del usuario a Integer para el HabitoRepository
        // 🎯 Quitamos el .intValue() para que viaje como Long limpio
        // 🎯 LA VIEJA CONFIABLE: Adapta el ID al tipo exacto del repositorio de hábitos
        List<Habito> habitosPropios = habitoService.obtenerPorUsuario(usuario.getIdUsuario());
        return ResponseEntity.ok(habitosPropios);
    }

    // 🎯 POST: Guardar un hábito amarrándolo a su verdadero dueño
    @PostMapping
    public ResponseEntity<?> crearHabito(@RequestBody Habito nuevoHabito, java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Sesión inválida.");
        }

        String correoUsuarioLogueado = principal.getName();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoUsuarioLogueado)
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuario no encontrado.");
        }

        nuevoHabito.setUsuario(usuario);
        Habito guardado = habitoRepository.save(nuevoHabito);
        return ResponseEntity.ok(guardado);
    }

    // 🎯 PUT: Alternar estado del hábito (Marcar/Desmarcar)
    @PutMapping("/{idHabito}/alternar")
    public ResponseEntity<?> alternarEstadoHabito(@PathVariable Integer idHabito, java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Sesión inválida.");
        }

        try {
            Habito actualizado = habitoService.alternarEstado(idHabito);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al alternar estado: " + e.getMessage());
        }
    }
}