package com.controfinanciero.infrastructure.persistence.entity;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Contexto Financiero.
 * Mapea la tabla 'financial_contexts' en PostgreSQL.
 */
@Entity
@Table(name = "financial_contexts", indexes = {
    @Index(name = "idx_financial_contexts_user", columnList = "user_id")
})
public class ContextoFinancieroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "income_type", nullable = false)
    private TipoIngreso incomeType;

    @Column(name = "has_variable_income", nullable = false)
    private Boolean hasVariableIncome;

    @Column(name = "desired_savings_percentage", precision = 5, scale = 2)
    private BigDecimal desiredSavingsPercentage;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "analysis_period")
    private PeriodoAnalisis analysisPeriod;

    @Column(name = "custom_period_days")
    private Integer customPeriodDays;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructor vacío requerido por JPA
    protected ContextoFinancieroEntity() {
    }

    // Constructor para creación
    public ContextoFinancieroEntity(Long userId, TipoIngreso incomeType) {
        this.userId = userId;
        this.incomeType = incomeType;
        this.hasVariableIncome = false;
        this.desiredSavingsPercentage = BigDecimal.ZERO;
        this.analysisPeriod = PeriodoAnalisis.MONTHLY;
        this.currencyCode = "COP";
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
        if (hasVariableIncome == null) {
            hasVariableIncome = false;
        }
        if (desiredSavingsPercentage == null) {
            desiredSavingsPercentage = BigDecimal.ZERO;
        }
        if (analysisPeriod == null) {
            analysisPeriod = PeriodoAnalisis.MONTHLY;
        }
        if (currencyCode == null) {
            currencyCode = "COP";
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

    public TipoIngreso getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(TipoIngreso incomeType) {
        this.incomeType = incomeType;
    }

    public Boolean getHasVariableIncome() {
        return hasVariableIncome;
    }

    public void setHasVariableIncome(Boolean hasVariableIncome) {
        this.hasVariableIncome = hasVariableIncome;
    }

    public BigDecimal getDesiredSavingsPercentage() {
        return desiredSavingsPercentage;
    }

    public void setDesiredSavingsPercentage(BigDecimal desiredSavingsPercentage) {
        this.desiredSavingsPercentage = desiredSavingsPercentage;
    }

    public PeriodoAnalisis getAnalysisPeriod() {
        return analysisPeriod;
    }

    public void setAnalysisPeriod(PeriodoAnalisis analysisPeriod) {
        this.analysisPeriod = analysisPeriod;
    }

    public Integer getCustomPeriodDays() {
        return customPeriodDays;
    }

    public void setCustomPeriodDays(Integer customPeriodDays) {
        this.customPeriodDays = customPeriodDays;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

