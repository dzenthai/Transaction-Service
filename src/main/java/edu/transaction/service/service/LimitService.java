package edu.transaction.service.service;

import edu.transaction.service.dto.LimitDTO;
import edu.transaction.service.exception.LimitException;
import edu.transaction.service.mapper.LimitMapper;
import edu.transaction.service.model.Limit;
import edu.transaction.service.model.enums.ExpenseCategory;
import edu.transaction.service.repository.LimitRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class LimitService {

    private final LimitRepo limitRepo;

    private final LimitMapper limitMapper;

    @Transactional
    public List<LimitDTO> getAllLimits() {
        log.info("LimitService | Retrieving the list of all limits");
        return limitMapper.toDTOList(limitRepo.findAll());
    }

    @Transactional
    public void setLimit(LimitDTO limitDTO) {

        var startDatetime = limitDTO.datetime();

        var startMonth = YearMonth.from(limitDTO.datetime()).getMonth();

        var endDatetime = startDatetime.plusMonths(1);

        List<Limit> oldLimits = limitRepo.findAllByExpenseCategory(limitDTO.expenseCategory())
                .stream()
                .filter(limit -> limit.getStartDatetime().getMonth().compareTo(startMonth) == 0)
                .toList();

        if (!oldLimits.isEmpty()) {
            throw new LimitException("Unable to update the limit. Please add a new limit.");
        }

        Limit limit = Limit.builder()
                .limitSum(limitDTO.limit())
                .expenseCategory(limitDTO.expenseCategory())
                .startDatetime(startDatetime)
                .endDatetime(endDatetime)
                .build();

        log.info("LimitService | Saving expense category limit: {}, limit: {} USD, datetime: {}",
                limit.getExpenseCategory(), limit.getLimitSum(), limit.getStartDatetime());

        limitRepo.save(limit);
    }

    public Limit createDefaultLimit(LocalDateTime transactionDateTime, ExpenseCategory expenseCategory) {
        log.debug("LimitCheckService | Setting default monthly limit to 20,000 USD");

        LocalDateTime startOfMonth = transactionDateTime.withDayOfMonth(1);

        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        return Limit.builder()
                .limitSum(new BigDecimal(20000))
                .startDatetime(startOfMonth)
                .endDatetime(endOfMonth)
                .expenseCategory(expenseCategory)
                .build();
    }
}
