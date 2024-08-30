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
        name = "Limit Controller",
        description = "This controller is responsible for viewing and setting spending limits."
)
public class LimitController {

    private final LimitService limitService;

    @Operation(
            summary = "Set a new spending limit",
            description = "This method allows setting a spending limit. " +
                    "The request body should include the total limit amount, spending category, date, and time.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
        return ResponseEntity.ok("Limit successfully set.");
    }

    @Operation(
            summary = "View the list of all limits",
            description = "This method returns the list of all limits."
    )
    @GetMapping("limits")
    public ResponseEntity<List<LimitDTO>> getAllLimits() {
        List<LimitDTO> limits = limitService.getAllLimits();
        return ResponseEntity.ok(limits);
    }
}
