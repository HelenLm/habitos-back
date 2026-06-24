package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService; // Inyectamos el servicio de correo

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setFechaRegistro(LocalDate.now());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // REQUERIMIENTO: Al guardar con éxito en YugabyteDB, gatillamos el envío del correo
        emailService.enviarCorreoBienvenida(usuarioGuardado.getCorreo(), usuarioGuardado.getNombre());

        return usuarioGuardado;
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario actualizarFotoPerfil(Integer idUsuario, MultipartFile archivo) {
        Usuario usuario = obtenerPorId(idUsuario);
        try {
            usuario.setFotoPerfil(archivo.getBytes()); // Convertimos el archivo a un arreglo de bytes
            return usuarioRepository.save(usuario);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar los bytes del archivo: " + e.getMessage());
        }
    }
}