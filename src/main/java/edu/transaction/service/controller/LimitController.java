package edu.transaction.service.controller;

import edu.transaction.service.dto.LimitDTO;
import edu.transaction.service.service.LimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер лимитов",
        description = "Данный контроллер отвечает просмотр и установку лимитов.")
public class LimitController {

    private final LimitService limitService;

    @Operation(
            summary = "Установить новый лимит расходов",
            description = "Этот метод позволяет установить лимит расходов. " +
                    "В теле запроса нужно указать общую сумму лимита, категорию расходов, дату и время.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LimitDTO.class)
                    )
            )
    )
    @PostMapping("limits/set")
    public ResponseEntity<String> setLimit(@Valid @RequestBody LimitDTO limitDTO) {
        limitService.setLimit(limitDTO);
        return ResponseEntity.ok("Лимит успешно установлен.");
    }

    @Operation(
            summary = "Просмотреть список всех лимитов",
            description = "Данный метод возвращает список всех лимитов."
    )
    @GetMapping("limits")
    public ResponseEntity<List<LimitDTO>> getLimits() {
        List<LimitDTO> limits = limitService.getAllLimits();
        return ResponseEntity.ok(limits);
    }
}
