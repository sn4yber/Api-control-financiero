package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Port: Repositorio de Movimiento Financiero.
 * Define las operaciones de persistencia para movimientos.
 * La implementación estará en la capa de infraestructura.
 */
public interface MovimientoFinancieroRepository {

    /**
     * Guarda un nuevo movimiento o actualiza uno existente.
     */
    MovimientoFinanciero save(MovimientoFinanciero movimiento);

    /**
     * Busca un movimiento por su ID.
     */
    Optional<MovimientoFinanciero> findById(Long id);

    /**
     * Lista todos los movimientos de un usuario.
     */
    List<MovimientoFinanciero> findByUsuarioId(Long usuarioId);

    /**
     * Lista movimientos de un usuario en un rango de fechas.
     */
    List<MovimientoFinanciero> findByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Lista movimientos de un usuario en un rango de fechas (alias para consistencia).
     */
    default List<MovimientoFinanciero> findByUsuarioIdAndFechaMovimientoBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        return findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
    }

    /**
     * Lista movimientos de un usuario en un rango de fechas ordenados por fecha descendente.
     * Usado para reportes y exportación.
     */
    default List<MovimientoFinanciero> findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        // La implementación real estará en el adapter de JPA
        // Este método default no se usa, pero satisface la interfaz
        return findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
    }

    /**
     * Lista movimientos de un usuario por tipo en un rango de fechas.
     */
    List<MovimientoFinanciero> findByUsuarioIdAndTipoMovimientoAndFechaMovimientoBetween(
            Long usuarioId,
            TipoMovimiento tipo,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Lista movimientos de un usuario por tipo.
     */
    List<MovimientoFinanciero> findByUsuarioIdAndTipo(Long usuarioId, TipoMovimiento tipo);

    /**
     * Lista movimientos de un usuario por categoría.
     */
    List<MovimientoFinanciero> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    /**
     * Lista movimientos de un usuario vinculados a una meta.
     */
    List<MovimientoFinanciero> findByUsuarioIdAndMetaId(Long usuarioId, Long metaId);

    /**
     * Calcula el total de ingresos de un usuario en un rango de fechas.
     */
    BigDecimal sumIngresosByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Calcula el total de gastos de un usuario en un rango de fechas.
     */
    BigDecimal sumGastosByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Calcula el total de ahorros de un usuario en un rango de fechas.
     */
    BigDecimal sumAhorrosByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Lista movimientos recurrentes de un usuario.
     */
    List<MovimientoFinanciero> findRecurrentesByUsuarioId(Long usuarioId);

    /**
     * Elimina un movimiento por su ID.
     */
    void deleteById(Long id);

    /**
     * Cuenta movimientos de un usuario.
     */
    long countByUsuarioId(Long usuarioId);
}

