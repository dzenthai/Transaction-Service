package edu.transaction.service.service;

import edu.transaction.service.dto.TransactionDTO;
import edu.transaction.service.exception.TransactionException;
import edu.transaction.service.mapper.TransactionMapper;
import edu.transaction.service.model.Transaction;
import edu.transaction.service.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepo transactionRepo;

    private final LimitCheckService limitCheckService;

    private final TransactionMapper transactionMapper;

    @Transactional
    public void saveTransaction(TransactionDTO transactionDTO) {
        log.info("TransactionService | Сохранение транзакции");

        Transaction transaction = Transaction.builder()
                .accountFrom(transactionDTO.accountFrom())
                .accountTo(transactionDTO.accountTo())
                .sum(transactionDTO.sum())
                .currency(transactionDTO.currency())
                .expenseCategory(transactionDTO.expenseCategory())
                .datetime(LocalDateTime.now())
                .build();

        limitCheckService.checkLimitExceeded(transaction);

        if (transaction.isLimitExceeded()) {
            throw new TransactionException("Превышен лимит транзакций");
        }

        transactionRepo.save(transaction);

    }
}
