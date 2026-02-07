package com.controfinanciero.infrastructure.service;

import com.controfinanciero.infrastructure.persistence.entity.MetaColaboradorEntity;
import com.controfinanciero.infrastructure.persistence.repository.MetaColaboradorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 🤝 Servicio de Metas Compartidas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SharedGoalsService {

    private final MetaColaboradorRepository colaboradorRepository;
    private final NotificationService notificationService;

    @Transactional
    public MetaColaboradorEntity compartirMeta(Long metaId, Long usuarioCreadorId, Long usuarioInvitadoId) {
        // Crear colaborador creador si no existe
        var creador = colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioCreadorId)
                .orElseGet(() -> {
                    MetaColaboradorEntity nuevo = new MetaColaboradorEntity();
                    nuevo.setMetaId(metaId);
                    nuevo.setUsuarioId(usuarioCreadorId);
                    nuevo.setEsCreador(true);
                    nuevo.setActivo(true);
                    return colaboradorRepository.save(nuevo);
                });

        // Crear invitación
        MetaColaboradorEntity invitado = new MetaColaboradorEntity();
        invitado.setMetaId(metaId);
        invitado.setUsuarioId(usuarioInvitadoId);
        invitado.setEsCreador(false);
        invitado.setActivo(true);
        invitado.setInvitadoAt(LocalDateTime.now());

        MetaColaboradorEntity guardado = colaboradorRepository.save(invitado);

        // Notificar al invitado
        notificationService.crearNotificacion(
                usuarioInvitadoId,
                "META_COMPARTIDA",
                "🤝 Invitación a Meta Compartida",
                "Te han invitado a colaborar en una meta financiera"
        );

        log.info("Meta {} compartida con usuario {}", metaId, usuarioInvitadoId);
        return guardado;
    }

    @Transactional
    public void registrarAporte(Long metaId, Long usuarioId, BigDecimal monto) {
        colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioId)
                .ifPresent(colaborador -> {
                    BigDecimal nuevoAporte = colaborador.getAporteTotal().add(monto);
                    colaborador.setAporteTotal(nuevoAporte);
                    colaboradorRepository.save(colaborador);

                    // Notificar a otros colaboradores
                    List<MetaColaboradorEntity> otros = colaboradorRepository
                            .findByMetaIdAndUsuarioIdNot(metaId, usuarioId);

                    for (MetaColaboradorEntity otro : otros) {
                        notificationService.crearNotificacion(
                                otro.getUsuarioId(),
                                "APORTE_META_COMPARTIDA",
                                "💰 Nuevo Aporte a Meta Compartida",
                                String.format("Se ha realizado un aporte de $%.2f a la meta compartida", monto.doubleValue())
                        );
                    }
                });
    }

    public List<MetaColaboradorEntity> obtenerColaboradores(Long metaId) {
        return colaboradorRepository.findByMetaId(metaId);
    }

    public List<MetaColaboradorEntity> obtenerMisColaboraciones(Long usuarioId) {
        return colaboradorRepository.findByUsuarioId(usuarioId);
    }
}

