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

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


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
    public List<LimitDTO> getAllLimitsByExpenseCategory(ExpenseCategory expenseCategory) {
        log.info("LimitService | Retrieving the limit by expense category");
        return limitMapper.toDTOList(limitRepo.findAllByExpenseCategory(expenseCategory));
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
}
