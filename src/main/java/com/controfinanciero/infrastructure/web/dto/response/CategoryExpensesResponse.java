package com.controfinanciero.infrastructure.web.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response con gastos agrupados por categoría
 */
public record CategoryExpensesResponse(
        List<CategoryExpense> categories,
        BigDecimal totalExpenses
) {
    public record CategoryExpense(
            Long categoryId,
            String categoryName,
            String colorHex,
            BigDecimal amount,
            int transactionCount,
            Double percentage
    ) {}
}

