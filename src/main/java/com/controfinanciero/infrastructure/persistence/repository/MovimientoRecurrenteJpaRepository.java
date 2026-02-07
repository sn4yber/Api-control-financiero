package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.MovimientoRecurrenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRecurrenteJpaRepository extends JpaRepository<MovimientoRecurrenteEntity, Long> {

    List<MovimientoRecurrenteEntity> findByUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Encuentra movimientos recurrentes que deben ejecutarse HOY o antes
     */
    @Query("SELECT m FROM MovimientoRecurrenteEntity m WHERE m.activo = true AND m.proximaEjecucion <= :fecha")
    List<MovimientoRecurrenteEntity> findPendientesDeEjecucion(LocalDate fecha);

    /**
     * Encuentra todos los movimientos recurrentes activos
     */
    List<MovimientoRecurrenteEntity> findByActivoTrue();
}

