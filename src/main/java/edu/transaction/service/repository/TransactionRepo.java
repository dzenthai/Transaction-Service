package edu.transaction.service.repository;

import edu.transaction.service.model.Transaction;
import edu.transaction.service.model.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByExpenseCategoryAndDatetimeBetween(
            ExpenseCategory expenseCategory,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime);

}
