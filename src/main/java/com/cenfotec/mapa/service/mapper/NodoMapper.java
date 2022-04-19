package com.cenfotec.mapa.service.mapper;

import com.cenfotec.mapa.domain.Nodo;
import com.cenfotec.mapa.service.dto.NodoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nodo} and its DTO {@link NodoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodoMapper extends EntityMapper<NodoDTO, Nodo> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NodoDTO toDtoId(Nodo nodo);
}
