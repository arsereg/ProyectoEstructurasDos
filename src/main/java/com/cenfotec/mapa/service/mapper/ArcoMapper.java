package com.cenfotec.mapa.service.mapper;

import com.cenfotec.mapa.domain.Arco;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arco} and its DTO {@link ArcoDTO}.
 */
@Mapper(componentModel = "spring", uses = { NodoMapper.class })
public interface ArcoMapper extends EntityMapper<ArcoDTO, Arco> {
    @Mapping(target = "froms", source = "froms", qualifiedByName = "idSet")
    @Mapping(target = "tos", source = "tos", qualifiedByName = "idSet")
    ArcoDTO toDto(Arco s);

    @Mapping(target = "removeFrom", ignore = true)
    @Mapping(target = "removeTo", ignore = true)
    Arco toEntity(ArcoDTO arcoDTO);
}
