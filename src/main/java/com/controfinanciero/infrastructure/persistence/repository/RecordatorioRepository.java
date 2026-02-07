package com.controfinanciero.infrastructure.persistence.repository;

import com.controfinanciero.infrastructure.persistence.entity.RecordatorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordatorioRepository extends JpaRepository<RecordatorioEntity, Long> {

    List<RecordatorioEntity> findByUsuarioIdAndActivoTrue(Long usuarioId);

    List<RecordatorioEntity> findByUsuarioIdAndPagadoFalseAndActivoTrue(Long usuarioId);

    @Query("SELECT r FROM RecordatorioEntity r WHERE r.activo = true AND r.pagado = false " +
           "AND r.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    List<RecordatorioEntity> findProximosPagos(LocalDate fechaInicio, LocalDate fechaFin);
}

