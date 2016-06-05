package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.RidingAreaStatus;
import com.ajdconsulting.pra.clubmanager.repository.RidingAreaStatusRepository;

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
 * Test class for the RidingAreaStatusResource REST controller.
 *
 * @see RidingAreaStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RidingAreaStatusResourceIntTest {

    private static final String DEFAULT_AREA_NAME = "AAAAA";
    private static final String UPDATED_AREA_NAME = "BBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Inject
    private RidingAreaStatusRepository ridingAreaStatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRidingAreaStatusMockMvc;

    private RidingAreaStatus ridingAreaStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RidingAreaStatusResource ridingAreaStatusResource = new RidingAreaStatusResource();
        ReflectionTestUtils.setField(ridingAreaStatusResource, "ridingAreaStatusRepository", ridingAreaStatusRepository);
        this.restRidingAreaStatusMockMvc = MockMvcBuilders.standaloneSetup(ridingAreaStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ridingAreaStatus = new RidingAreaStatus();
        ridingAreaStatus.setAreaName(DEFAULT_AREA_NAME);
        ridingAreaStatus.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRidingAreaStatus() throws Exception {
        int databaseSizeBeforeCreate = ridingAreaStatusRepository.findAll().size();

        // Create the RidingAreaStatus

        restRidingAreaStatusMockMvc.perform(post("/api/ridingAreaStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ridingAreaStatus)))
                .andExpect(status().isCreated());

        // Validate the RidingAreaStatus in the database
        List<RidingAreaStatus> ridingAreaStatuss = ridingAreaStatusRepository.findAll();
        assertThat(ridingAreaStatuss).hasSize(databaseSizeBeforeCreate + 1);
        RidingAreaStatus testRidingAreaStatus = ridingAreaStatuss.get(ridingAreaStatuss.size() - 1);
        assertThat(testRidingAreaStatus.getAreaName()).isEqualTo(DEFAULT_AREA_NAME);
        assertThat(testRidingAreaStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkAreaNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ridingAreaStatusRepository.findAll().size();
        // set the field null
        ridingAreaStatus.setAreaName(null);

        // Create the RidingAreaStatus, which fails.

        restRidingAreaStatusMockMvc.perform(post("/api/ridingAreaStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ridingAreaStatus)))
                .andExpect(status().isBadRequest());

        List<RidingAreaStatus> ridingAreaStatuss = ridingAreaStatusRepository.findAll();
        assertThat(ridingAreaStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ridingAreaStatusRepository.findAll().size();
        // set the field null
        ridingAreaStatus.setStatus(null);

        // Create the RidingAreaStatus, which fails.

        restRidingAreaStatusMockMvc.perform(post("/api/ridingAreaStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ridingAreaStatus)))
                .andExpect(status().isBadRequest());

        List<RidingAreaStatus> ridingAreaStatuss = ridingAreaStatusRepository.findAll();
        assertThat(ridingAreaStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRidingAreaStatuss() throws Exception {
        // Initialize the database
        ridingAreaStatusRepository.saveAndFlush(ridingAreaStatus);

        // Get all the ridingAreaStatuss
        restRidingAreaStatusMockMvc.perform(get("/api/ridingAreaStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ridingAreaStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].areaName").value(hasItem(DEFAULT_AREA_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getRidingAreaStatus() throws Exception {
        // Initialize the database
        ridingAreaStatusRepository.saveAndFlush(ridingAreaStatus);

        // Get the ridingAreaStatus
        restRidingAreaStatusMockMvc.perform(get("/api/ridingAreaStatuss/{id}", ridingAreaStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ridingAreaStatus.getId().intValue()))
            .andExpect(jsonPath("$.areaName").value(DEFAULT_AREA_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRidingAreaStatus() throws Exception {
        // Get the ridingAreaStatus
        restRidingAreaStatusMockMvc.perform(get("/api/ridingAreaStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRidingAreaStatus() throws Exception {
        // Initialize the database
        ridingAreaStatusRepository.saveAndFlush(ridingAreaStatus);

		int databaseSizeBeforeUpdate = ridingAreaStatusRepository.findAll().size();

        // Update the ridingAreaStatus
        ridingAreaStatus.setAreaName(UPDATED_AREA_NAME);
        ridingAreaStatus.setStatus(UPDATED_STATUS);

        restRidingAreaStatusMockMvc.perform(put("/api/ridingAreaStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ridingAreaStatus)))
                .andExpect(status().isOk());

        // Validate the RidingAreaStatus in the database
        List<RidingAreaStatus> ridingAreaStatuss = ridingAreaStatusRepository.findAll();
        assertThat(ridingAreaStatuss).hasSize(databaseSizeBeforeUpdate);
        RidingAreaStatus testRidingAreaStatus = ridingAreaStatuss.get(ridingAreaStatuss.size() - 1);
        assertThat(testRidingAreaStatus.getAreaName()).isEqualTo(UPDATED_AREA_NAME);
        assertThat(testRidingAreaStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteRidingAreaStatus() throws Exception {
        // Initialize the database
        ridingAreaStatusRepository.saveAndFlush(ridingAreaStatus);

		int databaseSizeBeforeDelete = ridingAreaStatusRepository.findAll().size();

        // Get the ridingAreaStatus
        restRidingAreaStatusMockMvc.perform(delete("/api/ridingAreaStatuss/{id}", ridingAreaStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RidingAreaStatus> ridingAreaStatuss = ridingAreaStatusRepository.findAll();
        assertThat(ridingAreaStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
