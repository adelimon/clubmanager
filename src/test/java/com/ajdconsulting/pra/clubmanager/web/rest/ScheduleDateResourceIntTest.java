package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;
import com.ajdconsulting.pra.clubmanager.repository.ScheduleDateRepository;

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
 * Test class for the ScheduleDateResource REST controller.
 *
 * @see ScheduleDateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ScheduleDateResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ScheduleDateRepository scheduleDateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScheduleDateMockMvc;

    private ScheduleDate scheduleDate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleDateResource scheduleDateResource = new ScheduleDateResource();
        ReflectionTestUtils.setField(scheduleDateResource, "scheduleDateRepository", scheduleDateRepository);
        this.restScheduleDateMockMvc = MockMvcBuilders.standaloneSetup(scheduleDateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scheduleDate = new ScheduleDate();
        scheduleDate.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createScheduleDate() throws Exception {
        int databaseSizeBeforeCreate = scheduleDateRepository.findAll().size();

        // Create the ScheduleDate

        restScheduleDateMockMvc.perform(post("/api/scheduleDates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleDate)))
                .andExpect(status().isCreated());

        // Validate the ScheduleDate in the database
        List<ScheduleDate> scheduleDates = scheduleDateRepository.findAll();
        assertThat(scheduleDates).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleDate testScheduleDate = scheduleDates.get(scheduleDates.size() - 1);
        assertThat(testScheduleDate.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleDateRepository.findAll().size();
        // set the field null
        scheduleDate.setDate(null);

        // Create the ScheduleDate, which fails.

        restScheduleDateMockMvc.perform(post("/api/scheduleDates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleDate)))
                .andExpect(status().isBadRequest());

        List<ScheduleDate> scheduleDates = scheduleDateRepository.findAll();
        assertThat(scheduleDates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScheduleDates() throws Exception {
        // Initialize the database
        scheduleDateRepository.saveAndFlush(scheduleDate);

        // Get all the scheduleDates
        restScheduleDateMockMvc.perform(get("/api/scheduleDates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleDate.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getScheduleDate() throws Exception {
        // Initialize the database
        scheduleDateRepository.saveAndFlush(scheduleDate);

        // Get the scheduleDate
        restScheduleDateMockMvc.perform(get("/api/scheduleDates/{id}", scheduleDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scheduleDate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScheduleDate() throws Exception {
        // Get the scheduleDate
        restScheduleDateMockMvc.perform(get("/api/scheduleDates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduleDate() throws Exception {
        // Initialize the database
        scheduleDateRepository.saveAndFlush(scheduleDate);

		int databaseSizeBeforeUpdate = scheduleDateRepository.findAll().size();

        // Update the scheduleDate
        scheduleDate.setDate(UPDATED_DATE);

        restScheduleDateMockMvc.perform(put("/api/scheduleDates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleDate)))
                .andExpect(status().isOk());

        // Validate the ScheduleDate in the database
        List<ScheduleDate> scheduleDates = scheduleDateRepository.findAll();
        assertThat(scheduleDates).hasSize(databaseSizeBeforeUpdate);
        ScheduleDate testScheduleDate = scheduleDates.get(scheduleDates.size() - 1);
        assertThat(testScheduleDate.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteScheduleDate() throws Exception {
        // Initialize the database
        scheduleDateRepository.saveAndFlush(scheduleDate);

		int databaseSizeBeforeDelete = scheduleDateRepository.findAll().size();

        // Get the scheduleDate
        restScheduleDateMockMvc.perform(delete("/api/scheduleDates/{id}", scheduleDate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ScheduleDate> scheduleDates = scheduleDateRepository.findAll();
        assertThat(scheduleDates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
