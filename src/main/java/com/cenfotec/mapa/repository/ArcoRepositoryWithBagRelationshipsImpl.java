package com.cenfotec.mapa.repository;

import com.cenfotec.mapa.domain.Arco;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ArcoRepositoryWithBagRelationshipsImpl implements ArcoRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Arco> fetchBagRelationships(Optional<Arco> arco) {
        return arco.map(this::fetchFroms).map(this::fetchTos);
    }

    @Override
    public Page<Arco> fetchBagRelationships(Page<Arco> arcos) {
        return new PageImpl<>(fetchBagRelationships(arcos.getContent()), arcos.getPageable(), arcos.getTotalElements());
    }

    @Override
    public List<Arco> fetchBagRelationships(List<Arco> arcos) {
        return Optional.of(arcos).map(this::fetchFroms).map(this::fetchTos).get();
    }

    Arco fetchFroms(Arco result) {
        return entityManager
            .createQuery("select arco from Arco arco left join fetch arco.froms where arco is :arco", Arco.class)
            .setParameter("arco", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Arco> fetchFroms(List<Arco> arcos) {
        return entityManager
            .createQuery("select distinct arco from Arco arco left join fetch arco.froms where arco in :arcos", Arco.class)
            .setParameter("arcos", arcos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Arco fetchTos(Arco result) {
        return entityManager
            .createQuery("select arco from Arco arco left join fetch arco.tos where arco is :arco", Arco.class)
            .setParameter("arco", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Arco> fetchTos(List<Arco> arcos) {
        return entityManager
            .createQuery("select distinct arco from Arco arco left join fetch arco.tos where arco in :arcos", Arco.class)
            .setParameter("arcos", arcos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
