package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;
import com.controfinanciero.infrastructure.persistence.entity.MetaColaboradorEntity;
import com.controfinanciero.infrastructure.persistence.repository.MetaColaboradorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🤝 Servicio de Metas Compartidas
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SharedGoalsService {

    private final MetaColaboradorRepository colaboradorRepository;
    private final NotificationService notificationService;
    private final UsuarioRepository usuarioRepository;
    private final MetaFinancieraRepository metaRepository;

    /**
     * Compartir meta usando username (más amigable para el usuario)
     */
    @Transactional
    public MetaColaboradorEntity compartirMetaPorUsername(
            Long metaId,
            Long usuarioCreadorId,
            String usernameCreador,
            String usernameInvitado) {

        // Buscar usuario invitado por username
        var usuarioInvitado = usuarioRepository.findByUsername(usernameInvitado)
                .orElseThrow(() -> new RuntimeException("Usuario '" + usernameInvitado + "' no encontrado"));

        // Validar que no se comparta consigo mismo
        if (usuarioCreadorId.equals(usuarioInvitado.getId())) {
            throw new RuntimeException("No puedes compartir una meta contigo mismo");
        }

        // ✅ VALIDAR QUE NO EXISTA YA UN COLABORADOR (evita duplicados)
        var colaboradorExistente = colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioInvitado.getId());
        if (colaboradorExistente.isPresent()) {
            throw new RuntimeException("Este usuario ya es colaborador de esta meta");
        }

        // Obtener nombre de la meta
        var meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

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
        invitado.setUsuarioId(usuarioInvitado.getId());
        invitado.setEsCreador(false);
        invitado.setActivo(true);
        invitado.setInvitadoAt(LocalDateTime.now());

        MetaColaboradorEntity guardado = colaboradorRepository.save(invitado);

        // Crear notificación con datos completos
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("metaId", metaId);
        metadata.put("usuarioInvitador", usernameCreador);
        metadata.put("metaNombre", meta.getNombre());

        notificationService.crearNotificacionConMetadata(
                usuarioInvitado.getId(),
                "META_COMPARTIDA",
                "🤝 Invitación a Meta Compartida",
                String.format("%s te ha invitado a colaborar en '%s'", usernameCreador, meta.getNombre()),
                metadata
        );

        log.info("Meta {} compartida con usuario {} ({})", metaId, usernameInvitado, usuarioInvitado.getId());
        return guardado;
    }

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

                    // Obtener información de la meta para la notificación
                    var meta = metaRepository.findById(metaId)
                            .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

                    // Obtener username del usuario que aportó
                    var usuarioQueAporta = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    // Notificar a otros colaboradores con metadata completa
                    List<MetaColaboradorEntity> otros = colaboradorRepository
                            .findByMetaIdAndUsuarioIdNot(metaId, usuarioId);

                    for (MetaColaboradorEntity otro : otros) {
                        Map<String, Object> metadata = new HashMap<>();
                        metadata.put("metaId", metaId);
                        metadata.put("usuarioInvitador", usuarioQueAporta.getUsername());
                        metadata.put("metaNombre", meta.getNombre());

                        notificationService.crearNotificacionConMetadata(
                                otro.getUsuarioId(),
                                "APORTE_META_COMPARTIDA",
                                "💰 Nuevo Aporte a Meta Compartida",
                                String.format("%s ha realizado un aporte de $%.2f a '%s'",
                                        usuarioQueAporta.getUsername(),
                                        monto.doubleValue(),
                                        meta.getNombre()),
                                metadata
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

    /**
     * Aceptar invitación a meta compartida
     */
    @Transactional
    public void aceptarInvitacion(Long metaId, Long usuarioId) {
        colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioId)
                .ifPresent(colaborador -> {
                    colaborador.setAceptadoAt(LocalDateTime.now());
                    colaboradorRepository.save(colaborador);
                    log.info("Usuario {} aceptó invitación a meta {}", usuarioId, metaId);
                });
    }

    /**
     * Salir de una meta compartida
     */
    @Transactional
    public void salirDeMeta(Long metaId, Long usuarioId) {
        colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioId)
                .ifPresent(colaborador -> {
                    if (colaborador.getEsCreador()) {
                        throw new RuntimeException("El creador no puede salir de la meta");
                    }
                    colaborador.setActivo(false);
                    colaboradorRepository.save(colaborador);
                    log.info("Usuario {} salió de meta {}", usuarioId, metaId);
                });
    }

    /**
     * Eliminar colaborador (solo creador puede hacerlo)
     */
    @Transactional
    public void eliminarColaborador(Long metaId, Long usuarioCreadorId, Long colaboradorId) {
        // Verificar que quien elimina es el creador
        var creador = colaboradorRepository.findByMetaIdAndUsuarioId(metaId, usuarioCreadorId)
                .orElseThrow(() -> new RuntimeException("No eres parte de esta meta"));

        if (!creador.getEsCreador()) {
            throw new RuntimeException("Solo el creador puede eliminar colaboradores");
        }

        // Eliminar colaborador
        colaboradorRepository.findByMetaIdAndUsuarioId(metaId, colaboradorId)
                .ifPresent(colaborador -> {
                    if (colaborador.getEsCreador()) {
                        throw new RuntimeException("No puedes eliminar al creador");
                    }
                    colaborador.setActivo(false);
                    colaboradorRepository.save(colaborador);

                    // Notificar al colaborador eliminado
                    notificationService.crearNotificacion(
                            colaboradorId,
                            "META_COMPARTIDA",
                            "❌ Eliminado de Meta Compartida",
                            "Has sido eliminado de una meta compartida"
                    );

                    log.info("Colaborador {} eliminado de meta {} por {}",
                            colaboradorId, metaId, usuarioCreadorId);
                });
    }
}

