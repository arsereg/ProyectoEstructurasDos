package com.cenfotec.mapa.web.rest;

import com.cenfotec.mapa.repository.ArcoRepository;
import com.cenfotec.mapa.service.ArcoService;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cenfotec.mapa.domain.Arco}.
 */
@RestController
@RequestMapping("/api")
public class ArcoResource {

    private final Logger log = LoggerFactory.getLogger(ArcoResource.class);

    private static final String ENTITY_NAME = "arco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArcoService arcoService;

    private final ArcoRepository arcoRepository;

    public ArcoResource(ArcoService arcoService, ArcoRepository arcoRepository) {
        this.arcoService = arcoService;
        this.arcoRepository = arcoRepository;
    }

    /**
     * {@code POST  /arcos} : Create a new arco.
     *
     * @param arcoDTO the arcoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arcoDTO, or with status {@code 400 (Bad Request)} if the arco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arcos")
    public ResponseEntity<ArcoDTO> createArco(@RequestBody ArcoDTO arcoDTO) throws URISyntaxException {
        log.debug("REST request to save Arco : {}", arcoDTO);
        if (arcoDTO.getId() != null) {
            throw new BadRequestAlertException("A new arco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArcoDTO result = arcoService.save(arcoDTO);
        return ResponseEntity
            .created(new URI("/api/arcos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arcos/:id} : Updates an existing arco.
     *
     * @param id the id of the arcoDTO to save.
     * @param arcoDTO the arcoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arcoDTO,
     * or with status {@code 400 (Bad Request)} if the arcoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arcoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arcos/{id}")
    public ResponseEntity<ArcoDTO> updateArco(@PathVariable(value = "id", required = false) final Long id, @RequestBody ArcoDTO arcoDTO)
        throws URISyntaxException {
        log.debug("REST request to update Arco : {}, {}", id, arcoDTO);
        if (arcoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arcoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arcoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArcoDTO result = arcoService.save(arcoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arcoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arcos/:id} : Partial updates given fields of an existing arco, field will ignore if it is null
     *
     * @param id the id of the arcoDTO to save.
     * @param arcoDTO the arcoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arcoDTO,
     * or with status {@code 400 (Bad Request)} if the arcoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arcoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arcoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arcos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArcoDTO> partialUpdateArco(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArcoDTO arcoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Arco partially : {}, {}", id, arcoDTO);
        if (arcoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arcoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arcoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArcoDTO> result = arcoService.partialUpdate(arcoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arcoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arcos} : get all the arcos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arcos in body.
     */
    @GetMapping("/arcos")
    public ResponseEntity<List<ArcoDTO>> getAllArcos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Arcos");
        List<ArcoDTO> page = arcoService.findAll();
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /arcos/:id} : get the "id" arco.
     *
     * @param id the id of the arcoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arcoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arcos/{id}")
    public ResponseEntity<ArcoDTO> getArco(@PathVariable Long id) {
        log.debug("REST request to get Arco : {}", id);
        Optional<ArcoDTO> arcoDTO = arcoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arcoDTO);
    }

    /**
     * {@code DELETE  /arcos/:id} : delete the "id" arco.
     *
     * @param id the id of the arcoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arcos/{id}")
    public ResponseEntity<Void> deleteArco(@PathVariable Long id) {
        log.debug("REST request to delete Arco : {}", id);
        arcoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
