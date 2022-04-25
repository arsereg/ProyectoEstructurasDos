package com.cenfotec.mapa.service.mapper;

import com.cenfotec.mapa.domain.Nodo;
import com.cenfotec.mapa.service.dto.NodoDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nodo} and its DTO {@link NodoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodoMapper extends EntityMapper<NodoDTO, Nodo> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<NodoDTO> toDtoIdSet(Set<Nodo> nodo);
}
