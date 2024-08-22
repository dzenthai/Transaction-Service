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
        name = "Контроллер обменного курса",
        description = "Данный контроллер отвечает за получение актуальных курсов валют.")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Operation(
            summary = "Получить актуальный курс валюты",
            description = "Данный метод возвращает актуальный курс валют. В параметрах запроса нужно указать валютную пару.",
            parameters = {
                    @Parameter(name = "currencyPair", description = "Пара валют, для которой нужно получить курс. Например USD/KZT"),
            }
    )
    @GetMapping("exchange-rate/get")
    public ResponseEntity<BigDecimal> getExchangeRate(@RequestParam String currencyPair) {
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(currencyPair, LocalDate.now());
        return ResponseEntity.ok(exchangeRate);
    }
}
