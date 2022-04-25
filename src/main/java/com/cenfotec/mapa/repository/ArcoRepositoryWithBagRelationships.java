package com.cenfotec.mapa.repository;

import com.cenfotec.mapa.domain.Arco;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ArcoRepositoryWithBagRelationships {
    Optional<Arco> fetchBagRelationships(Optional<Arco> arco);

    List<Arco> fetchBagRelationships(List<Arco> arcos);

    Page<Arco> fetchBagRelationships(Page<Arco> arcos);
}
