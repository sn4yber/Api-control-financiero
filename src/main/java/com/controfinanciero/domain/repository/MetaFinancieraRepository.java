package com.controfinanciero.domain.repository;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;

import java.util.List;
import java.util.Optional;

/**
 * Port: Repositorio de Meta Financiera.
 * Define las operaciones de persistencia para metas.
 * La implementación estará en la capa de infraestructura.
 */
public interface MetaFinancieraRepository {

    /**
     * Guarda una nueva meta o actualiza una existente.
     */
    MetaFinanciera save(MetaFinanciera meta);

    /**
     * Busca una meta por su ID.
     */
    Optional<MetaFinanciera> findById(Long id);

    /**
     * Lista todas las metas de un usuario.
     */
    List<MetaFinanciera> findByUsuarioId(Long usuarioId);

    /**
     * Lista metas de un usuario por estado.
     */
    List<MetaFinanciera> findByUsuarioIdAndEstado(Long usuarioId, EstadoMeta estado);

    /**
     * Lista metas activas de un usuario.
     */
    List<MetaFinanciera> findActivasByUsuarioId(Long usuarioId);

    /**
     * Lista metas completadas de un usuario.
     */
    List<MetaFinanciera> findCompletadasByUsuarioId(Long usuarioId);

    /**
     * Lista metas de un usuario por prioridad.
     */
    List<MetaFinanciera> findByUsuarioIdAndPrioridad(Long usuarioId, Prioridad prioridad);

    /**
     * Lista metas de un usuario ordenadas por prioridad (descendente).
     */
    List<MetaFinanciera> findByUsuarioIdOrderByPrioridadDesc(Long usuarioId);

    /**
     * Lista metas de un usuario ordenadas por fecha objetivo.
     */
    List<MetaFinanciera> findByUsuarioIdOrderByFechaObjetivo(Long usuarioId);

    /**
     * Elimina una meta por su ID.
     */
    void deleteById(Long id);

    /**
     * Cuenta metas activas de un usuario.
     */
    long countActivasByUsuarioId(Long usuarioId);

    /**
     * Cuenta metas completadas de un usuario.
     */
    long countCompletadasByUsuarioId(Long usuarioId);
}

