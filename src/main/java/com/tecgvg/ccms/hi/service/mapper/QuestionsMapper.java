package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.Questions;
import com.tecgvg.ccms.hi.service.dto.QuestionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Questions} and its DTO {@link QuestionsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionsMapper extends EntityMapper<QuestionsDTO, Questions> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionsDTO toDtoId(Questions questions);
}
