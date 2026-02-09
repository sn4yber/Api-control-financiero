package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.repository.NotificacionRepository;
import com.controfinanciero.infrastructure.persistence.entity.MetaColaboradorEntity;
import com.controfinanciero.infrastructure.persistence.repository.MetaColaboradorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 🔧 Controller de Mantenimiento
 * Endpoints para limpiar datos corruptos y duplicados
 */
@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceController {

    private final MetaColaboradorRepository colaboradorRepository;
    private final NotificacionRepository notificacionRepository;

    /**
     * POST /api/maintenance/limpiar-duplicados-colaboradores
     * Elimina duplicados en meta_colaboradores, mantiene el más reciente
     */
    @PostMapping("/limpiar-duplicados-colaboradores")
    @Transactional
    public ResponseEntity<?> limpiarDuplicadosColaboradores() {
        log.info("🔧 Iniciando limpieza de duplicados en meta_colaboradores...");

        List<MetaColaboradorEntity> todos = colaboradorRepository.findAll();

        // Agrupar por (metaId, usuarioId)
        Map<String, List<MetaColaboradorEntity>> agrupados = todos.stream()
                .collect(Collectors.groupingBy(c -> c.getMetaId() + "-" + c.getUsuarioId()));

        int eliminados = 0;
        List<String> detalles = new ArrayList<>();

        for (Map.Entry<String, List<MetaColaboradorEntity>> entry : agrupados.entrySet()) {
            List<MetaColaboradorEntity> grupo = entry.getValue();

            if (grupo.size() > 1) {
                // Mantener el más reciente (por invitado_at o id)
                grupo.sort((a, b) -> {
                    if (a.getInvitadoAt() != null && b.getInvitadoAt() != null) {
                        return b.getInvitadoAt().compareTo(a.getInvitadoAt());
                    }
                    return b.getId().compareTo(a.getId());
                });

                MetaColaboradorEntity mantener = grupo.get(0);

                // Eliminar los demás
                for (int i = 1; i < grupo.size(); i++) {
                    MetaColaboradorEntity eliminar = grupo.get(i);
                    colaboradorRepository.delete(eliminar);
                    eliminados++;

                    String detalle = String.format("Meta %d + Usuario %d: Eliminado duplicado ID %d (mantenido ID %d)",
                            eliminar.getMetaId(), eliminar.getUsuarioId(), eliminar.getId(), mantener.getId());
                    detalles.add(detalle);
                    log.info("✅ " + detalle);
                }
            }
        }

        log.info("🎯 Limpieza completada: {} duplicados eliminados", eliminados);

        return ResponseEntity.ok(new LimpiezaResponse(
                "Limpieza de duplicados completada",
                eliminados,
                detalles
        ));
    }

    /**
     * POST /api/maintenance/limpiar-notificaciones-corruptas
     * Elimina notificaciones de metas compartidas sin metaId válido
     */
    @PostMapping("/limpiar-notificaciones-corruptas")
    @Transactional
    public ResponseEntity<?> limpiarNotificacionesCorruptas() {
        log.info("🔧 Iniciando limpieza de notificaciones corruptas...");

        List<String> tiposMetasCompartidas = Arrays.asList(
                "META_COMPARTIDA",
                "APORTE_META_COMPARTIDA",
                "META_COMPLETADA",
                "RECORDATORIO_META"
        );

        var todasNotificaciones = notificacionRepository.findAll();
        int eliminadas = 0;
        List<String> detalles = new ArrayList<>();

        for (var notif : todasNotificaciones) {
            if (tiposMetasCompartidas.contains(notif.getTipo())) {
                // Verificar si metaId es null (campo directo en la entidad)
                boolean esCorrupta = notif.getMetaId() == null;

                if (esCorrupta) {
                    String detalle = String.format("Notificación ID %d (%s): metaId null - Eliminada",
                            notif.getId(), notif.getTipo());
                    detalles.add(detalle);
                    log.info("✅ " + detalle);

                    notificacionRepository.delete(notif);
                    eliminadas++;
                }
            }
        }

        log.info("🎯 Limpieza completada: {} notificaciones corruptas eliminadas", eliminadas);

        return ResponseEntity.ok(new LimpiezaResponse(
                "Limpieza de notificaciones corruptas completada",
                eliminadas,
                detalles
        ));
    }

    /**
     * GET /api/maintenance/verificar-duplicados
     * Verifica si existen duplicados sin eliminarlos
     */
    @GetMapping("/verificar-duplicados")
    public ResponseEntity<?> verificarDuplicados() {
        List<MetaColaboradorEntity> todos = colaboradorRepository.findAll();

        Map<String, List<MetaColaboradorEntity>> agrupados = todos.stream()
                .collect(Collectors.groupingBy(c -> c.getMetaId() + "-" + c.getUsuarioId()));

        List<DuplicadoInfo> duplicados = new ArrayList<>();
        int totalDuplicados = 0;

        for (Map.Entry<String, List<MetaColaboradorEntity>> entry : agrupados.entrySet()) {
            if (entry.getValue().size() > 1) {
                MetaColaboradorEntity primero = entry.getValue().get(0);
                duplicados.add(new DuplicadoInfo(
                        primero.getMetaId(),
                        primero.getUsuarioId(),
                        entry.getValue().size(),
                        entry.getValue().stream().map(MetaColaboradorEntity::getId).collect(Collectors.toList())
                ));
                totalDuplicados += entry.getValue().size() - 1;
            }
        }

        return ResponseEntity.ok(new VerificacionResponse(
                totalDuplicados > 0 ? "Se encontraron duplicados" : "No hay duplicados",
                totalDuplicados,
                duplicados
        ));
    }

    /**
     * GET /api/maintenance/verificar-notificaciones-corruptas
     * Verifica notificaciones corruptas sin eliminarlas
     */
    @GetMapping("/verificar-notificaciones-corruptas")
    public ResponseEntity<?> verificarNotificacionesCorruptas() {
        List<String> tiposMetasCompartidas = Arrays.asList(
                "META_COMPARTIDA",
                "APORTE_META_COMPARTIDA",
                "META_COMPLETADA",
                "RECORDATORIO_META"
        );

        var todasNotificaciones = notificacionRepository.findAll();
        List<NotificacionCorruptaInfo> corruptas = new ArrayList<>();

        for (var notif : todasNotificaciones) {
            if (tiposMetasCompartidas.contains(notif.getTipo())) {
                // Verificar si metaId es null
                if (notif.getMetaId() == null) {
                    corruptas.add(new NotificacionCorruptaInfo(
                            notif.getId(),
                            notif.getTipo(),
                            notif.getTitulo(),
                            "metaId = null",
                            notif.getVersion()
                    ));
                }
            }
        }

        return ResponseEntity.ok(new VerificacionNotificacionesResponse(
                corruptas.size() > 0 ? "Se encontraron notificaciones corruptas" : "No hay notificaciones corruptas",
                corruptas.size(),
                corruptas
        ));
    }

    // DTOs
    record LimpiezaResponse(String mensaje, int cantidad, List<String> detalles) {}

    record DuplicadoInfo(Long metaId, Long usuarioId, int cantidad, List<Long> ids) {}

    record VerificacionResponse(String mensaje, int totalDuplicados, List<DuplicadoInfo> duplicados) {}

    record NotificacionCorruptaInfo(Long id, String tipo, String titulo, String problema, String version) {}

    record VerificacionNotificacionesResponse(String mensaje, int cantidad, List<NotificacionCorruptaInfo> notificaciones) {}
}





