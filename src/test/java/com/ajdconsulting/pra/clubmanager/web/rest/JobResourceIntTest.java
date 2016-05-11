package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.Job;
import com.ajdconsulting.pra.clubmanager.repository.JobRepository;

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
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Float DEFAULT_POINT_VALUE = 0F;
    private static final Float UPDATED_POINT_VALUE = 1F;

    private static final Float DEFAULT_CASH_VALUE = 1F;
    private static final Float UPDATED_CASH_VALUE = 2F;
    private static final String DEFAULT_JOB_DAY = "AAAAA";
    private static final String UPDATED_JOB_DAY = "BBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 0;
    private static final Integer UPDATED_SORT_ORDER = 1;

    private static final Boolean DEFAULT_RESERVED = false;
    private static final Boolean UPDATED_RESERVED = true;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobMockMvc;

    private Job job;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobResource jobResource = new JobResource();
        ReflectionTestUtils.setField(jobResource, "jobRepository", jobRepository);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        job = new Job();
        job.setTitle(DEFAULT_TITLE);
        job.setPointValue(DEFAULT_POINT_VALUE);
        job.setCashValue(DEFAULT_CASH_VALUE);
        job.setJobDay(DEFAULT_JOB_DAY);
        job.setReserved(DEFAULT_RESERVED);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getPointValue()).isEqualTo(DEFAULT_POINT_VALUE);
        assertThat(testJob.getCashValue()).isEqualTo(DEFAULT_CASH_VALUE);
        assertThat(testJob.getJobDay()).isEqualTo(DEFAULT_JOB_DAY);
        assertThat(testJob.getReserved()).isEqualTo(DEFAULT_RESERVED);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setTitle(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPointValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setPointValue(null);

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSortOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null

        // Create the Job, which fails.

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobs
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].pointValue").value(hasItem(DEFAULT_POINT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].cashValue").value(hasItem(DEFAULT_CASH_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].jobDay").value(hasItem(DEFAULT_JOB_DAY.toString())))
                .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
                .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.pointValue").value(DEFAULT_POINT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.cashValue").value(DEFAULT_CASH_VALUE.doubleValue()))
            .andExpect(jsonPath("$.jobDay").value(DEFAULT_JOB_DAY.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

		int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        job.setTitle(UPDATED_TITLE);
        job.setPointValue(UPDATED_POINT_VALUE);
        job.setCashValue(UPDATED_CASH_VALUE);
        job.setJobDay(UPDATED_JOB_DAY);
        job.setReserved(UPDATED_RESERVED);

        restJobMockMvc.perform(put("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJob.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
        assertThat(testJob.getCashValue()).isEqualTo(UPDATED_CASH_VALUE);
        assertThat(testJob.getJobDay()).isEqualTo(UPDATED_JOB_DAY);
        assertThat(testJob.getReserved()).isEqualTo(UPDATED_RESERVED);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

		int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
