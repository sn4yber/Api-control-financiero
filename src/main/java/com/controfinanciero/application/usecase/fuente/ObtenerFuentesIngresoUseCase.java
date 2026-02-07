package com.controfinanciero.application.usecase.fuente;

import com.controfinanciero.application.dto.FuenteIngresoDTO;
import com.controfinanciero.domain.model.FuenteIngreso;
import com.controfinanciero.domain.repository.FuenteIngresoRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener todas las fuentes de ingreso de un usuario
 */
public class ObtenerFuentesIngresoUseCase {

    private final FuenteIngresoRepository fuenteIngresoRepository;

    public ObtenerFuentesIngresoUseCase(FuenteIngresoRepository fuenteIngresoRepository) {
        this.fuenteIngresoRepository = fuenteIngresoRepository;
    }

    public List<FuenteIngresoDTO> ejecutar(Long usuarioId) {
        List<FuenteIngreso> fuentes = fuenteIngresoRepository.findByUsuarioId(usuarioId);

        return fuentes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FuenteIngresoDTO> ejecutarActivas(Long usuarioId) {
        List<FuenteIngreso> fuentes = fuenteIngresoRepository.findByUsuarioIdAndActiva(usuarioId, true);

        return fuentes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private FuenteIngresoDTO toDTO(FuenteIngreso fuente) {
        return new FuenteIngresoDTO(
                fuente.getId(),
                fuente.getUsuarioId(),
                fuente.getNombre(),
                fuente.getDescripcion(),
                fuente.getTipoFuente(),
                fuente.isEsIngresoReal(),
                fuente.isActiva(),
                fuente.getCreatedAt(),
                fuente.getUpdatedAt()
        );
    }
}

