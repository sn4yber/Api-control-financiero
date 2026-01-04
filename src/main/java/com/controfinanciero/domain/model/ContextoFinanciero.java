package com.controfinanciero.domain.model;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio: Contexto Financiero.
 * Define cómo funciona el dinero del usuario (frecuencia, ahorro deseado, etc.).
 * Permite que la API se adapte a la realidad financiera de cada persona.
 */
public class ContextoFinanciero {

    private Long id;
    private Long usuarioId;
    private TipoIngreso tipoIngreso;
    private boolean tieneIngresoVariable;
    private BigDecimal porcentajeAhorroDeseado;
    private PeriodoAnalisis periodoAnalisis;
    private Integer diasPeriodoPersonalizado;
    private String codigoMoneda;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío requerido
    protected ContextoFinanciero() {
    }

    // Constructor para creación
    public ContextoFinanciero(Long usuarioId, TipoIngreso tipoIngreso) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "UsuarioId no puede ser null");
        this.tipoIngreso = Objects.requireNonNull(tipoIngreso, "TipoIngreso no puede ser null");
        this.tieneIngresoVariable = false;
        this.porcentajeAhorroDeseado = BigDecimal.ZERO;
        this.periodoAnalisis = PeriodoAnalisis.MONTHLY;
        this.codigoMoneda = "COP";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Métodos de negocio
    public void configurarAhorro(BigDecimal porcentaje) {
        validarPorcentaje(porcentaje);
        this.porcentajeAhorroDeseado = porcentaje;
        this.updatedAt = LocalDateTime.now();
    }

    public void cambiarTipoIngreso(TipoIngreso nuevoTipo, boolean tieneVariable) {
        this.tipoIngreso = Objects.requireNonNull(nuevoTipo, "TipoIngreso no puede ser null");
        this.tieneIngresoVariable = tieneVariable;
        this.updatedAt = LocalDateTime.now();
    }

    public void configurarPeriodoAnalisis(PeriodoAnalisis periodo, Integer diasPersonalizados) {
        this.periodoAnalisis = Objects.requireNonNull(periodo, "PeriodoAnalisis no puede ser null");

        if (periodo.requiereDiasPersonalizados()) {
            if (diasPersonalizados == null || diasPersonalizados <= 0) {
                throw new IllegalArgumentException("Periodo personalizado requiere días válidos");
            }
            this.diasPeriodoPersonalizado = diasPersonalizados;
        } else {
            this.diasPeriodoPersonalizado = null;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void cambiarMoneda(String codigoMoneda) {
        this.codigoMoneda = Objects.requireNonNull(codigoMoneda, "Código de moneda no puede ser null");
        this.updatedAt = LocalDateTime.now();
    }

    public boolean tieneIngresosPredecibles() {
        return tipoIngreso.esIngresoPredecible() && !tieneIngresoVariable;
    }

    public boolean debeAhorrar() {
        return porcentajeAhorroDeseado.compareTo(BigDecimal.ZERO) > 0;
    }

    // Validaciones de dominio
    private void validarPorcentaje(BigDecimal porcentaje) {
        if (porcentaje == null) {
            throw new IllegalArgumentException("Porcentaje no puede ser null");
        }
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Porcentaje debe estar entre 0 y 100");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public boolean isTieneIngresoVariable() {
        return tieneIngresoVariable;
    }

    public BigDecimal getPorcentajeAhorroDeseado() {
        return porcentajeAhorroDeseado;
    }

    public PeriodoAnalisis getPeriodoAnalisis() {
        return periodoAnalisis;
    }

    public Integer getDiasPeriodoPersonalizado() {
        return diasPeriodoPersonalizado;
    }

    public String getCodigoMoneda() {
        return codigoMoneda;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters (solo para infraestructura)
    protected void setId(Long id) {
        this.id = id;
    }

    protected void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    protected void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    protected void setTieneIngresoVariable(boolean tieneIngresoVariable) {
        this.tieneIngresoVariable = tieneIngresoVariable;
    }

    protected void setPorcentajeAhorroDeseado(BigDecimal porcentajeAhorroDeseado) {
        this.porcentajeAhorroDeseado = porcentajeAhorroDeseado;
    }

    protected void setPeriodoAnalisis(PeriodoAnalisis periodoAnalisis) {
        this.periodoAnalisis = periodoAnalisis;
    }

    protected void setDiasPeriodoPersonalizado(Integer diasPeriodoPersonalizado) {
        this.diasPeriodoPersonalizado = diasPeriodoPersonalizado;
    }

    protected void setCodigoMoneda(String codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextoFinanciero that = (ContextoFinanciero) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ContextoFinanciero{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", tipoIngreso=" + tipoIngreso +
                ", porcentajeAhorroDeseado=" + porcentajeAhorroDeseado +
                '}';
    }
}

