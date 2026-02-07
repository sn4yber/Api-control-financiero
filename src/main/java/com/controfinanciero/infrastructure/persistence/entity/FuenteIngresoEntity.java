package com.controfinanciero.infrastructure.persistence.entity;

import com.controfinanciero.domain.model.enums.TipoFuente;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Fuente de Ingreso.
 * Mapea la tabla 'income_sources' en PostgreSQL.
 */
@Entity
@Table(name = "income_sources", indexes = {
    @Index(name = "idx_income_sources_user_active", columnList = "user_id,is_active"),
    @Index(name = "idx_income_sources_type", columnList = "source_type")
})
public class FuenteIngresoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "source_type", nullable = false)
    private TipoFuente sourceType;

    @Column(name = "is_real_income", nullable = false)
    private Boolean isRealIncome;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructor vacío requerido por JPA
    protected FuenteIngresoEntity() {
    }

    // Constructor para creación
    public FuenteIngresoEntity(Long userId, String name, TipoFuente sourceType) {
        this.userId = userId;
        this.name = name;
        this.sourceType = sourceType;
        this.isRealIncome = true;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (isRealIncome == null) {
            isRealIncome = true;
        }
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TipoFuente getSourceType() {
        return sourceType;
    }

    public void setSourceType(TipoFuente sourceType) {
        this.sourceType = sourceType;
    }

    public Boolean getIsRealIncome() {
        return isRealIncome;
    }

    public void setIsRealIncome(Boolean isRealIncome) {
        this.isRealIncome = isRealIncome;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}