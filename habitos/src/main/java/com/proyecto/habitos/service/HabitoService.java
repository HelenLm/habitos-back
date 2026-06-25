package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.model.Registro;
import com.proyecto.habitos.repository.HabitoRepository;
import com.proyecto.habitos.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 Importante para asegurar los cambios
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitoService {

    private final HabitoRepository habitoRepository;
    private final RegistroRepository registroRepository;

    // 🎯 1. Cambiado a Integer e incluyendo el guion bajo del repositorio
    public List<Habito> obtenerPorUsuario(Integer idUsuario) {
        List<Habito> habitos = habitoRepository.findByUsuario_IdUsuario(idUsuario);
        LocalDate hoy = LocalDate.now();

        for (Habito h : habitos) {
            Optional<Registro> regHoy = registroRepository.findByHabitoIdHabitoAndFecha(h.getIdHabito(), hoy);

            h.setCompletadoHoy(regHoy.isPresent() && "COMPLETADO".equalsIgnoreCase(regHoy.get().getEstado()));
            h.setRacha(h.getCompletadoHoy() ? 1 : 0);
        }
        return habitos;
    }

    @Transactional // 👈 Agrega esto para que los inserts/deletes de registros se guarden de inmediato
    public Habito alternarEstado(Integer idHabito) {
        // Buscamos directo con el Integer sin hacer conversiones a .longValue()
        Habito habito = obtenerPorId(idHabito);
        LocalDate hoy = LocalDate.now();

        Optional<Registro> registroExistente = registroRepository.findByHabitoIdHabitoAndFecha(idHabito, hoy);

        if (registroExistente.isPresent()) {
            registroRepository.delete(registroExistente.get());
            habito.setCompletadoHoy(false);
            habito.setRacha(0);
        } else {
            Registro nuevoRegistro = new Registro();
            nuevoRegistro.setHabito(habito);
            nuevoRegistro.setFecha(hoy);
            nuevoRegistro.setEstado("COMPLETADO");

            registroRepository.save(nuevoRegistro);

            habito.setCompletadoHoy(true);
            habito.setRacha(1);
        }

        // Modificamos el estado transaccional del hábito
        return habitoRepository.save(habito);
    }

    public Habito guardar(Habito habito) {
        return habitoRepository.save(habito);
    }

    // 🎯 2. Cambiado a Integer para que coincida con Yugabyte
    public Habito obtenerPorId(Integer id) {
        return habitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito no encontrado con ID: " + id));
    }

    // 🎯 3. Cambiado a Integer
    public void eliminar(Integer id) {
        habitoRepository.deleteById(id);
    }


}