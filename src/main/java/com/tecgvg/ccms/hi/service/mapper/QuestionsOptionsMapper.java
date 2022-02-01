package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.QuestionsOptions;
import com.tecgvg.ccms.hi.service.dto.QuestionsOptionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionsOptions} and its DTO {@link QuestionsOptionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionsMapper.class })
public interface QuestionsOptionsMapper extends EntityMapper<QuestionsOptionsDTO, QuestionsOptions> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "id")
    QuestionsOptionsDTO toDto(QuestionsOptions s);
}
