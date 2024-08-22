package edu.transaction.service.service;

import edu.transaction.service.mapper.TransactionMapper;
import edu.transaction.service.model.Limit;
import edu.transaction.service.model.Transaction;
import edu.transaction.service.model.enums.ExpenseCategory;
import edu.transaction.service.repository.LimitRepo;
import edu.transaction.service.repository.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LimitCheckServiceTest {

    @Mock
    private LimitRepo limitRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private LimitCheckService limitCheckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkLimitExceededTest() {

        Transaction transaction = Transaction
                .builder()
                .datetime(LocalDateTime.now())
                .expenseCategory(ExpenseCategory.service)
                .sum(new BigDecimal("1200"))
                .build();

        Limit limit = Limit
                .builder()
                .limitSum(new BigDecimal("1000"))
                .expenseCategory(ExpenseCategory.service)
                .startDatetime(YearMonth.from(transaction.getDatetime()).atDay(1).atStartOfDay())
                .endDatetime(YearMonth.from(transaction.getDatetime()).atEndOfMonth().atTime(23, 59, 59))
                .build();

        when(limitRepo.findAll()).thenReturn(List.of(limit));

        when(transactionRepo.findAllByExpenseCategoryAndDatetimeBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        when(exchangeRateService.convertToUSD(any())).thenReturn(new BigDecimal("1200"));

        limitCheckService.checkLimitExceeded(transaction);

        assertTrue(transaction.isLimitExceeded());

        verify(limitRepo, times(1)).findAll();
        verify(transactionRepo, times(1))
                .findAllByExpenseCategoryAndDatetimeBetween(any(), any(), any());
        verify(exchangeRateService, times(1)).convertToUSD(any());
    }
}
