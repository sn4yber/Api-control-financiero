package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.FuenteIngreso;
import com.controfinanciero.domain.model.enums.TipoFuente;

import java.util.List;
import java.util.Optional;

/**
 * Port: Repositorio de Fuente de Ingreso.
 * Define las operaciones de persistencia para fuentes de ingreso.
 * La implementación estará en la capa de infraestructura.
 */
public interface FuenteIngresoRepository {

    /**
     * Guarda una nueva fuente de ingreso o actualiza una existente.
     */
    FuenteIngreso save(FuenteIngreso fuenteIngreso);

    /**
     * Busca una fuente de ingreso por su ID.
     */
    Optional<FuenteIngreso> findById(Long id);

    /**
     * Lista todas las fuentes de ingreso de un usuario.
     */
    List<FuenteIngreso> findByUsuarioId(Long usuarioId);

    /**
     * Lista fuentes de ingreso activas de un usuario.
     */
    List<FuenteIngreso> findActivasByUsuarioId(Long usuarioId);

    /**
     * Lista fuentes de ingreso por usuario y estado activo.
     */
    List<FuenteIngreso> findByUsuarioIdAndActiva(Long usuarioId, boolean activa);

    /**
     * Lista fuentes de ingreso de un usuario por tipo.
     */
    List<FuenteIngreso> findByUsuarioIdAndTipo(Long usuarioId, TipoFuente tipo);

    /**
     * Lista fuentes de ingreso reales (no préstamos) de un usuario.
     */
    List<FuenteIngreso> findIngresosRealesByUsuarioId(Long usuarioId);

    /**
     * Busca una fuente de ingreso por nombre de usuario.
     */
    Optional<FuenteIngreso> findByUsuarioIdAndNombre(Long usuarioId, String nombre);

    /**
     * Verifica si existe una fuente con ese nombre para un usuario.
     */
    boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre);

    /**
     * Elimina una fuente de ingreso por su ID.
     */
    void deleteById(Long id);

    /**
     * Cuenta fuentes de ingreso activas de un usuario.
     */
    long countActivasByUsuarioId(Long usuarioId);
}

