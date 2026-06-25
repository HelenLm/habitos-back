package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Categoria;
import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.repository.CategoriaRepository;
import com.proyecto.habitos.repository.UsuarioRepository; // 🎯 IMPORTADO
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://habitos-front.netlify.app")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository; // 🎯 DECLARADO PARA INYECCIÓN

    // 🎯 GET: Obtener las categorías privadas del usuario logueado
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String correo = principal.getName();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        // Buscamos solo las categorías de este usuario específico en YugabyteDB
        // 🎯 Pasamos el ID directo porque tu repositorio espera un Long nativo
        // 🎯 LA VIEJA CONFIABLE: Convierte a texto y luego al tipo exacto que pide el repositorio
        List<Categoria> lista = categoriaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        return ResponseEntity.ok(lista);
    }

    // 🎯 POST: Crear una nueva categoría
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria nuevaCategoria, java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Sesión inválida.");
        }

        try {
            // 1. Buscamos al usuario dueño de la sesión
            String correoUsuarioLogueado = principal.getName();
            Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoUsuarioLogueado)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            // 2. Le asignamos el usuario a la nueva categoría 🎯
            nuevaCategoria.setUsuario(usuario);

            // 3. Ahora sí guardamos en la base de datos
            Categoria guardada = categoriaRepository.save(nuevaCategoria);
            return ResponseEntity.ok(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar categoría: " + e.getMessage());
        }
    }

    // 🎯 DELETE: Eliminar una categoría por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            if (!categoriaRepository.existsById(id)) {
                return ResponseEntity.status(404).body("La categoría no existe.");
            }
            categoriaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar la categoría: " + e.getMessage());
        }
    }
}