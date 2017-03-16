package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.MemberWork;
import com.ajdconsulting.pra.clubmanager.repository.MemberWorkRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MemberWorkResource REST controller.
 *
 * @see MemberWorkResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberWorkResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_STR = dateTimeFormatter.format(DEFAULT_START);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_STR = dateTimeFormatter.format(DEFAULT_END);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MemberWorkRepository memberWorkRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberWorkMockMvc;

    private MemberWork memberWork;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberWorkResource memberWorkResource = new MemberWorkResource();
        ReflectionTestUtils.setField(memberWorkResource, "memberWorkRepository", memberWorkRepository);
        this.restMemberWorkMockMvc = MockMvcBuilders.standaloneSetup(memberWorkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberWork = new MemberWork();
        memberWork.setStart(DEFAULT_START);
        memberWork.setEnd(DEFAULT_END);
        memberWork.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMemberWork() throws Exception {
        int databaseSizeBeforeCreate = memberWorkRepository.findAll().size();

        // Create the MemberWork

        restMemberWorkMockMvc.perform(post("/api/memberWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberWork)))
                .andExpect(status().isCreated());

        // Validate the MemberWork in the database
        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeCreate + 1);
        MemberWork testMemberWork = memberWorks.get(memberWorks.size() - 1);
        assertThat(testMemberWork.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testMemberWork.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testMemberWork.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberWorkRepository.findAll().size();
        // set the field null
        memberWork.setStart(null);

        // Create the MemberWork, which fails.

        restMemberWorkMockMvc.perform(post("/api/memberWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberWork)))
                .andExpect(status().isBadRequest());

        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberWorkRepository.findAll().size();
        // set the field null
        memberWork.setEnd(null);

        // Create the MemberWork, which fails.

        restMemberWorkMockMvc.perform(post("/api/memberWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberWork)))
                .andExpect(status().isBadRequest());

        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberWorkRepository.findAll().size();
        // set the field null
        memberWork.setDescription(null);

        // Create the MemberWork, which fails.

        restMemberWorkMockMvc.perform(post("/api/memberWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberWork)))
                .andExpect(status().isBadRequest());

        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberWorks() throws Exception {
        // Initialize the database
        memberWorkRepository.saveAndFlush(memberWork);

        // Get all the memberWorks
        restMemberWorkMockMvc.perform(get("/api/memberWorks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberWork.getId().intValue())))
                .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START_STR)))
                .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMemberWork() throws Exception {
        // Initialize the database
        memberWorkRepository.saveAndFlush(memberWork);

        // Get the memberWork
        restMemberWorkMockMvc.perform(get("/api/memberWorks/{id}", memberWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberWork.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START_STR))
            .andExpect(jsonPath("$.end").value(DEFAULT_END_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMemberWork() throws Exception {
        // Get the memberWork
        restMemberWorkMockMvc.perform(get("/api/memberWorks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberWork() throws Exception {
        // Initialize the database
        memberWorkRepository.saveAndFlush(memberWork);

		int databaseSizeBeforeUpdate = memberWorkRepository.findAll().size();

        // Update the memberWork
        memberWork.setStart(UPDATED_START);
        memberWork.setEnd(UPDATED_END);
        memberWork.setDescription(UPDATED_DESCRIPTION);

        restMemberWorkMockMvc.perform(put("/api/memberWorks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberWork)))
                .andExpect(status().isOk());

        // Validate the MemberWork in the database
        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeUpdate);
        MemberWork testMemberWork = memberWorks.get(memberWorks.size() - 1);
        assertThat(testMemberWork.getStart()).isEqualTo(UPDATED_START);
        assertThat(testMemberWork.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testMemberWork.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteMemberWork() throws Exception {
        // Initialize the database
        memberWorkRepository.saveAndFlush(memberWork);

		int databaseSizeBeforeDelete = memberWorkRepository.findAll().size();

        // Get the memberWork
        restMemberWorkMockMvc.perform(delete("/api/memberWorks/{id}", memberWork.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MemberWork> memberWorks = memberWorkRepository.findAll();
        assertThat(memberWorks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
