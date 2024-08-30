package edu.transaction.service.mapper;

import edu.transaction.service.dto.TransactionDTO;
import edu.transaction.service.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction transaction) {
        log.debug("TransactionMapper | Covert transaction to DTO:{}", transaction);
        return TransactionDTO.builder()
                .accountFrom(transaction.getAccountFrom())
                .accountTo(transaction.getAccountTo())
                .sum(transaction.getSum())
                .currency(transaction.getCurrency())
                .expenseCategory(transaction.getExpenseCategory())
                .build();
    }
}
