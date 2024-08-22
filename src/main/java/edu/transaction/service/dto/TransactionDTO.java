package edu.transaction.service.dto;

import edu.transaction.service.model.enums.Currency;
import edu.transaction.service.model.enums.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record TransactionDTO(

        @Schema(description = "Sender", example = "1234567891")
        @NotNull(message = "The account from cannot be null")
        Long accountFrom,

        @NotNull(message = "The account to cannot be null")
        @Schema(description = "Recipient", example = "9876543211")
        Long accountTo,

        @NotNull(message = "The sum cannot be null")
        @DecimalMin(value = "0.01", message = "The sum must be greater than 0")
        @Schema(description = "Transaction amount", example = "500")
        BigDecimal sum,

        @NotNull(message = "The currency cannot be null")
        @Schema(description = "Currency", example = "USD")
        Currency currency,

        @NotNull(message = "The expense category cannot be null")
        @Schema(description = "Expense category", example = "service")
        ExpenseCategory expenseCategory
) {}
