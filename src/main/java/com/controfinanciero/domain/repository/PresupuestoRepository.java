package com.controfinanciero.domain.repository;

import com.controfinanciero.infrastructure.persistence.entity.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {

    List<PresupuestoEntity> findByUsuarioIdAndActivoTrue(Long usuarioId);

    Optional<PresupuestoEntity> findByUsuarioIdAndCategoriaIdAndPeriodo(Long usuarioId, Long categoriaId, String periodo);

    List<PresupuestoEntity> findByUsuarioIdAndPeriodoAndActivoTrue(Long usuarioId, String periodo);

    /**
     * Encuentra presupuestos excedidos o cerca del límite que no han enviado alerta
     */
    @Query("SELECT p FROM PresupuestoEntity p WHERE p.activo = true AND p.alertaEnviada = false " +
           "AND (p.gastoActual >= p.limiteMensual * 0.9)")
    List<PresupuestoEntity> findPresupuestosParaAlertar();
}

