package edu.transaction.service.controller;

import edu.transaction.service.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Exchange Rate Controller",
        description = "This controller is responsible for retrieving current exchange rates."
)
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Operation(
            summary = "Get current exchange rate",
            description = "This method returns the current exchange rate. " +
                    "The currency pair should be specified in the request parameters.",
            parameters = {
                    @Parameter(name = "currencyPair", description = "The currency pair for which you need the rate. " +
                            "For example, USD/KZT"),
            }
    )
    @GetMapping("exchange-rate/get")
    public ResponseEntity<BigDecimal> getExchangeRate(@RequestParam String currencyPair) {
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(currencyPair, LocalDate.now());
        return ResponseEntity.ok(exchangeRate);
    }
}
