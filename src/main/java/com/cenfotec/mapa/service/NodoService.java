package com.cenfotec.mapa.service;

import com.cenfotec.mapa.service.dto.NodoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.cenfotec.mapa.domain.Nodo}.
 */
public interface NodoService {
    /**
     * Save a nodo.
     *
     * @param nodoDTO the entity to save.
     * @return the persisted entity.
     */
    NodoDTO save(NodoDTO nodoDTO);

    /**
     * Partially updates a nodo.
     *
     * @param nodoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NodoDTO> partialUpdate(NodoDTO nodoDTO);

    /**
     * Get all the nodos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NodoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" nodo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NodoDTO> findOne(Long id);

    /**
     * Delete the "id" nodo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
