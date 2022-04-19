package com.cenfotec.mapa.repository;

import com.cenfotec.mapa.domain.Nodo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodoRepository extends JpaRepository<Nodo, Long> {}
