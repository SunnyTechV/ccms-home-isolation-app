package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import com.tecgvg.ccms.hi.service.dto.AssessmentAnswersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssessmentAnswers} and its DTO {@link AssessmentAnswersDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionsMapper.class, AssessmentMapper.class })
public interface AssessmentAnswersMapper extends EntityMapper<AssessmentAnswersDTO, AssessmentAnswers> {
    @Mapping(target = "questions", source = "questions", qualifiedByName = "id")
    @Mapping(target = "assessment", source = "assessment", qualifiedByName = "id")
    AssessmentAnswersDTO toDto(AssessmentAnswers s);
}
