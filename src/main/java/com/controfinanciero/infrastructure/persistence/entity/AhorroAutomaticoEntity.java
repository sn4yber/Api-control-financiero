package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Configuración de Ahorro Automático
 */
@Entity
@Table(name = "ahorro_automatico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AhorroAutomaticoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long usuarioId;

    @Column(name = "activo", nullable = false)
    private Boolean activo = false;

    @Column(name = "meta_destino_id")
    private Long metaDestinoId;

    @Column(name = "tipo_redondeo")
    private String tipoRedondeo = "PESO"; // PESO, CINCO, DIEZ

    @Column(name = "total_ahorrado", precision = 15, scale = 2)
    private BigDecimal totalAhorrado = BigDecimal.ZERO;

    @Column(name = "movimientos_procesados")
    private Integer movimientosProcesados = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activo == null) activo = false;
        if (totalAhorrado == null) totalAhorrado = BigDecimal.ZERO;
        if (movimientosProcesados == null) movimientosProcesados = 0;
        if (tipoRedondeo == null) tipoRedondeo = "PESO";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public BigDecimal calcularRedondeo(BigDecimal monto) {
        BigDecimal redondeo;
        switch (tipoRedondeo) {
            case "CINCO":
                redondeo = BigDecimal.valueOf(5);
                break;
            case "DIEZ":
                redondeo = BigDecimal.valueOf(10);
                break;
            default:
                redondeo = BigDecimal.ONE;
        }

        BigDecimal resto = monto.remainder(redondeo);
        if (resto.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return redondeo.subtract(resto);
    }
}

