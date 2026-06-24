package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Registro;
import com.proyecto.habitos.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroRepository registroRepository;

    public List<Registro> obtenerPorHabito(Integer idHabito) {
        return registroRepository.findByHabitoIdHabito(idHabito);
    }

    public Registro guardar(Registro registro) {
        return registroRepository.save(registro);
    }
}