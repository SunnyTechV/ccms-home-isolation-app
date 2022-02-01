package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.IsolationsDetails;
import com.tecgvg.ccms.hi.service.dto.IsolationsDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IsolationsDetails} and its DTO {@link IsolationsDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IsolationsDetailsMapper extends EntityMapper<IsolationsDetailsDTO, IsolationsDetails> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IsolationsDetailsDTO toDtoId(IsolationsDetails isolationsDetails);
}
