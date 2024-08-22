package edu.transaction.service.repository;

import edu.transaction.service.model.ExchangeRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface ExchangeRateRepo extends MongoRepository<ExchangeRate, String> {

    Optional<ExchangeRate> findTopByCurrencyPairOrderByDateDesc(String currencyPair);

    Optional<ExchangeRate> findByCurrencyPairAndDate(String currencyPair, LocalDate date);
}
