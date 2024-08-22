package edu.transaction.service.model;

import edu.transaction.service.model.enums.Currency;
import edu.transaction.service.model.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "account_from")
    private Long accountFrom;

    @Column(name = "account_to")
    private Long accountTo;

    @Column(name = "sum", precision = 15, scale = 2)
    private BigDecimal sum;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category")
    private ExpenseCategory expenseCategory;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Transient
    private boolean limitExceeded;
}
