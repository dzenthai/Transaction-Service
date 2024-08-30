package edu.transaction.service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.transaction.service.dto.TransactionDTO;
import edu.transaction.service.exception.ExchangeException;
import edu.transaction.service.model.ExchangeRate;
import edu.transaction.service.model.enums.Currency;
import edu.transaction.service.repository.ExchangeRateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepo exchangeRateRepo;

    @Value("${twelvedata.apikey}")
    private String apiKey;

    @Transactional
    public BigDecimal getExchangeRate(String currencyPair, LocalDate date) {
        Optional<ExchangeRate> optionalRate = exchangeRateRepo.findByCurrencyPairAndDate(currencyPair, date);

        if (optionalRate.isPresent()) {
            log.info("ExchangeRateService | Searching for the exchange rate in the database.");
            return optionalRate.get().getRate();
        }

        BigDecimal rate = fetchExchangeRateFromAPI(currencyPair, date);

        if (rate != null) {

            log.info("ExchangeRateService | Retrieving the exchange rate through an API request.");

            ExchangeRate exchangeRate = ExchangeRate.builder()
                    .currencyPair(currencyPair)
                    .rate(rate)
                    .date(date)
                    .build();

            exchangeRateRepo.save(exchangeRate);
        }

        Optional<ExchangeRate> lastAvailableRate = exchangeRateRepo.findTopByCurrencyPairOrderByDateDesc(currencyPair);

        if (lastAvailableRate.isPresent()) {
            log.info("ExchangeRateService | Searching for the latest available rate.");
            if (lastAvailableRate.get().getDate().isBefore(date)) {
                return lastAvailableRate.get().getRate();
            }
        }

        return rate != null ? rate : lastAvailableRate.map(ExchangeRate::getRate)
                .orElseThrow(() -> new ExchangeException("ExchangeRateService | No available exchange rate."));
    }

    @Transactional
    public BigDecimal convertToUSD(TransactionDTO transactionDTO) {
        if (transactionDTO.currency() == Currency.USD) {
            return transactionDTO.sum();
        }
        BigDecimal exchangeRate = getExchangeRate(transactionDTO.currency().name() + "/USD", LocalDate.now());
        log.debug("ExchangeRateService | Converting {} to USD", transactionDTO.currency().name());
        return transactionDTO.sum().multiply(exchangeRate);
    }

    private BigDecimal fetchExchangeRateFromAPI(String currencyPair, LocalDate date) {
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            String url = String.format(
                    "https://api.twelvedata.com/time_series?symbol=%s&interval=1day&start_date=%s&apikey=%s&format=JSON",
                    currencyPair, date, apiKey);

            Response response = client.prepareGet(url)
                    .execute()
                    .toCompletableFuture()
                    .join();

            String responseBody = response.getResponseBody();
            log.debug("ExchangeRateService | Full response from Twelvedata API: {}", responseBody);

            BigDecimal closePrice = parseClosePrice(responseBody);

            if (closePrice == null) {
                log.debug("ExchangeRateService | Closing data for {} is unavailable, attempting to retrieve the previous closing rate.", date);
                url = String.format(
                        "https://api.twelvedata.com/time_series?symbol=%s&interval=1day&start_date=%s&apikey=%s&format=JSON&previous_close=true",
                        currencyPair, date, apiKey);

                response = client.prepareGet(url)
                        .execute()
                        .toCompletableFuture()
                        .join();

                responseBody = response.getResponseBody();
                log.debug("ExchangeRateService | Full response from Twelvedata API with previous_close=true: {}", responseBody);

                closePrice = parseClosePrice(responseBody);
            }

            return closePrice;
        } catch (Exception e) {
            log.error("ExchangeRateService | Error retrieving exchange rate: {}", e.getMessage());
            throw new ExchangeException("Failed to retrieve the exchange rate.");
        }
    }


    private BigDecimal parseClosePrice(String responseBody) {
        try {
            log.debug("ExchangeRateService | Parsing close price, response body:{}", responseBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode valuesNode = rootNode.path("values");
            if (valuesNode.isArray() && !valuesNode.isEmpty()) {
                JsonNode firstValueNode = valuesNode.get(0);
                String closePriceStr = firstValueNode.path("close").asText();
                return closePriceStr != null && !closePriceStr.isEmpty() ? new BigDecimal(closePriceStr) : null;
            } else {
                log.error("ExchangeRateService | The array of values is empty or not found in the API response.");
                return null;
            }
        } catch (Exception e) {
            log.error("ExchangeRateService | Error parsing Twelvedata API response: {}", e.getMessage());
            return null;
        }
    }
}
