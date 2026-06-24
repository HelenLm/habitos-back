package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Habito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Integer> {
    // Método personalizado para buscar todos los hábitos de un usuario en específico
    List<Habito> findByUsuarioIdUsuario(Integer idUsuario);
}