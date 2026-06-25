package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.repository.UsuarioRepository;
import com.proyecto.habitos.service.UsuarioService;
import com.proyecto.habitos.service.PdfService;
import com.proyecto.habitos.service.HabitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://habitos-front.netlify.app")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PdfService pdfService; // Ambas inyecciones juntas en la parte superior
    private final UsuarioRepository usuarioRepository;
    private final HabitoService habitoService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> descargarReportePdf(@PathVariable Long id) { // 🎯 Cambiado a Integer para que coincida con tus IDs nativos

        // 1. Buscamos el usuario por su ID usando tu servicio
        Usuario usuario = usuarioService.obtenerPorId(id);

        // 2. Buscamos la lista de hábitos de ese usuario específico
        List<Habito> habitos = habitoService.obtenerPorUsuario(usuario.getIdUsuario());

        // 3. Generamos el stream binario del PDF pasándole AMBOS argumentos 🎯
        ByteArrayInputStream pdfStream = pdfService.generarReporteUsuario(usuario, habitos);

        // 4. Configuramos las cabeceras HTTP de descarga
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tracker-usuario-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<InputStreamResource> descargarReportePerfil(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String correo = principal.getName();
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🎯 Obtenemos la lista de hábitos reales del usuario
        List<Habito> habitos = habitoService.obtenerPorUsuario(usuario.getIdUsuario());

        // 🎯 Se los pasamos al nuevo constructor del PdfService
        ByteArrayInputStream pdfFlujo = pdfService.generarReporteUsuario(usuario, habitos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tracker_mensual.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfFlujo));
    }

    @PutMapping(value = "/{id}/foto", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> subirFotoPerfil(
            @PathVariable Long id,
            @RequestPart("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(usuarioService.actualizarFotoPerfil(id, archivo));
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(usuario.getFotoPerfil());
    }
}