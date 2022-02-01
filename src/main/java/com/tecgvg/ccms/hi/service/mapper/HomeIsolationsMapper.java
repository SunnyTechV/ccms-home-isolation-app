package com.tecgvg.ccms.hi.service.mapper;

import com.tecgvg.ccms.hi.domain.HomeIsolations;
import com.tecgvg.ccms.hi.service.dto.HomeIsolationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HomeIsolations} and its DTO {@link HomeIsolationsDTO}.
 */
@Mapper(componentModel = "spring", uses = { IsolationsDetailsMapper.class })
public interface HomeIsolationsMapper extends EntityMapper<HomeIsolationsDTO, HomeIsolations> {
    @Mapping(target = "isolationsDetails", source = "isolationsDetails", qualifiedByName = "id")
    HomeIsolationsDTO toDto(HomeIsolations s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HomeIsolationsDTO toDtoId(HomeIsolations homeIsolations);
}
