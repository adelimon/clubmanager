package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import com.ajdconsulting.pra.clubmanager.repository.EarnedPointsRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EarnedPointsResource REST controller.
 *
 * @see EarnedPointsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EarnedPointsResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_POINT_VALUE = 0F;
    private static final Float UPDATED_POINT_VALUE = 1F;

    @Inject
    private EarnedPointsRepository earnedPointsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEarnedPointsMockMvc;

    private EarnedPoints earnedPoints;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EarnedPointsResource earnedPointsResource = new EarnedPointsResource();
        ReflectionTestUtils.setField(earnedPointsResource, "earnedPointsRepository", earnedPointsRepository);
        this.restEarnedPointsMockMvc = MockMvcBuilders.standaloneSetup(earnedPointsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        earnedPoints = new EarnedPoints();
        earnedPoints.setDate(DEFAULT_DATE);
        earnedPoints.setDescription(DEFAULT_DESCRIPTION);
        earnedPoints.setPointValue(DEFAULT_POINT_VALUE);
    }

    @Test
    @Transactional
    public void createEarnedPoints() throws Exception {
        int databaseSizeBeforeCreate = earnedPointsRepository.findAll().size();

        // Create the EarnedPoints

        restEarnedPointsMockMvc.perform(post("/api/earnedPointss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(earnedPoints)))
                .andExpect(status().isCreated());

        // Validate the EarnedPoints in the database
        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeCreate + 1);
        EarnedPoints testEarnedPoints = earnedPointss.get(earnedPointss.size() - 1);
        assertThat(testEarnedPoints.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEarnedPoints.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEarnedPoints.getPointValue()).isEqualTo(DEFAULT_POINT_VALUE);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = earnedPointsRepository.findAll().size();
        // set the field null
        earnedPoints.setDate(null);

        // Create the EarnedPoints, which fails.

        restEarnedPointsMockMvc.perform(post("/api/earnedPointss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(earnedPoints)))
                .andExpect(status().isBadRequest());

        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = earnedPointsRepository.findAll().size();
        // set the field null
        earnedPoints.setDescription(null);

        // Create the EarnedPoints, which fails.

        restEarnedPointsMockMvc.perform(post("/api/earnedPointss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(earnedPoints)))
                .andExpect(status().isBadRequest());

        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPointValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = earnedPointsRepository.findAll().size();
        // set the field null
        earnedPoints.setPointValue(null);

        // Create the EarnedPoints, which fails.

        restEarnedPointsMockMvc.perform(post("/api/earnedPointss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(earnedPoints)))
                .andExpect(status().isBadRequest());

        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEarnedPointss() throws Exception {
        // Initialize the database
        earnedPointsRepository.saveAndFlush(earnedPoints);

        // Get all the earnedPointss
        restEarnedPointsMockMvc.perform(get("/api/earnedPointss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(earnedPoints.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].pointValue").value(hasItem(DEFAULT_POINT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getEarnedPoints() throws Exception {
        // Initialize the database
        earnedPointsRepository.saveAndFlush(earnedPoints);

        // Get the earnedPoints
        restEarnedPointsMockMvc.perform(get("/api/earnedPointss/{id}", earnedPoints.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(earnedPoints.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pointValue").value(DEFAULT_POINT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEarnedPoints() throws Exception {
        // Get the earnedPoints
        restEarnedPointsMockMvc.perform(get("/api/earnedPointss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEarnedPoints() throws Exception {
        // Initialize the database
        earnedPointsRepository.saveAndFlush(earnedPoints);

		int databaseSizeBeforeUpdate = earnedPointsRepository.findAll().size();

        // Update the earnedPoints
        earnedPoints.setDate(UPDATED_DATE);
        earnedPoints.setDescription(UPDATED_DESCRIPTION);
        earnedPoints.setPointValue(UPDATED_POINT_VALUE);

        restEarnedPointsMockMvc.perform(put("/api/earnedPointss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(earnedPoints)))
                .andExpect(status().isOk());

        // Validate the EarnedPoints in the database
        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeUpdate);
        EarnedPoints testEarnedPoints = earnedPointss.get(earnedPointss.size() - 1);
        assertThat(testEarnedPoints.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEarnedPoints.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEarnedPoints.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
    }

    @Test
    @Transactional
    public void deleteEarnedPoints() throws Exception {
        // Initialize the database
        earnedPointsRepository.saveAndFlush(earnedPoints);

		int databaseSizeBeforeDelete = earnedPointsRepository.findAll().size();

        // Get the earnedPoints
        restEarnedPointsMockMvc.perform(delete("/api/earnedPointss/{id}", earnedPoints.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EarnedPoints> earnedPointss = earnedPointsRepository.findAll();
        assertThat(earnedPointss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
