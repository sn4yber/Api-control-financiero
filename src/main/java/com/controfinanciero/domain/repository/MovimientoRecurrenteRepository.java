package com.controfinanciero.domain.repository;

import com.controfinanciero.infrastructure.persistence.entity.MovimientoRecurrenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRecurrenteRepository extends JpaRepository<MovimientoRecurrenteEntity, Long> {

    List<MovimientoRecurrenteEntity> findByUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Encuentra movimientos recurrentes que deben ejecutarse HOY
     */
    @Query("SELECT m FROM MovimientoRecurrenteEntity m WHERE m.activo = true AND m.proximaEjecucion <= :fecha")
    List<MovimientoRecurrenteEntity> findPendientesDeEjecucion(LocalDate fecha);

    @Query("SELECT COUNT(m) FROM MovimientoRecurrenteEntity m WHERE m.usuarioId = :usuarioId AND m.activo = true")
    long countByUsuarioId(Long usuarioId);
}
