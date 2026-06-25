package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // El guion bajo resuelve la ambigüedad en la navegación de propiedades
    List<Categoria> findByUsuario_IdUsuario(Integer idUsuario);
}