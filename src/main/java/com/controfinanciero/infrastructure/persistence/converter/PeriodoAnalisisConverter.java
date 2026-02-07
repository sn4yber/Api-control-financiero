package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear PeriodoAnalisis a ENUM de PostgreSQL.
 */
@Converter(autoApply = true)
public class PeriodoAnalisisConverter implements AttributeConverter<PeriodoAnalisis, String> {

    @Override
    public String convertToDatabaseColumn(PeriodoAnalisis attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public PeriodoAnalisis convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return PeriodoAnalisis.valueOf(dbData);
    }
}

