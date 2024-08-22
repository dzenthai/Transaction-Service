package edu.transaction.service.mapper;

import edu.transaction.service.dto.LimitDTO;
import edu.transaction.service.model.Limit;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LimitMapper {

    public LimitDTO toDTO(Limit limit) {
        return LimitDTO.builder()
                .limit(limit.getLimitSum())
                .expenseCategory(limit.getExpenseCategory())
                .datetime(limit.getStartDatetime())
                .build();
    }

    public List<LimitDTO> toDTOList(List<Limit> limits) {
        return limits.stream()
                .map(this::toDTO)
                .toList();
    }
}
