package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Meta;
import com.proyecto.habitos.repository.MetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;

    public List<Meta> obtenerPorHabito(Integer idHabito) {
        return metaRepository.findByHabitoIdHabito(idHabito);
    }

    public Meta guardar(Meta meta) {
        return metaRepository.save(meta);
    }
}