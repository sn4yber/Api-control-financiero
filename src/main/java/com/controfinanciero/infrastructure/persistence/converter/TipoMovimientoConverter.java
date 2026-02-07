package com.controfinanciero.infrastructure.persistence.converter;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter para mapear TipoMovimiento a PostgreSQL ENUM.
 */
@Converter(autoApply = true)
public class TipoMovimientoConverter implements AttributeConverter<TipoMovimiento, String> {

    @Override
    public String convertToDatabaseColumn(TipoMovimiento attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoMovimiento convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoMovimiento.valueOf(dbData);
    }
}

