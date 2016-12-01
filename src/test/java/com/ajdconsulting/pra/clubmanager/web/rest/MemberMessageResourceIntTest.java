package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.MemberMessage;
import com.ajdconsulting.pra.clubmanager.repository.MemberMessageRepository;

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
 * Test class for the MemberMessageResource REST controller.
 *
 * @see MemberMessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberMessageResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";
    private static final String DEFAULT_MESSAGE_TEXT = "AAAAA";
    private static final String UPDATED_MESSAGE_TEXT = "BBBBB";

    @Inject
    private MemberMessageRepository memberMessageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberMessageMockMvc;

    private MemberMessage memberMessage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberMessageResource memberMessageResource = new MemberMessageResource();
        ReflectionTestUtils.setField(memberMessageResource, "memberMessageRepository", memberMessageRepository);
        this.restMemberMessageMockMvc = MockMvcBuilders.standaloneSetup(memberMessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberMessage = new MemberMessage();
        memberMessage.setSubject(DEFAULT_SUBJECT);
        memberMessage.setMessageText(DEFAULT_MESSAGE_TEXT);
    }

    @Test
    @Transactional
    public void createMemberMessage() throws Exception {
        int databaseSizeBeforeCreate = memberMessageRepository.findAll().size();

        // Create the MemberMessage

        restMemberMessageMockMvc.perform(post("/api/memberMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMessage)))
                .andExpect(status().isCreated());

        // Validate the MemberMessage in the database
        List<MemberMessage> memberMessages = memberMessageRepository.findAll();
        assertThat(memberMessages).hasSize(databaseSizeBeforeCreate + 1);
        MemberMessage testMemberMessage = memberMessages.get(memberMessages.size() - 1);
        assertThat(testMemberMessage.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testMemberMessage.getMessageText()).isEqualTo(DEFAULT_MESSAGE_TEXT);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberMessageRepository.findAll().size();
        // set the field null
        memberMessage.setSubject(null);

        // Create the MemberMessage, which fails.

        restMemberMessageMockMvc.perform(post("/api/memberMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMessage)))
                .andExpect(status().isBadRequest());

        List<MemberMessage> memberMessages = memberMessageRepository.findAll();
        assertThat(memberMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberMessageRepository.findAll().size();
        // set the field null
        memberMessage.setMessageText(null);

        // Create the MemberMessage, which fails.

        restMemberMessageMockMvc.perform(post("/api/memberMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMessage)))
                .andExpect(status().isBadRequest());

        List<MemberMessage> memberMessages = memberMessageRepository.findAll();
        assertThat(memberMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberMessages() throws Exception {
        // Initialize the database
        memberMessageRepository.saveAndFlush(memberMessage);

        // Get all the memberMessages
        restMemberMessageMockMvc.perform(get("/api/memberMessages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberMessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].messageText").value(hasItem(DEFAULT_MESSAGE_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getMemberMessage() throws Exception {
        // Initialize the database
        memberMessageRepository.saveAndFlush(memberMessage);

        // Get the memberMessage
        restMemberMessageMockMvc.perform(get("/api/memberMessages/{id}", memberMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberMessage.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.messageText").value(DEFAULT_MESSAGE_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMemberMessage() throws Exception {
        // Get the memberMessage
        restMemberMessageMockMvc.perform(get("/api/memberMessages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberMessage() throws Exception {
        // Initialize the database
        memberMessageRepository.saveAndFlush(memberMessage);

		int databaseSizeBeforeUpdate = memberMessageRepository.findAll().size();

        // Update the memberMessage
        memberMessage.setSubject(UPDATED_SUBJECT);
        memberMessage.setMessageText(UPDATED_MESSAGE_TEXT);

        restMemberMessageMockMvc.perform(put("/api/memberMessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMessage)))
                .andExpect(status().isOk());

        // Validate the MemberMessage in the database
        List<MemberMessage> memberMessages = memberMessageRepository.findAll();
        assertThat(memberMessages).hasSize(databaseSizeBeforeUpdate);
        MemberMessage testMemberMessage = memberMessages.get(memberMessages.size() - 1);
        assertThat(testMemberMessage.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testMemberMessage.getMessageText()).isEqualTo(UPDATED_MESSAGE_TEXT);
    }

    @Test
    @Transactional
    public void deleteMemberMessage() throws Exception {
        // Initialize the database
        memberMessageRepository.saveAndFlush(memberMessage);

		int databaseSizeBeforeDelete = memberMessageRepository.findAll().size();

        // Get the memberMessage
        restMemberMessageMockMvc.perform(delete("/api/memberMessages/{id}", memberMessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MemberMessage> memberMessages = memberMessageRepository.findAll();
        assertThat(memberMessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
