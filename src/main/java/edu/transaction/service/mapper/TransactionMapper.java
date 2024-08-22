package edu.transaction.service.mapper;

import edu.transaction.service.dto.TransactionDTO;
import edu.transaction.service.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .accountFrom(transaction.getAccountFrom())
                .accountTo(transaction.getAccountTo())
                .sum(transaction.getSum())
                .currency(transaction.getCurrency())
                .expenseCategory(transaction.getExpenseCategory())
                .build();
    }

    public List<TransactionDTO> toDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toDTO)
                .toList();
    }
}
