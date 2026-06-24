package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.model.Registro;
import com.proyecto.habitos.repository.HabitoRepository;
import com.proyecto.habitos.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitoService {

    private final HabitoRepository habitoRepository;
    private final RegistroRepository registroRepository;

    public List<Habito> obtenerPorUsuario(Integer idUsuario) {
        List<Habito> habitos = habitoRepository.findByUsuarioIdUsuario(idUsuario);
        LocalDate hoy = LocalDate.now();

        for (Habito h : habitos) {
            // 🎯 Usamos tu método .findByHabitoIdHabitoAndFecha
            Optional<Registro> regHoy = registroRepository.findByHabitoIdHabitoAndFecha(h.getIdHabito(), hoy);

            // Si el registro existe y su estado es "COMPLETADO", mandamos true a Angular
            h.setCompletadoHoy(regHoy.isPresent() && "COMPLETADO".equalsIgnoreCase(regHoy.get().getEstado()));

            h.setRacha(h.getCompletadoHoy() ? 1 : 0);
        }
        return habitos;
    }

    public Habito alternarEstado(Integer idHabito) {
        Habito habito = obtenerPorId(idHabito);
        LocalDate hoy = LocalDate.now();

        // 🎯 Buscamos por tu columna "fecha"
        Optional<Registro> registroExistente = registroRepository.findByHabitoIdHabitoAndFecha(idHabito, hoy);

        if (registroExistente.isPresent()) {
            // Si ya existía el registro de hoy, lo eliminamos (el usuario lo desmarcó)
            registroRepository.delete(registroExistente.get());
            habito.setCompletadoHoy(false);
            habito.setRacha(0);
        } else {
            // Si no existía, creamos tu objeto Registro con sus campos reales 🎯
            Registro nuevoRegistro = new Registro();
            nuevoRegistro.setHabito(habito);
            nuevoRegistro.setFecha(hoy);          // Tu campo fecha
            nuevoRegistro.setEstado("COMPLETADO"); // Tu campo estado (String)

            registroRepository.save(nuevoRegistro);

            habito.setCompletadoHoy(true);
            habito.setRacha(1);
        }

        return habito;
    }

    public Habito guardar(Habito habito) {
        return habitoRepository.save(habito);
    }

    public Habito obtenerPorId(Integer id) {
        return habitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito no encontrado con ID: " + id));
    }

    public void eliminar(Integer id) {
        habitoRepository.deleteById(id);
    }
}