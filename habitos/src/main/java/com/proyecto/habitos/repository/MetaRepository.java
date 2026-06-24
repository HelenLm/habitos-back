package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByHabitoIdHabito(Integer idHabito);
}