package com.controfinanciero.infrastructure.web.controller;

import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.infrastructure.persistence.entity.RecordatorioEntity;
import com.controfinanciero.infrastructure.persistence.repository.RecordatorioRepository;
import com.controfinanciero.infrastructure.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 🔔 Controller de Recordatorios Inteligentes
 */
@RestController
@RequestMapping("/api/recordatorios")
@RequiredArgsConstructor
public class RecordatoriosController {

    private final RecordatorioRepository recordatorioRepository;
    private final AuthenticationService authService;

    @GetMapping
    public ResponseEntity<?> obtenerRecordatorios() {
        Usuario usuario = authService.getCurrentUser();
        List<RecordatorioEntity> recordatorios = recordatorioRepository
                .findByUsuarioIdAndActivoTrue(usuario.getId());
        return ResponseEntity.ok(recordatorios);
    }

    @GetMapping("/proximos")
    public ResponseEntity<?> obtenerProximos(@RequestParam(defaultValue = "7") int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate futuro = hoy.plusDays(dias);
        List<RecordatorioEntity> proximos = recordatorioRepository
                .findProximosPagos(hoy, futuro);
        return ResponseEntity.ok(proximos);
    }

    @PostMapping
    public ResponseEntity<?> crearRecordatorio(@Valid @RequestBody CrearRecordatorioRequest request) {
        Usuario usuario = authService.getCurrentUser();

        RecordatorioEntity recordatorio = new RecordatorioEntity();
        recordatorio.setUsuarioId(usuario.getId());
        recordatorio.setTitulo(request.titulo);
        recordatorio.setDescripcion(request.descripcion);
        recordatorio.setMonto(request.monto);
        recordatorio.setFechaPago(request.fechaPago);
        recordatorio.setRecurrente(request.recurrente != null ? request.recurrente : false);
        recordatorio.setFrecuencia(request.frecuencia);
        recordatorio.setDiasAnticipacion(request.diasAnticipacion != null ? request.diasAnticipacion : 3);

        RecordatorioEntity guardado = recordatorioRepository.save(recordatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}/marcar-pagado")
    public ResponseEntity<?> marcarPagado(@PathVariable Long id) {
        return recordatorioRepository.findById(id)
                .map(recordatorio -> {
                    recordatorio.setPagado(true);
                    recordatorioRepository.save(recordatorio);
                    return ResponseEntity.ok(recordatorio);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRecordatorio(@PathVariable Long id) {
        recordatorioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    record CrearRecordatorioRequest(
            String titulo,
            String descripcion,
            BigDecimal monto,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPago,
            Boolean recurrente,
            String frecuencia,
            Integer diasAnticipacion
    ) {}
}

