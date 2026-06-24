package com.proyecto.habitos.controller;

import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.service.UsuarioService;
import com.proyecto.habitos.service.PdfService;
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
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PdfService pdfService; // Ambas inyecciones juntas en la parte superior

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> descargarReportePdf(@PathVariable Integer id) {
        // 1. Buscamos el usuario por su ID
        Usuario usuario = usuarioService.obtenerPorId(id);

        // 2. Generamos el stream binario del PDF
        ByteArrayInputStream pdfStream = pdfService.generarReporteUsuario(usuario);

        // 3. Configuramos las cabeceras HTTP de descarga
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=reporte-usuario-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    @PutMapping(value = "/{id}/foto", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> subirFotoPerfil(
            @PathVariable Integer id,
            @RequestPart("archivo") MultipartFile archivo) {
        return ResponseEntity.ok(usuarioService.actualizarFotoPerfil(id, archivo));
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(usuario.getFotoPerfil());
    }
}