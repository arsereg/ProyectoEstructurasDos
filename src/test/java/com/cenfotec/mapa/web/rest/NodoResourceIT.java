package com.cenfotec.mapa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cenfotec.mapa.IntegrationTest;
import com.cenfotec.mapa.domain.Nodo;
import com.cenfotec.mapa.repository.NodoRepository;
import com.cenfotec.mapa.service.dto.NodoDTO;
import com.cenfotec.mapa.service.mapper.NodoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NodoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NodoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    private static final String ENTITY_API_URL = "/api/nodos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NodoRepository nodoRepository;

    @Autowired
    private NodoMapper nodoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNodoMockMvc;

    private Nodo nodo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nodo createEntity(EntityManager em) {
        Nodo nodo = new Nodo().name(DEFAULT_NAME).x(DEFAULT_X).y(DEFAULT_Y);
        return nodo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nodo createUpdatedEntity(EntityManager em) {
        Nodo nodo = new Nodo().name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);
        return nodo;
    }

    @BeforeEach
    public void initTest() {
        nodo = createEntity(em);
    }

    @Test
    @Transactional
    void createNodo() throws Exception {
        int databaseSizeBeforeCreate = nodoRepository.findAll().size();
        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);
        restNodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nodoDTO)))
            .andExpect(status().isCreated());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeCreate + 1);
        Nodo testNodo = nodoList.get(nodoList.size() - 1);
        assertThat(testNodo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNodo.getX()).isEqualTo(DEFAULT_X);
        assertThat(testNodo.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createNodoWithExistingId() throws Exception {
        // Create the Nodo with an existing ID
        nodo.setId(1L);
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        int databaseSizeBeforeCreate = nodoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nodoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNodos() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        // Get all the nodoList
        restNodoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nodo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @Test
    @Transactional
    void getNodo() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        // Get the nodo
        restNodoMockMvc
            .perform(get(ENTITY_API_URL_ID, nodo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nodo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingNodo() throws Exception {
        // Get the nodo
        restNodoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNodo() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();

        // Update the nodo
        Nodo updatedNodo = nodoRepository.findById(nodo.getId()).get();
        // Disconnect from session so that the updates on updatedNodo are not directly saved in db
        em.detach(updatedNodo);
        updatedNodo.name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);
        NodoDTO nodoDTO = nodoMapper.toDto(updatedNodo);

        restNodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nodoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
        Nodo testNodo = nodoList.get(nodoList.size() - 1);
        assertThat(testNodo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNodo.getX()).isEqualTo(UPDATED_X);
        assertThat(testNodo.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nodoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nodoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nodoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNodoWithPatch() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();

        // Update the nodo using partial update
        Nodo partialUpdatedNodo = new Nodo();
        partialUpdatedNodo.setId(nodo.getId());

        partialUpdatedNodo.y(UPDATED_Y);

        restNodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNodo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNodo))
            )
            .andExpect(status().isOk());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
        Nodo testNodo = nodoList.get(nodoList.size() - 1);
        assertThat(testNodo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNodo.getX()).isEqualTo(DEFAULT_X);
        assertThat(testNodo.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void fullUpdateNodoWithPatch() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();

        // Update the nodo using partial update
        Nodo partialUpdatedNodo = new Nodo();
        partialUpdatedNodo.setId(nodo.getId());

        partialUpdatedNodo.name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);

        restNodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNodo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNodo))
            )
            .andExpect(status().isOk());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
        Nodo testNodo = nodoList.get(nodoList.size() - 1);
        assertThat(testNodo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNodo.getX()).isEqualTo(UPDATED_X);
        assertThat(testNodo.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nodoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nodoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nodoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNodo() throws Exception {
        int databaseSizeBeforeUpdate = nodoRepository.findAll().size();
        nodo.setId(count.incrementAndGet());

        // Create the Nodo
        NodoDTO nodoDTO = nodoMapper.toDto(nodo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNodoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nodoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nodo in the database
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNodo() throws Exception {
        // Initialize the database
        nodoRepository.saveAndFlush(nodo);

        int databaseSizeBeforeDelete = nodoRepository.findAll().size();

        // Delete the nodo
        restNodoMockMvc
            .perform(delete(ENTITY_API_URL_ID, nodo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nodo> nodoList = nodoRepository.findAll();
        assertThat(nodoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
