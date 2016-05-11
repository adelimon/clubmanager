package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.MemberTypes;
import com.ajdconsulting.pra.clubmanager.repository.MemberTypesRepository;

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
 * Test class for the MemberTypesResource REST controller.
 *
 * @see MemberTypesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberTypesResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private MemberTypesRepository memberTypesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberTypesMockMvc;

    private MemberTypes memberTypes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberTypesResource memberTypesResource = new MemberTypesResource();
        ReflectionTestUtils.setField(memberTypesResource, "memberTypesRepository", memberTypesRepository);
        this.restMemberTypesMockMvc = MockMvcBuilders.standaloneSetup(memberTypesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberTypes = new MemberTypes();
        memberTypes.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createMemberTypes() throws Exception {
        int databaseSizeBeforeCreate = memberTypesRepository.findAll().size();

        // Create the MemberTypes

        restMemberTypesMockMvc.perform(post("/api/memberTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberTypes)))
                .andExpect(status().isCreated());

        // Validate the MemberTypes in the database
        List<MemberTypes> memberTypess = memberTypesRepository.findAll();
        assertThat(memberTypess).hasSize(databaseSizeBeforeCreate + 1);
        MemberTypes testMemberTypes = memberTypess.get(memberTypess.size() - 1);
        assertThat(testMemberTypes.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberTypesRepository.findAll().size();
        // set the field null
        memberTypes.setType(null);

        // Create the MemberTypes, which fails.

        restMemberTypesMockMvc.perform(post("/api/memberTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberTypes)))
                .andExpect(status().isBadRequest());

        List<MemberTypes> memberTypess = memberTypesRepository.findAll();
        assertThat(memberTypess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberTypess() throws Exception {
        // Initialize the database
        memberTypesRepository.saveAndFlush(memberTypes);

        // Get all the memberTypess
        restMemberTypesMockMvc.perform(get("/api/memberTypess?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberTypes.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getMemberTypes() throws Exception {
        // Initialize the database
        memberTypesRepository.saveAndFlush(memberTypes);

        // Get the memberTypes
        restMemberTypesMockMvc.perform(get("/api/memberTypess/{id}", memberTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberTypes.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMemberTypes() throws Exception {
        // Get the memberTypes
        restMemberTypesMockMvc.perform(get("/api/memberTypess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberTypes() throws Exception {
        // Initialize the database
        memberTypesRepository.saveAndFlush(memberTypes);

		int databaseSizeBeforeUpdate = memberTypesRepository.findAll().size();

        // Update the memberTypes
        memberTypes.setType(UPDATED_TYPE);

        restMemberTypesMockMvc.perform(put("/api/memberTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberTypes)))
                .andExpect(status().isOk());

        // Validate the MemberTypes in the database
        List<MemberTypes> memberTypess = memberTypesRepository.findAll();
        assertThat(memberTypess).hasSize(databaseSizeBeforeUpdate);
        MemberTypes testMemberTypes = memberTypess.get(memberTypess.size() - 1);
        assertThat(testMemberTypes.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteMemberTypes() throws Exception {
        // Initialize the database
        memberTypesRepository.saveAndFlush(memberTypes);

		int databaseSizeBeforeDelete = memberTypesRepository.findAll().size();

        // Get the memberTypes
        restMemberTypesMockMvc.perform(delete("/api/memberTypess/{id}", memberTypes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MemberTypes> memberTypess = memberTypesRepository.findAll();
        assertThat(memberTypess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
