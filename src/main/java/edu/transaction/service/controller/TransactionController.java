package edu.transaction.service.controller;

import edu.transaction.service.dto.TransactionDTO;
import edu.transaction.service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Transaction Controller",
        description = "This controller is responsible for saving and viewing transactions."
)
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Save a new transaction",
            description = "This method allows saving a new transaction. " +
                    "The request body should include two users: " +
                    "the sender and the recipient of the funds, the transaction amount, desired currency, date and time.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class)
                    )
            )
    )
    @PostMapping("transactions/save")
    public ResponseEntity<Object> saveTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        transactionService.saveTransaction(transactionDTO);
        return ResponseEntity.ok().body("Transaction from %s to %s, with amount = %s%s has been successfully saved."
                .formatted(transactionDTO.accountFrom(), transactionDTO.accountTo(), transactionDTO.sum(), transactionDTO.currency()));
    }
}
