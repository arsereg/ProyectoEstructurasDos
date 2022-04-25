package com.cenfotec.mapa.service;

import com.cenfotec.mapa.service.dto.ArcoDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cenfotec.mapa.domain.Arco}.
 */
public interface ArcoService {
    /**
     * Save a arco.
     *
     * @param arcoDTO the entity to save.
     * @return the persisted entity.
     */
    ArcoDTO save(ArcoDTO arcoDTO);

    /**
     * Partially updates a arco.
     *
     * @param arcoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArcoDTO> partialUpdate(ArcoDTO arcoDTO);

    /**
     * Get all the arcos.
     *
     * @return the list of entities.
     */
    List<ArcoDTO> findAll();

    /**
     * Get all the arcos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArcoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" arco.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArcoDTO> findOne(Long id);

    /**
     * Delete the "id" arco.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
