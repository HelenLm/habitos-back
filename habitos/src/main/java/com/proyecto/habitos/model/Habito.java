package com.proyecto.habitos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habito")
    private Integer idHabito;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Relación con Usuario (Muchas hábitos pertenecen a un Usuario)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Relación con Categoria (Muchos hábitos pertenecen a una Categoría)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Transient
    private Boolean completadoHoy = false;

    @Transient
    private Integer racha = 0;

    // Métodos explícitos
    public Boolean getCompletadoHoy() {
        return completadoHoy != null ? completadoHoy : false;
    }

    public void setCompletadoHoy(Boolean completadoHoy) {
        this.completadoHoy = completadoHoy;
    }

    public Integer getRacha() {
        return racha != null ? racha : 0;
    }

    public void setRacha(Integer racha) {
        this.racha = racha;
    }
}