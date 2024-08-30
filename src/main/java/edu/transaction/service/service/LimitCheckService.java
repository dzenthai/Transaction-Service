package edu.transaction.service.service;

import edu.transaction.service.mapper.TransactionMapper;
import edu.transaction.service.model.Limit;
import edu.transaction.service.model.Transaction;
import edu.transaction.service.repository.LimitRepo;
import edu.transaction.service.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class LimitCheckService {

    private final LimitRepo limitRepo;

    private final TransactionRepo transactionRepo;

    private final ExchangeRateService exchangeRateService;

    private final TransactionMapper transactionMapper;

    private final LimitService limitService;

    @Transactional
    public void checkLimitExceeded(Transaction transaction) {
        LocalDateTime transactionDateTime = transaction.getDatetime();

        List<Limit> applicableLimits = limitRepo.findAll().stream()
                .filter(limit -> limit.getExpenseCategory() == transaction.getExpenseCategory() &&
                        transactionDateTime.isAfter(limit.getStartDatetime()) &&
                        transactionDateTime.isBefore(limit.getEndDatetime()))
                .toList();

        Limit currentLimit = applicableLimits.isEmpty() ?
                limitService.createDefaultLimit(transactionDateTime, transaction.getExpenseCategory()) :
                applicableLimits.getFirst();

        BigDecimal transactionSumInUSD = exchangeRateService.convertToUSD(transactionMapper.toDTO(transaction));

        BigDecimal totalTransactionsAmountInUSD = transactionRepo.findAllByExpenseCategoryAndDatetimeBetween(
                        transaction.getExpenseCategory(),
                        currentLimit.getStartDatetime(),
                        currentLimit.getEndDatetime())
                .stream()
                .map(t-> exchangeRateService.convertToUSD(transactionMapper.toDTO(t)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("LimitCheckService | Checking transaction for exceeding the monthly limit");

        transaction.setLimitExceeded(totalTransactionsAmountInUSD.add(transactionSumInUSD).compareTo(currentLimit.getLimitSum()) > 0);
    }
}
