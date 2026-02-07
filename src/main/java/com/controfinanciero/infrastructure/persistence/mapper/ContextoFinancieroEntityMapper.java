package com.controfinanciero.infrastructure.persistence.mapper;

import com.controfinanciero.domain.model.ContextoFinanciero;
import com.controfinanciero.infrastructure.persistence.entity.ContextoFinancieroEntity;

/**
 * Mapper para convertir entre ContextoFinanciero (Domain) y ContextoFinancieroEntity (Persistence).
 */
public class ContextoFinancieroEntityMapper {

    /**
     * Convierte de Entity a Domain.
     */
    public static ContextoFinanciero toDomain(ContextoFinancieroEntity entity) {
        if (entity == null) {
            return null;
        }

        ContextoFinanciero contexto = new ContextoFinanciero(
                entity.getUserId(),
                entity.getIncomeType()
        );

        // Usar reflexión para establecer campos privados
        try {
            var idField = ContextoFinanciero.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(contexto, entity.getId());

            var tieneIngresoVariableField = ContextoFinanciero.class.getDeclaredField("tieneIngresoVariable");
            tieneIngresoVariableField.setAccessible(true);
            tieneIngresoVariableField.set(contexto, entity.getHasVariableIncome());

            var porcentajeField = ContextoFinanciero.class.getDeclaredField("porcentajeAhorroDeseado");
            porcentajeField.setAccessible(true);
            porcentajeField.set(contexto, entity.getDesiredSavingsPercentage());

            var periodoField = ContextoFinanciero.class.getDeclaredField("periodoAnalisis");
            periodoField.setAccessible(true);
            periodoField.set(contexto, entity.getAnalysisPeriod());

            var diasField = ContextoFinanciero.class.getDeclaredField("diasPeriodoPersonalizado");
            diasField.setAccessible(true);
            diasField.set(contexto, entity.getCustomPeriodDays());

            var monedaField = ContextoFinanciero.class.getDeclaredField("codigoMoneda");
            monedaField.setAccessible(true);
            monedaField.set(contexto, entity.getCurrencyCode());

            var createdAtField = ContextoFinanciero.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(contexto, entity.getCreatedAt());

            var updatedAtField = ContextoFinanciero.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(contexto, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear ContextoFinancieroEntity a ContextoFinanciero", e);
        }

        return contexto;
    }

    /**
     * Convierte de Domain a Entity.
     */
    public static ContextoFinancieroEntity toEntity(ContextoFinanciero contexto) {
        if (contexto == null) {
            return null;
        }

        ContextoFinancieroEntity entity = new ContextoFinancieroEntity(
                contexto.getUsuarioId(),
                contexto.getTipoIngreso()
        );

        // Establecer campos adicionales
        if (contexto.getId() != null) {
            entity.setId(contexto.getId());
        }
        entity.setHasVariableIncome(contexto.isTieneIngresoVariable());
        entity.setDesiredSavingsPercentage(contexto.getPorcentajeAhorroDeseado());
        entity.setAnalysisPeriod(contexto.getPeriodoAnalisis());
        entity.setCustomPeriodDays(contexto.getDiasPeriodoPersonalizado());
        entity.setCurrencyCode(contexto.getCodigoMoneda());

        if (contexto.getCreatedAt() != null) {
            entity.setCreatedAt(contexto.getCreatedAt());
        }
        if (contexto.getUpdatedAt() != null) {
            entity.setUpdatedAt(contexto.getUpdatedAt());
        }

        return entity;
    }

    /**
     * Actualiza una Entity existente con datos del Domain.
     */
    public static void updateEntity(ContextoFinanciero contexto, ContextoFinancieroEntity entity) {
        if (contexto == null || entity == null) {
            return;
        }

        entity.setIncomeType(contexto.getTipoIngreso());
        entity.setHasVariableIncome(contexto.isTieneIngresoVariable());
        entity.setDesiredSavingsPercentage(contexto.getPorcentajeAhorroDeseado());
        entity.setAnalysisPeriod(contexto.getPeriodoAnalisis());
        entity.setCustomPeriodDays(contexto.getDiasPeriodoPersonalizado());
        entity.setCurrencyCode(contexto.getCodigoMoneda());
    }
}

