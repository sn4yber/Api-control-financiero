package com.controfinanciero.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response con el resumen financiero del usuario
 */
public record DashboardSummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal totalSavings,
        BigDecimal currentBalance,
        BigDecimal availableBalance,
        LocalDate periodStart,
        LocalDate periodEnd,
        int totalTransactions,
        GoalsOverview goalsOverview
) {
    public record GoalsOverview(
            int activeGoals,
            int completedGoals,
            BigDecimal totalTargetAmount,
            BigDecimal totalSavedAmount,
            Double averageProgress
    ) {}
}

