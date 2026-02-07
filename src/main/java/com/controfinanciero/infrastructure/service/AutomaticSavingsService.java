package com.controfinanciero.infrastructure.service;

import com.controfinanciero.infrastructure.persistence.entity.AhorroAutomaticoEntity;
import com.controfinanciero.infrastructure.persistence.repository.AhorroAutomaticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 💡 Servicio de Ahorro Automático
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutomaticSavingsService {

    private final AhorroAutomaticoRepository ahorroRepository;

    @Transactional
    public AhorroAutomaticoEntity configurar(Long usuarioId, Long metaDestinoId, String tipoRedondeo) {
        AhorroAutomaticoEntity config = ahorroRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    AhorroAutomaticoEntity nueva = new AhorroAutomaticoEntity();
                    nueva.setUsuarioId(usuarioId);
                    return nueva;
                });

        config.setActivo(true);
        config.setMetaDestinoId(metaDestinoId);
        config.setTipoRedondeo(tipoRedondeo != null ? tipoRedondeo : "PESO");

        return ahorroRepository.save(config);
    }

    @Transactional
    public BigDecimal procesarRedondeo(Long usuarioId, BigDecimal montoGasto) {
        return ahorroRepository.findByUsuarioId(usuarioId)
                .filter(AhorroAutomaticoEntity::getActivo)
                .map(config -> {
                    BigDecimal redondeo = config.calcularRedondeo(montoGasto);

                    if (redondeo.compareTo(BigDecimal.ZERO) > 0) {
                        config.setTotalAhorrado(config.getTotalAhorrado().add(redondeo));
                        config.setMovimientosProcesados(config.getMovimientosProcesados() + 1);
                        ahorroRepository.save(config);

                        log.info("Ahorro automático: ${} redondeado para usuario {}", redondeo, usuarioId);
                    }

                    return redondeo;
                })
                .orElse(BigDecimal.ZERO);
    }

    public AhorroAutomaticoEntity obtenerEstadisticas(Long usuarioId) {
        return ahorroRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    AhorroAutomaticoEntity nueva = new AhorroAutomaticoEntity();
                    nueva.setUsuarioId(usuarioId);
                    nueva.setActivo(false);
                    return nueva;
                });
    }

    @Transactional
    public void pausar(Long usuarioId) {
        ahorroRepository.findByUsuarioId(usuarioId)
                .ifPresent(config -> {
                    config.setActivo(false);
                    ahorroRepository.save(config);
                });
    }
}

