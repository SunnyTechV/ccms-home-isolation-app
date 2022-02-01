package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.Assessment;
import com.tecgvg.ccms.hi.service.dto.AssessmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assessment} and its DTO {@link AssessmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { HomeIsolationsMapper.class })
public interface AssessmentMapper extends EntityMapper<AssessmentDTO, Assessment> {
    @Mapping(target = "homeIsolations", source = "homeIsolations", qualifiedByName = "id")
    AssessmentDTO toDto(Assessment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssessmentDTO toDtoId(Assessment assessment);
}
