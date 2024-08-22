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
        name = "Контроллер транзакций",
        description = "Данный контроллер отвечает за сохранение и просмотр транзакций.")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Сохранить новую транзакцию",
            description = "Этот метод позволяет сохранить новую транзакцию. " +
                    "В теле запроса нужно указать двух пользователей: " +
                    "отправителя и получателя средств, сумму транзакции, желаемую валюту, дату и время.",
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
        return ResponseEntity.ok().body("Транзакция от пользовтеля %s, к пользовтелю %s, с суммой = %s%s успешно сохранена"
                .formatted(transactionDTO.accountFrom(), transactionDTO.accountTo(), transactionDTO.sum(), transactionDTO.currency()));
    }
}
