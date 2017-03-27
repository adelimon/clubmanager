package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.Integration;
import com.ajdconsulting.pra.clubmanager.repository.IntegrationRepository;

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
 * Test class for the IntegrationResource REST controller.
 *
 * @see IntegrationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IntegrationResourceIntTest {

    private static final String DEFAULT_PLATFORM = "AAAAA";
    private static final String UPDATED_PLATFORM = "BBBBB";
    private static final String DEFAULT_APIKEY = "AAAAA";
    private static final String UPDATED_APIKEY = "BBBBB";

    @Inject
    private IntegrationRepository integrationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIntegrationMockMvc;

    private Integration integration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IntegrationResource integrationResource = new IntegrationResource();
        ReflectionTestUtils.setField(integrationResource, "integrationRepository", integrationRepository);
        this.restIntegrationMockMvc = MockMvcBuilders.standaloneSetup(integrationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        integration = new Integration();
        integration.setPlatform(DEFAULT_PLATFORM);
        integration.setApikey(DEFAULT_APIKEY);
    }

    @Test
    @Transactional
    public void createIntegration() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();

        // Create the Integration

        restIntegrationMockMvc.perform(post("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integration)))
                .andExpect(status().isCreated());

        // Validate the Integration in the database
        List<Integration> integrations = integrationRepository.findAll();
        assertThat(integrations).hasSize(databaseSizeBeforeCreate + 1);
        Integration testIntegration = integrations.get(integrations.size() - 1);
        assertThat(testIntegration.getPlatform()).isEqualTo(DEFAULT_PLATFORM);
        assertThat(testIntegration.getApikey()).isEqualTo(DEFAULT_APIKEY);
    }

    @Test
    @Transactional
    public void checkPlatformIsRequired() throws Exception {
        int databaseSizeBeforeTest = integrationRepository.findAll().size();
        // set the field null
        integration.setPlatform(null);

        // Create the Integration, which fails.

        restIntegrationMockMvc.perform(post("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integration)))
                .andExpect(status().isBadRequest());

        List<Integration> integrations = integrationRepository.findAll();
        assertThat(integrations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApikeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = integrationRepository.findAll().size();
        // set the field null
        integration.setApikey(null);

        // Create the Integration, which fails.

        restIntegrationMockMvc.perform(post("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integration)))
                .andExpect(status().isBadRequest());

        List<Integration> integrations = integrationRepository.findAll();
        assertThat(integrations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIntegrations() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get all the integrations
        restIntegrationMockMvc.perform(get("/api/integrations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(integration.getId().intValue())))
                .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.toString())))
                .andExpect(jsonPath("$.[*].apikey").value(hasItem(DEFAULT_APIKEY.toString())));
    }

    @Test
    @Transactional
    public void getIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", integration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(integration.getId().intValue()))
            .andExpect(jsonPath("$.platform").value(DEFAULT_PLATFORM.toString()))
            .andExpect(jsonPath("$.apikey").value(DEFAULT_APIKEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIntegration() throws Exception {
        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

		int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Update the integration
        integration.setPlatform(UPDATED_PLATFORM);
        integration.setApikey(UPDATED_APIKEY);

        restIntegrationMockMvc.perform(put("/api/integrations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(integration)))
                .andExpect(status().isOk());

        // Validate the Integration in the database
        List<Integration> integrations = integrationRepository.findAll();
        assertThat(integrations).hasSize(databaseSizeBeforeUpdate);
        Integration testIntegration = integrations.get(integrations.size() - 1);
        assertThat(testIntegration.getPlatform()).isEqualTo(UPDATED_PLATFORM);
        assertThat(testIntegration.getApikey()).isEqualTo(UPDATED_APIKEY);
    }

    @Test
    @Transactional
    public void deleteIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

		int databaseSizeBeforeDelete = integrationRepository.findAll().size();

        // Get the integration
        restIntegrationMockMvc.perform(delete("/api/integrations/{id}", integration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Integration> integrations = integrationRepository.findAll();
        assertThat(integrations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
