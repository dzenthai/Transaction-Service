package edu.transaction.service.repository;

import edu.transaction.service.model.Limit;
import edu.transaction.service.model.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LimitRepo extends JpaRepository<Limit, Long> {

    List<Limit> findAllByExpenseCategory(
            ExpenseCategory expenseCategory);

}
