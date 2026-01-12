package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.Prioridad;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear Prioridad a PostgreSQL ENUM.
 */
@Converter(autoApply = true)
public class PrioridadConverter implements AttributeConverter<Prioridad, String> {

    @Override
    public String convertToDatabaseColumn(Prioridad attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Prioridad convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Prioridad.valueOf(dbData);
    }
}
