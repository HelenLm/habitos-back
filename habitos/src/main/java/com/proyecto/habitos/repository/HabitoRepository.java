package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Habito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 🎯 Cambiamos el tipo de ID de la entidad a Integer (segundo parámetro)
@Repository
public interface HabitoRepository extends JpaRepository<Habito, Integer> {

    // 🎯 Declaramos el método que le falta a tu servicio
    List<Habito> findByUsuario_IdUsuario(Integer idUsuario);
}