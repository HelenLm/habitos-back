package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 🎯 Este método busca al usuario ignorando si escriben con mayúsculas o minúsculas
    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    // Si manejas el método anterior para el Login, déjalo aquí también
    Optional<Usuario> findByCorreo(String correo);
}