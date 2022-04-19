package com.cenfotec.mapa.service.mapper;

import com.cenfotec.mapa.domain.Arco;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arco} and its DTO {@link ArcoDTO}.
 */
@Mapper(componentModel = "spring", uses = { NodoMapper.class })
public interface ArcoMapper extends EntityMapper<ArcoDTO, Arco> {
    @Mapping(target = "from", source = "from", qualifiedByName = "id")
    @Mapping(target = "to", source = "to", qualifiedByName = "id")
    ArcoDTO toDto(Arco s);
}
