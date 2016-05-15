package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.PaidSignup;
import com.ajdconsulting.pra.clubmanager.repository.PaidSignupRepository;

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
 * Test class for the PaidSignupResource REST controller.
 *
 * @see PaidSignupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PaidSignupResourceIntTest {


    @Inject
    private PaidSignupRepository paidSignupRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaidSignupMockMvc;

    private PaidSignup paidSignup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaidSignupResource paidSignupResource = new PaidSignupResource();
        ReflectionTestUtils.setField(paidSignupResource, "paidSignupRepository", paidSignupRepository);
        this.restPaidSignupMockMvc = MockMvcBuilders.standaloneSetup(paidSignupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paidSignup = new PaidSignup();
    }

    @Test
    @Transactional
    public void createPaidSignup() throws Exception {
        int databaseSizeBeforeCreate = paidSignupRepository.findAll().size();

        // Create the PaidSignup

        restPaidSignupMockMvc.perform(post("/api/paidSignups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paidSignup)))
                .andExpect(status().isCreated());

        // Validate the PaidSignup in the database
        List<PaidSignup> paidSignups = paidSignupRepository.findAll();
        assertThat(paidSignups).hasSize(databaseSizeBeforeCreate + 1);
        PaidSignup testPaidSignup = paidSignups.get(paidSignups.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPaidSignups() throws Exception {
        // Initialize the database
        paidSignupRepository.saveAndFlush(paidSignup);

        // Get all the paidSignups
        restPaidSignupMockMvc.perform(get("/api/paidSignups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paidSignup.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPaidSignup() throws Exception {
        // Initialize the database
        paidSignupRepository.saveAndFlush(paidSignup);

        // Get the paidSignup
        restPaidSignupMockMvc.perform(get("/api/paidSignups/{id}", paidSignup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paidSignup.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPaidSignup() throws Exception {
        // Get the paidSignup
        restPaidSignupMockMvc.perform(get("/api/paidSignups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaidSignup() throws Exception {
        // Initialize the database
        paidSignupRepository.saveAndFlush(paidSignup);

		int databaseSizeBeforeUpdate = paidSignupRepository.findAll().size();

        // Update the paidSignup

        restPaidSignupMockMvc.perform(put("/api/paidSignups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paidSignup)))
                .andExpect(status().isOk());

        // Validate the PaidSignup in the database
        List<PaidSignup> paidSignups = paidSignupRepository.findAll();
        assertThat(paidSignups).hasSize(databaseSizeBeforeUpdate);
        PaidSignup testPaidSignup = paidSignups.get(paidSignups.size() - 1);
    }

    @Test
    @Transactional
    public void deletePaidSignup() throws Exception {
        // Initialize the database
        paidSignupRepository.saveAndFlush(paidSignup);

		int databaseSizeBeforeDelete = paidSignupRepository.findAll().size();

        // Get the paidSignup
        restPaidSignupMockMvc.perform(delete("/api/paidSignups/{id}", paidSignup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PaidSignup> paidSignups = paidSignupRepository.findAll();
        assertThat(paidSignups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
