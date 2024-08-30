package edu.transaction.service.mapper;

import edu.transaction.service.dto.LimitDTO;
import edu.transaction.service.model.Limit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class LimitMapper {

    public LimitDTO toDTO(Limit limit) {
        log.debug("LimitMapper | Convert limit to DTO:{}", limit);
        return LimitDTO.builder()
                .limit(limit.getLimitSum())
                .expenseCategory(limit.getExpenseCategory())
                .datetime(limit.getStartDatetime())
                .build();
    }

    public List<LimitDTO> toDTOList(List<Limit> limits) {
        log.debug("LimitMapper | Convert list of limits to list DTO:{}", limits);
        return limits.stream()
                .map(this::toDTO)
                .toList();
    }
}
