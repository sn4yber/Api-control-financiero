package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.TipoIngreso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear TipoIngreso a ENUM de PostgreSQL.
 */
@Converter(autoApply = true)
public class TipoIngresoConverter implements AttributeConverter<TipoIngreso, String> {

    @Override
    public String convertToDatabaseColumn(TipoIngreso attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoIngreso convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoIngreso.valueOf(dbData);
    }
}
