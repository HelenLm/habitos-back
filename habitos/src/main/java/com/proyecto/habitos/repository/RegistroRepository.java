package com.proyecto.habitos.repository;

import com.proyecto.habitos.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Integer> {

    List<Registro> findByHabitoIdHabito(Integer idHabito);
    Optional<Registro> findByHabitoIdHabitoAndFecha(Integer idHabito, LocalDate fecha);
}