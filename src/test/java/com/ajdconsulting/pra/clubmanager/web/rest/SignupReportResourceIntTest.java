package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.SignupReport;
import com.ajdconsulting.pra.clubmanager.repository.SignupReportRepository;

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
 * Test class for the SignupReportResource REST controller.
 *
 * @see SignupReportResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SignupReportResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Float DEFAULT_POINT_VALUE = 1F;
    private static final Float UPDATED_POINT_VALUE = 2F;

    private static final Float DEFAULT_CASH_VALUE = 1F;
    private static final Float UPDATED_CASH_VALUE = 2F;

    private static final Boolean DEFAULT_RESERVED = false;
    private static final Boolean UPDATED_RESERVED = true;
    private static final String DEFAULT_JOB_DAY = "AAAAA";
    private static final String UPDATED_JOB_DAY = "BBBBB";
    private static final String DEFAULT_LEADER = "AAAAA";
    private static final String UPDATED_LEADER = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SignupReportRepository signupReportRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSignupReportMockMvc;

    private SignupReport signupReport;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignupReportResource signupReportResource = new SignupReportResource();
        ReflectionTestUtils.setField(signupReportResource, "signupReportRepository", signupReportRepository);
        this.restSignupReportMockMvc = MockMvcBuilders.standaloneSetup(signupReportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        signupReport = new SignupReport();
        signupReport.setName(DEFAULT_NAME);
        signupReport.setTitle(DEFAULT_TITLE);
        signupReport.setPointValue(DEFAULT_POINT_VALUE);
        signupReport.setCashValue(DEFAULT_CASH_VALUE);
        signupReport.setReserved(DEFAULT_RESERVED);
        signupReport.setJobDay(DEFAULT_JOB_DAY);
        signupReport.setLeader(DEFAULT_LEADER);
        signupReport.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createSignupReport() throws Exception {
        int databaseSizeBeforeCreate = signupReportRepository.findAll().size();

        // Create the SignupReport

        restSignupReportMockMvc.perform(post("/api/signupReports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signupReport)))
                .andExpect(status().isCreated());

        // Validate the SignupReport in the database
        List<SignupReport> signupReports = signupReportRepository.findAll();
        assertThat(signupReports).hasSize(databaseSizeBeforeCreate + 1);
        SignupReport testSignupReport = signupReports.get(signupReports.size() - 1);
        assertThat(testSignupReport.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSignupReport.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSignupReport.getPointValue()).isEqualTo(DEFAULT_POINT_VALUE);
        assertThat(testSignupReport.getCashValue()).isEqualTo(DEFAULT_CASH_VALUE);
        assertThat(testSignupReport.getReserved()).isEqualTo(DEFAULT_RESERVED);
        assertThat(testSignupReport.getJobDay()).isEqualTo(DEFAULT_JOB_DAY);
        assertThat(testSignupReport.getLeader()).isEqualTo(DEFAULT_LEADER);
        assertThat(testSignupReport.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void getAllSignupReports() throws Exception {
        // Initialize the database
        signupReportRepository.saveAndFlush(signupReport);

        // Get all the signupReports
        restSignupReportMockMvc.perform(get("/api/signupReports?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signupReport.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].pointValue").value(hasItem(DEFAULT_POINT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].cashValue").value(hasItem(DEFAULT_CASH_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())))
                .andExpect(jsonPath("$.[*].jobDay").value(hasItem(DEFAULT_JOB_DAY.toString())))
                .andExpect(jsonPath("$.[*].leader").value(hasItem(DEFAULT_LEADER.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSignupReport() throws Exception {
        // Initialize the database
        signupReportRepository.saveAndFlush(signupReport);

        // Get the signupReport
        restSignupReportMockMvc.perform(get("/api/signupReports/{id}", signupReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(signupReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.pointValue").value(DEFAULT_POINT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.cashValue").value(DEFAULT_CASH_VALUE.doubleValue()))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.booleanValue()))
            .andExpect(jsonPath("$.jobDay").value(DEFAULT_JOB_DAY.toString()))
            .andExpect(jsonPath("$.leader").value(DEFAULT_LEADER.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignupReport() throws Exception {
        // Get the signupReport
        restSignupReportMockMvc.perform(get("/api/signupReports/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignupReport() throws Exception {
        // Initialize the database
        signupReportRepository.saveAndFlush(signupReport);

		int databaseSizeBeforeUpdate = signupReportRepository.findAll().size();

        // Update the signupReport
        signupReport.setName(UPDATED_NAME);
        signupReport.setTitle(UPDATED_TITLE);
        signupReport.setPointValue(UPDATED_POINT_VALUE);
        signupReport.setCashValue(UPDATED_CASH_VALUE);
        signupReport.setReserved(UPDATED_RESERVED);
        signupReport.setJobDay(UPDATED_JOB_DAY);
        signupReport.setLeader(UPDATED_LEADER);
        signupReport.setDate(UPDATED_DATE);

        restSignupReportMockMvc.perform(put("/api/signupReports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signupReport)))
                .andExpect(status().isOk());

        // Validate the SignupReport in the database
        List<SignupReport> signupReports = signupReportRepository.findAll();
        assertThat(signupReports).hasSize(databaseSizeBeforeUpdate);
        SignupReport testSignupReport = signupReports.get(signupReports.size() - 1);
        assertThat(testSignupReport.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSignupReport.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSignupReport.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
        assertThat(testSignupReport.getCashValue()).isEqualTo(UPDATED_CASH_VALUE);
        assertThat(testSignupReport.getReserved()).isEqualTo(UPDATED_RESERVED);
        assertThat(testSignupReport.getJobDay()).isEqualTo(UPDATED_JOB_DAY);
        assertThat(testSignupReport.getLeader()).isEqualTo(UPDATED_LEADER);
        assertThat(testSignupReport.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteSignupReport() throws Exception {
        // Initialize the database
        signupReportRepository.saveAndFlush(signupReport);

		int databaseSizeBeforeDelete = signupReportRepository.findAll().size();

        // Get the signupReport
        restSignupReportMockMvc.perform(delete("/api/signupReports/{id}", signupReport.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SignupReport> signupReports = signupReportRepository.findAll();
        assertThat(signupReports).hasSize(databaseSizeBeforeDelete - 1);
    }
}
