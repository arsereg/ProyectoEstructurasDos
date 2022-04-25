package com.cenfotec.mapa.web.rest;

import com.cenfotec.mapa.repository.NodoRepository;
import com.cenfotec.mapa.service.GraphService;
import com.cenfotec.mapa.service.NodoService;
import com.cenfotec.mapa.service.dto.NodoDTO;
import com.cenfotec.mapa.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cenfotec.mapa.domain.Nodo}.
 */
@RestController
@RequestMapping("/api")
public class NodoResource {

    private final Logger log = LoggerFactory.getLogger(NodoResource.class);

    private static final String ENTITY_NAME = "nodo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NodoService nodoService;

    private final NodoRepository nodoRepository;

    private final GraphService graphService;

    public NodoResource(NodoService nodoService, NodoRepository nodoRepository, GraphService graphService) {
        this.nodoService = nodoService;
        this.nodoRepository = nodoRepository;
        this.graphService = graphService;
    }

    /**
     * {@code POST  /nodos} : Create a new nodo.
     *
     * @param nodoDTO the nodoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nodoDTO, or with status {@code 400 (Bad Request)} if the nodo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nodos")
    public ResponseEntity<NodoDTO> createNodo(@RequestBody NodoDTO nodoDTO) throws URISyntaxException {
        log.debug("REST request to save Nodo : {}", nodoDTO);
        if (nodoDTO.getId() != null) {
            throw new BadRequestAlertException("A new nodo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodoDTO result = nodoService.save(nodoDTO);
        return ResponseEntity
            .created(new URI("/api/nodos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nodos/:id} : Updates an existing nodo.
     *
     * @param id the id of the nodoDTO to save.
     * @param nodoDTO the nodoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nodoDTO,
     * or with status {@code 400 (Bad Request)} if the nodoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nodoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nodos/{id}")
    public ResponseEntity<NodoDTO> updateNodo(@PathVariable(value = "id", required = false) final Long id, @RequestBody NodoDTO nodoDTO)
        throws URISyntaxException {
        log.debug("REST request to update Nodo : {}, {}", id, nodoDTO);
        if (nodoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nodoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NodoDTO result = nodoService.save(nodoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nodoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nodos/:id} : Partial updates given fields of an existing nodo, field will ignore if it is null
     *
     * @param id the id of the nodoDTO to save.
     * @param nodoDTO the nodoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nodoDTO,
     * or with status {@code 400 (Bad Request)} if the nodoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nodoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nodoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nodos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NodoDTO> partialUpdateNodo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NodoDTO nodoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nodo partially : {}, {}", id, nodoDTO);
        if (nodoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nodoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NodoDTO> result = nodoService.partialUpdate(nodoDTO);
        graphService.initializeGraph();
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nodoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nodos} : get all the nodos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nodos in body.
     */
    @GetMapping("/nodos")
    public ResponseEntity<List<NodoDTO>> getAllNodos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Nodos");
        List<NodoDTO> page = nodoService.findAll();
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /nodos/:id} : get the "id" nodo.
     *
     * @param id the id of the nodoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nodoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nodos/{id}")
    public ResponseEntity<NodoDTO> getNodo(@PathVariable Long id) {
        log.debug("REST request to get Nodo : {}", id);
        Optional<NodoDTO> nodoDTO = nodoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nodoDTO);
    }

    /**
     * {@code DELETE  /nodos/:id} : delete the "id" nodo.
     *
     * @param id the id of the nodoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nodos/{id}")
    public ResponseEntity<Void> deleteNodo(@PathVariable Long id) {
        log.debug("REST request to delete Nodo : {}", id);
        nodoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
