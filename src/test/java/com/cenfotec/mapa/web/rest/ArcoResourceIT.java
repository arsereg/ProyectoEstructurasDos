package com.cenfotec.mapa.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cenfotec.mapa.IntegrationTest;
import com.cenfotec.mapa.domain.Arco;
import com.cenfotec.mapa.repository.ArcoRepository;
import com.cenfotec.mapa.service.dto.ArcoDTO;
import com.cenfotec.mapa.service.mapper.ArcoMapper;
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
 * Integration tests for the {@link ArcoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArcoResourceIT {

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    private static final String ENTITY_API_URL = "/api/arcos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArcoRepository arcoRepository;

    @Autowired
    private ArcoMapper arcoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArcoMockMvc;

    private Arco arco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arco createEntity(EntityManager em) {
        Arco arco = new Arco().weight(DEFAULT_WEIGHT);
        return arco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arco createUpdatedEntity(EntityManager em) {
        Arco arco = new Arco().weight(UPDATED_WEIGHT);
        return arco;
    }

    @BeforeEach
    public void initTest() {
        arco = createEntity(em);
    }

    @Test
    @Transactional
    void createArco() throws Exception {
        int databaseSizeBeforeCreate = arcoRepository.findAll().size();
        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);
        restArcoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arcoDTO)))
            .andExpect(status().isCreated());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeCreate + 1);
        Arco testArco = arcoList.get(arcoList.size() - 1);
        assertThat(testArco.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void createArcoWithExistingId() throws Exception {
        // Create the Arco with an existing ID
        arco.setId(1L);
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        int databaseSizeBeforeCreate = arcoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArcoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arcoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArcos() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        // Get all the arcoList
        restArcoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arco.getId().intValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getArco() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        // Get the arco
        restArcoMockMvc
            .perform(get(ENTITY_API_URL_ID, arco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arco.getId().intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingArco() throws Exception {
        // Get the arco
        restArcoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArco() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();

        // Update the arco
        Arco updatedArco = arcoRepository.findById(arco.getId()).get();
        // Disconnect from session so that the updates on updatedArco are not directly saved in db
        em.detach(updatedArco);
        updatedArco.weight(UPDATED_WEIGHT);
        ArcoDTO arcoDTO = arcoMapper.toDto(updatedArco);

        restArcoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arcoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arcoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
        Arco testArco = arcoList.get(arcoList.size() - 1);
        assertThat(testArco.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arcoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arcoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arcoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arcoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArcoWithPatch() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();

        // Update the arco using partial update
        Arco partialUpdatedArco = new Arco();
        partialUpdatedArco.setId(arco.getId());

        restArcoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArco))
            )
            .andExpect(status().isOk());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
        Arco testArco = arcoList.get(arcoList.size() - 1);
        assertThat(testArco.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateArcoWithPatch() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();

        // Update the arco using partial update
        Arco partialUpdatedArco = new Arco();
        partialUpdatedArco.setId(arco.getId());

        partialUpdatedArco.weight(UPDATED_WEIGHT);

        restArcoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArco))
            )
            .andExpect(status().isOk());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
        Arco testArco = arcoList.get(arcoList.size() - 1);
        assertThat(testArco.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arcoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arcoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arcoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArco() throws Exception {
        int databaseSizeBeforeUpdate = arcoRepository.findAll().size();
        arco.setId(count.incrementAndGet());

        // Create the Arco
        ArcoDTO arcoDTO = arcoMapper.toDto(arco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArcoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(arcoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arco in the database
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArco() throws Exception {
        // Initialize the database
        arcoRepository.saveAndFlush(arco);

        int databaseSizeBeforeDelete = arcoRepository.findAll().size();

        // Delete the arco
        restArcoMockMvc
            .perform(delete(ENTITY_API_URL_ID, arco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arco> arcoList = arcoRepository.findAll();
        assertThat(arcoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
