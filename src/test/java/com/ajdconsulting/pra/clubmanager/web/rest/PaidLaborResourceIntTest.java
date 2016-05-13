package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.PaidLabor;
import com.ajdconsulting.pra.clubmanager.repository.PaidLaborRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PaidLaborResource REST controller.
 *
 * @see PaidLaborResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PaidLaborResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PaidLaborRepository paidLaborRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaidLaborMockMvc;

    private PaidLabor paidLabor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaidLaborResource paidLaborResource = new PaidLaborResource();
        ReflectionTestUtils.setField(paidLaborResource, "paidLaborRepository", paidLaborRepository);
        this.restPaidLaborMockMvc = MockMvcBuilders.standaloneSetup(paidLaborResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paidLabor = new PaidLabor();
        paidLabor.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPaidLabor() throws Exception {
        int databaseSizeBeforeCreate = paidLaborRepository.findAll().size();

        // Create the PaidLabor

        restPaidLaborMockMvc.perform(post("/api/paidLabors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paidLabor)))
                .andExpect(status().isCreated());

        // Validate the PaidLabor in the database
        List<PaidLabor> paidLabors = paidLaborRepository.findAll();
        assertThat(paidLabors).hasSize(databaseSizeBeforeCreate + 1);
        PaidLabor testPaidLabor = paidLabors.get(paidLabors.size() - 1);
        assertThat(testPaidLabor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paidLaborRepository.findAll().size();
        // set the field null
        paidLabor.setName(null);

        // Create the PaidLabor, which fails.

        restPaidLaborMockMvc.perform(post("/api/paidLabors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paidLabor)))
                .andExpect(status().isBadRequest());

        List<PaidLabor> paidLabors = paidLaborRepository.findAll();
        assertThat(paidLabors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaidLabors() throws Exception {
        // Initialize the database
        paidLaborRepository.saveAndFlush(paidLabor);

        // Get all the paidLabors
        restPaidLaborMockMvc.perform(get("/api/paidLabors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paidLabor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPaidLabor() throws Exception {
        // Initialize the database
        paidLaborRepository.saveAndFlush(paidLabor);

        // Get the paidLabor
        restPaidLaborMockMvc.perform(get("/api/paidLabors/{id}", paidLabor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paidLabor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaidLabor() throws Exception {
        // Get the paidLabor
        restPaidLaborMockMvc.perform(get("/api/paidLabors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaidLabor() throws Exception {
        // Initialize the database
        paidLaborRepository.saveAndFlush(paidLabor);

		int databaseSizeBeforeUpdate = paidLaborRepository.findAll().size();

        // Update the paidLabor
        paidLabor.setName(UPDATED_NAME);

        restPaidLaborMockMvc.perform(put("/api/paidLabors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paidLabor)))
                .andExpect(status().isOk());

        // Validate the PaidLabor in the database
        List<PaidLabor> paidLabors = paidLaborRepository.findAll();
        assertThat(paidLabors).hasSize(databaseSizeBeforeUpdate);
        PaidLabor testPaidLabor = paidLabors.get(paidLabors.size() - 1);
        assertThat(testPaidLabor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePaidLabor() throws Exception {
        // Initialize the database
        paidLaborRepository.saveAndFlush(paidLabor);

		int databaseSizeBeforeDelete = paidLaborRepository.findAll().size();

        // Get the paidLabor
        restPaidLaborMockMvc.perform(delete("/api/paidLabors/{id}", paidLabor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PaidLabor> paidLabors = paidLaborRepository.findAll();
        assertThat(paidLabors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
