package com.cenfotec.mapa.repository;

import com.cenfotec.mapa.domain.Arco;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Arco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArcoRepository extends JpaRepository<Arco, Long> {}
