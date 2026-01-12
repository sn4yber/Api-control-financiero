package com.controfinanciero.application.usecase.Meta;

import com.controfinanciero.application.dto.MetaFinancieraDTO;
import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener todas las metas financieras de un usuario
 */
public class ObtenerMetasFinancierasUseCase {
    private final MetaFinancieraRepository metaRepository;

    public ObtenerMetasFinancierasUseCase(MetaFinancieraRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    public List<MetaFinancieraDTO> ejecutar(Long usuarioId) {
        List<MetaFinanciera> metas = metaRepository.findByUsuarioId(usuarioId);
        return metas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MetaFinancieraDTO> ejecutarPorEstado(Long usuarioId, EstadoMeta estado) {
        List<MetaFinanciera> metas = metaRepository.findByUsuarioIdAndEstado(usuarioId, estado);
        return metas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MetaFinancieraDTO toDTO(MetaFinanciera meta) {
        return new MetaFinancieraDTO(
                meta.getId(),
                meta.getUsuarioId(),
                meta.getNombre(),
                meta.getDescripcion(),
                meta.getMontoObjetivo(),
                meta.getMontoActual(),
                meta.getFechaObjetivo(),
                meta.getPrioridad(),
                meta.getEstado(),
                meta.getCreatedAt(),
                meta.getUpdatedAt(),
                meta.getCompletedAt()
        );
    }
}
