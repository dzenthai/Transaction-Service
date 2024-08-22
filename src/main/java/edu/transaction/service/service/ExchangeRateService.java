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
            log.info("ExchangeRateService | Поиск курса в базе данных.");
            return optionalRate.get().getRate();
        }

        BigDecimal rate = fetchExchangeRateFromAPI(currencyPair, date);

        if (rate != null) {

            log.info("ExchangeRateService | Получение курса через обращение к API.");

            ExchangeRate exchangeRate = ExchangeRate.builder()
                    .currencyPair(currencyPair)
                    .rate(rate)
                    .date(date)
                    .build();

            exchangeRateRepo.save(exchangeRate);
        }

        Optional<ExchangeRate> lastAvailableRate = exchangeRateRepo.findTopByCurrencyPairOrderByDateDesc(currencyPair);

        if (lastAvailableRate.isPresent()) {
            log.info("ExchangeRateService | Поиск последнего доступного курса.");
            if (lastAvailableRate.get().getDate().isBefore(date)) {
                return lastAvailableRate.get().getRate();
            }
        }

        return rate != null ? rate : lastAvailableRate.map(ExchangeRate::getRate)
                .orElseThrow(() -> new ExchangeException("ExchangeRateService | Нет доступного обменного курса."));
    }

    @Transactional
    public BigDecimal convertToUSD(TransactionDTO transactionDTO) {
        if (transactionDTO.currency() == Currency.USD) {
            return transactionDTO.sum();
        }
        BigDecimal exchangeRate = getExchangeRate(transactionDTO.currency().name() + "/USD", LocalDate.now());
        log.info("ExchangeRateService | Конвертация {} в USD", transactionDTO.currency().name());
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
            log.info("ExchangeRateService | Полный ответ от Twelvedata API: {}", responseBody);

            BigDecimal closePrice = parseClosePrice(responseBody);

            if (closePrice == null) {
                log.info("ExchangeRateService | Данные закрытия на {} недоступны, пытаемся получить предыдущий закрытый курс.", date);
                url = String.format(
                        "https://api.twelvedata.com/time_series?symbol=%s&interval=1day&start_date=%s&apikey=%s&format=JSON&previous_close=true",
                        currencyPair, date, apiKey);

                response = client.prepareGet(url)
                        .execute()
                        .toCompletableFuture()
                        .join();

                responseBody = response.getResponseBody();
                log.info("ExchangeRateService | Полный ответ от Twelvedata API с previous_close=true: {}", responseBody);

                closePrice = parseClosePrice(responseBody);
            }

            return closePrice;
        } catch (Exception e) {
            log.error("ExchangeRateService | Ошибка при получении курса валют: {}", e.getMessage());
            throw new ExchangeException("Не удалось получить курс валют");
        }
    }


    private BigDecimal parseClosePrice(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode valuesNode = rootNode.path("values");
            if (valuesNode.isArray() && !valuesNode.isEmpty()) {
                JsonNode firstValueNode = valuesNode.get(0);
                String closePriceStr = firstValueNode.path("close").asText();
                return closePriceStr != null && !closePriceStr.isEmpty() ? new BigDecimal(closePriceStr) : null;
            } else {
                log.error("ExchangeRateService | Массив значений пуст или не найден в ответе API.");
                return null;
            }
        } catch (Exception e) {
            log.error("ExchangeRateService | Ошибка при парсинге ответа Twelvedata API: {}", e.getMessage());
            return null;
        }
    }
}
