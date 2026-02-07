package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad JPA: Racha de actividad del usuario
 */
@Entity
@Table(name = "racha_ahorro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RachaAhorroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long usuarioId;

    @Column(name = "racha_actual", nullable = false)
    private Integer rachaActual = 0;

    @Column(name = "racha_maxima", nullable = false)
    private Integer rachaMaxima = 0;

    @Column(name = "ultima_actividad")
    private LocalDate ultimaActividad;

    @Column(name = "dias_consecutivos")
    private Integer diasConsecutivos = 0;

    public void incrementarRacha() {
        this.rachaActual++;
        this.diasConsecutivos++;
        if (this.rachaActual > this.rachaMaxima) {
            this.rachaMaxima = this.rachaActual;
        }
        this.ultimaActividad = LocalDate.now();
    }

    public void reiniciarRacha() {
        this.rachaActual = 0;
        this.diasConsecutivos = 0;
    }

    public boolean debeReiniciarse() {
        if (ultimaActividad == null) return false;
        return LocalDate.now().minusDays(1).isAfter(ultimaActividad);
    }
}

