package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.persistence.entity.RegistroAuditoriaEntity;
import com.controfinanciero.infrastructure.persistence.repository.RegistroAuditoriaJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 📜 Servicio de Auditoría
 * Registra QUIÉN hizo QUÉ y CUÁNDO
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final RegistroAuditoriaJpaRepository auditoriaRepo;
    private final ObjectMapper objectMapper;

    /**
     * Registra una acción de auditoría
     * Se ejecuta de forma asíncrona para no impactar performance
     */
    @Async
    public void registrarAccion(Usuario usuario, String tipoEntidad, Long entidadId,
                               String accion, Object valorAnterior, Object valorNuevo,
                               HttpServletRequest request) {
        try {
            String valorAnteriorJson = valorAnterior != null ? objectMapper.writeValueAsString(valorAnterior) : null;
            String valorNuevoJson = valorNuevo != null ? objectMapper.writeValueAsString(valorNuevo) : null;

            RegistroAuditoriaEntity registro = RegistroAuditoriaEntity.builder()
                    .usuarioId(usuario.getId())
                    .tipoEntidad(tipoEntidad)
                    .entidadId(entidadId)
                    .accion(accion)
                    .valorAnterior(valorAnteriorJson)
                    .valorNuevo(valorNuevoJson)
                    .ipAddress(obtenerIpCliente(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .build();

            auditoriaRepo.save(registro);

            log.info("📜 Auditoría registrada: Usuario #{} - {} {} en {} #{}",
                    usuario.getId(), accion, tipoEntidad, tipoEntidad, entidadId);

        } catch (Exception e) {
            log.error("❌ Error al registrar auditoría: {}", e.getMessage(), e);
        }
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

