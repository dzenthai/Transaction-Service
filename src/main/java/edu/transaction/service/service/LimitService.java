package edu.transaction.service.service;

import edu.transaction.service.dto.LimitDTO;
import edu.transaction.service.exception.LimitException;
import edu.transaction.service.mapper.LimitMapper;
import edu.transaction.service.model.Limit;
import edu.transaction.service.repository.LimitRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.info("LimitService | Получение списка всех лимитов");
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
            throw new LimitException("Обновление лимита невозможно. Пожалуйста добавьте новый лимит.");
        }

        Limit limit = Limit.builder()
                .limitSum(limitDTO.limit())
                .expenseCategory(limitDTO.expenseCategory())
                .startDatetime(startDatetime)
                .endDatetime(endDatetime)
                .build();

        log.info("LimitService | Сохранение лимита на категорию расходов:{}, лимит:{} USD, Дата и время:{}",
                limit.getExpenseCategory(), limit.getLimitSum(), limit.getStartDatetime());

        limitRepo.save(limit);
    }
}
