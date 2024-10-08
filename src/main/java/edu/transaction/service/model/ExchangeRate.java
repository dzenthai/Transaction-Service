package edu.transaction.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@Document(collection = "exchange_rates")
public class ExchangeRate {

    @Id
    private String id;

    private String currencyPair;

    private BigDecimal rate;

    private LocalDate date;
}
