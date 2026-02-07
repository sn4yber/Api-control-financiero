package com.controfinanciero.domain.model;

import com.controfinanciero.domain.model.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio: Movimiento Financiero.
 * Representa cualquier entrada o salida de dinero del usuario.
 * El saldo no se guarda, se calcula a partir de los movimientos.
 */
public class MovimientoFinanciero {

    private Long id;
    private Long usuarioId;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal monto;
    private String descripcion;
    private LocalDate fechaMovimiento;
    private Long categoriaId;
    private Long fuenteIngresoId;
    private Long metaId;
    private boolean esRecurrente;
    private String patronRecurrencia;
    private String notas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío requerido
    protected MovimientoFinanciero() {
    }

    // Constructor para creación
    public MovimientoFinanciero(
            Long usuarioId,
            TipoMovimiento tipoMovimiento,
            BigDecimal monto,
            String descripcion,
            LocalDate fechaMovimiento
    ) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "UsuarioId no puede ser null");
        this.tipoMovimiento = Objects.requireNonNull(tipoMovimiento, "TipoMovimiento no puede ser null");
        this.monto = Objects.requireNonNull(monto, "Monto no puede ser null");
        this.descripcion = descripcion;
        this.fechaMovimiento = Objects.requireNonNull(fechaMovimiento, "Fecha no puede ser null");
        this.esRecurrente = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validarMonto();
    }

    // Métodos de negocio
    public void asignarCategoria(Long categoriaId) {
        this.categoriaId = categoriaId;
        this.updatedAt = LocalDateTime.now();
    }

    public void asignarFuenteIngreso(Long fuenteIngresoId) {
        if (!tipoMovimiento.esIngreso()) {
            throw new IllegalStateException("Solo los ingresos pueden tener fuente de ingreso");
        }
        this.fuenteIngresoId = fuenteIngresoId;
        this.updatedAt = LocalDateTime.now();
    }

    public void vincularAMeta(Long metaId) {
        if (tipoMovimiento != TipoMovimiento.SAVINGS) {
            throw new IllegalStateException("Solo los ahorros pueden vincularse a metas");
        }
        this.metaId = metaId;
        this.updatedAt = LocalDateTime.now();
    }

    public void configurarRecurrencia(String patronRecurrencia) {
        this.esRecurrente = true;
        this.patronRecurrencia = Objects.requireNonNull(patronRecurrencia, "Patrón de recurrencia no puede ser null");
        this.updatedAt = LocalDateTime.now();
    }

    public void eliminarRecurrencia() {
        this.esRecurrente = false;
        this.patronRecurrencia = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void actualizarDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.updatedAt = LocalDateTime.now();
    }

    public void agregarNotas(String notas) {
        this.notas = notas;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean esIngreso() {
        return tipoMovimiento.esIngreso();
    }

    public boolean esGasto() {
        return tipoMovimiento.esGasto();
    }

    public boolean esAhorro() {
        return tipoMovimiento.esAhorro();
    }

    public boolean incrementaSaldo() {
        return tipoMovimiento.incrementaSaldo();
    }

    public boolean tieneCategoria() {
        return categoriaId != null;
    }

    public boolean estaVinculadoAMeta() {
        return metaId != null;
    }

    // Validaciones de dominio
    private void validarMonto() {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public Long getFuenteIngresoId() {
        return fuenteIngresoId;
    }

    public Long getMetaId() {
        return metaId;
    }

    public boolean isEsRecurrente() {
        return esRecurrente;
    }

    public String getPatronRecurrencia() {
        return patronRecurrencia;
    }

    public String getNotas() {
        return notas;
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

    protected void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    protected void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    protected void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    protected void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    protected void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    protected void setFuenteIngresoId(Long fuenteIngresoId) {
        this.fuenteIngresoId = fuenteIngresoId;
    }

    protected void setMetaId(Long metaId) {
        this.metaId = metaId;
    }

    protected void setEsRecurrente(boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    protected void setPatronRecurrencia(String patronRecurrencia) {
        this.patronRecurrencia = patronRecurrencia;
    }

    protected void setNotas(String notas) {
        this.notas = notas;
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
        MovimientoFinanciero that = (MovimientoFinanciero) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "MovimientoFinanciero{" +
                "id=" + id +
                ", tipo=" + tipoMovimiento +
                ", monto=" + monto +
                ", fecha=" + fechaMovimiento +
                '}';
    }
}

