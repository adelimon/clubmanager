package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.Signup;
import com.ajdconsulting.pra.clubmanager.repository.SignupRepository;

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
 * Test class for the SignupResource REST controller.
 *
 * @see SignupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SignupResourceIntTest {


    @Inject
    private SignupRepository signupRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSignupMockMvc;

    private Signup signup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignupResource signupResource = new SignupResource();
        ReflectionTestUtils.setField(signupResource, "signupRepository", signupRepository);
        this.restSignupMockMvc = MockMvcBuilders.standaloneSetup(signupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        signup = new Signup();
    }

    @Test
    @Transactional
    public void createSignup() throws Exception {
        int databaseSizeBeforeCreate = signupRepository.findAll().size();

        // Create the Signup

        restSignupMockMvc.perform(post("/api/signups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signup)))
                .andExpect(status().isCreated());

        // Validate the Signup in the database
        List<Signup> signups = signupRepository.findAll();
        assertThat(signups).hasSize(databaseSizeBeforeCreate + 1);
        Signup testSignup = signups.get(signups.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSignups() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

        // Get all the signups
        restSignupMockMvc.perform(get("/api/signups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signup.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

        // Get the signup
        restSignupMockMvc.perform(get("/api/signups/{id}", signup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(signup.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSignup() throws Exception {
        // Get the signup
        restSignupMockMvc.perform(get("/api/signups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

		int databaseSizeBeforeUpdate = signupRepository.findAll().size();

        // Update the signup

        restSignupMockMvc.perform(put("/api/signups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signup)))
                .andExpect(status().isOk());

        // Validate the Signup in the database
        List<Signup> signups = signupRepository.findAll();
        assertThat(signups).hasSize(databaseSizeBeforeUpdate);
        Signup testSignup = signups.get(signups.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

		int databaseSizeBeforeDelete = signupRepository.findAll().size();

        // Get the signup
        restSignupMockMvc.perform(delete("/api/signups/{id}", signup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Signup> signups = signupRepository.findAll();
        assertThat(signups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
