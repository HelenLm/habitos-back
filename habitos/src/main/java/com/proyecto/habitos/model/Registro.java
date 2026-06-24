package com.proyecto.habitos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "registro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Integer idRegistro;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    // Relación con Habito (Muchos registros de seguimiento pertenecen a un Hábito)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_habito", nullable = false)
    private Habito habito;
}