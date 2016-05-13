package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.MemberStatus;
import com.ajdconsulting.pra.clubmanager.repository.MemberStatusRepository;

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
 * Test class for the MemberStatusResource REST controller.
 *
 * @see MemberStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberStatusResourceIntTest {


    private static final Integer DEFAULT_YEAR = 1961;
    private static final Integer UPDATED_YEAR = 1962;

    @Inject
    private MemberStatusRepository memberStatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberStatusMockMvc;

    private MemberStatus memberStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberStatusResource memberStatusResource = new MemberStatusResource();
        ReflectionTestUtils.setField(memberStatusResource, "memberStatusRepository", memberStatusRepository);
        this.restMemberStatusMockMvc = MockMvcBuilders.standaloneSetup(memberStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberStatus = new MemberStatus();
        memberStatus.setYear(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createMemberStatus() throws Exception {
        int databaseSizeBeforeCreate = memberStatusRepository.findAll().size();

        // Create the MemberStatus

        restMemberStatusMockMvc.perform(post("/api/memberStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberStatus)))
                .andExpect(status().isCreated());

        // Validate the MemberStatus in the database
        List<MemberStatus> memberStatuss = memberStatusRepository.findAll();
        assertThat(memberStatuss).hasSize(databaseSizeBeforeCreate + 1);
        MemberStatus testMemberStatus = memberStatuss.get(memberStatuss.size() - 1);
        assertThat(testMemberStatus.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberStatusRepository.findAll().size();
        // set the field null
        memberStatus.setYear(null);

        // Create the MemberStatus, which fails.

        restMemberStatusMockMvc.perform(post("/api/memberStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberStatus)))
                .andExpect(status().isBadRequest());

        List<MemberStatus> memberStatuss = memberStatusRepository.findAll();
        assertThat(memberStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberStatuss() throws Exception {
        // Initialize the database
        memberStatusRepository.saveAndFlush(memberStatus);

        // Get all the memberStatuss
        restMemberStatusMockMvc.perform(get("/api/memberStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getMemberStatus() throws Exception {
        // Initialize the database
        memberStatusRepository.saveAndFlush(memberStatus);

        // Get the memberStatus
        restMemberStatusMockMvc.perform(get("/api/memberStatuss/{id}", memberStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberStatus.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingMemberStatus() throws Exception {
        // Get the memberStatus
        restMemberStatusMockMvc.perform(get("/api/memberStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberStatus() throws Exception {
        // Initialize the database
        memberStatusRepository.saveAndFlush(memberStatus);

		int databaseSizeBeforeUpdate = memberStatusRepository.findAll().size();

        // Update the memberStatus
        memberStatus.setYear(UPDATED_YEAR);

        restMemberStatusMockMvc.perform(put("/api/memberStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberStatus)))
                .andExpect(status().isOk());

        // Validate the MemberStatus in the database
        List<MemberStatus> memberStatuss = memberStatusRepository.findAll();
        assertThat(memberStatuss).hasSize(databaseSizeBeforeUpdate);
        MemberStatus testMemberStatus = memberStatuss.get(memberStatuss.size() - 1);
        assertThat(testMemberStatus.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deleteMemberStatus() throws Exception {
        // Initialize the database
        memberStatusRepository.saveAndFlush(memberStatus);

		int databaseSizeBeforeDelete = memberStatusRepository.findAll().size();

        // Get the memberStatus
        restMemberStatusMockMvc.perform(delete("/api/memberStatuss/{id}", memberStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MemberStatus> memberStatuss = memberStatusRepository.findAll();
        assertThat(memberStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
