package edu.transaction.service.dto;

import edu.transaction.service.model.enums.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
public record LimitDTO(

        @NotNull(message = "The sum cannot be null")
        @DecimalMin(value = "0.01", message = "The sum must be greater than 0")
        @Schema(description = "Limit", example = "300")
        BigDecimal limit,

        @NotNull(message = "The sum cannot be null")
        @Schema(description = "Expense category", example = "product")
        ExpenseCategory expenseCategory,

        @NotNull(message = "The sum cannot be null")
        @Schema(description = "Date and time", example = "2024-08-13T19:35:18")
        LocalDateTime datetime) {
}


