package com.proyecto.habitos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "meta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meta")
    private Integer idMeta;

    @Column(name = "objetivo", nullable = false, length = 255)
    private String objetivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    // Relación con Habito (Muchas metas pueden pertenecer a un Hábito)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_habito", nullable = false)
    private Habito habito;
}