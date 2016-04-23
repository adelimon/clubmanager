package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.MemberNote;
import com.ajdconsulting.pra.clubmanager.repository.MemberNoteRepository;

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
 * Test class for the MemberNoteResource REST controller.
 *
 * @see MemberNoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberNoteResourceIntTest {

    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MemberNoteRepository memberNoteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberNoteMockMvc;

    private MemberNote memberNote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberNoteResource memberNoteResource = new MemberNoteResource();
        ReflectionTestUtils.setField(memberNoteResource, "memberNoteRepository", memberNoteRepository);
        this.restMemberNoteMockMvc = MockMvcBuilders.standaloneSetup(memberNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberNote = new MemberNote();
        memberNote.setNotes(DEFAULT_NOTES);
        memberNote.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createMemberNote() throws Exception {
        int databaseSizeBeforeCreate = memberNoteRepository.findAll().size();

        // Create the MemberNote

        restMemberNoteMockMvc.perform(post("/api/memberNotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberNote)))
                .andExpect(status().isCreated());

        // Validate the MemberNote in the database
        List<MemberNote> memberNotes = memberNoteRepository.findAll();
        assertThat(memberNotes).hasSize(databaseSizeBeforeCreate + 1);
        MemberNote testMemberNote = memberNotes.get(memberNotes.size() - 1);
        assertThat(testMemberNote.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testMemberNote.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkNotesIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberNoteRepository.findAll().size();
        // set the field null
        memberNote.setNotes(null);

        // Create the MemberNote, which fails.

        restMemberNoteMockMvc.perform(post("/api/memberNotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberNote)))
                .andExpect(status().isBadRequest());

        List<MemberNote> memberNotes = memberNoteRepository.findAll();
        assertThat(memberNotes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberNotes() throws Exception {
        // Initialize the database
        memberNoteRepository.saveAndFlush(memberNote);

        // Get all the memberNotes
        restMemberNoteMockMvc.perform(get("/api/memberNotes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberNote.getId().intValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getMemberNote() throws Exception {
        // Initialize the database
        memberNoteRepository.saveAndFlush(memberNote);

        // Get the memberNote
        restMemberNoteMockMvc.perform(get("/api/memberNotes/{id}", memberNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberNote.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMemberNote() throws Exception {
        // Get the memberNote
        restMemberNoteMockMvc.perform(get("/api/memberNotes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberNote() throws Exception {
        // Initialize the database
        memberNoteRepository.saveAndFlush(memberNote);

		int databaseSizeBeforeUpdate = memberNoteRepository.findAll().size();

        // Update the memberNote
        memberNote.setNotes(UPDATED_NOTES);
        memberNote.setDate(UPDATED_DATE);

        restMemberNoteMockMvc.perform(put("/api/memberNotes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberNote)))
                .andExpect(status().isOk());

        // Validate the MemberNote in the database
        List<MemberNote> memberNotes = memberNoteRepository.findAll();
        assertThat(memberNotes).hasSize(databaseSizeBeforeUpdate);
        MemberNote testMemberNote = memberNotes.get(memberNotes.size() - 1);
        assertThat(testMemberNote.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testMemberNote.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteMemberNote() throws Exception {
        // Initialize the database
        memberNoteRepository.saveAndFlush(memberNote);

		int databaseSizeBeforeDelete = memberNoteRepository.findAll().size();

        // Get the memberNote
        restMemberNoteMockMvc.perform(delete("/api/memberNotes/{id}", memberNote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MemberNote> memberNotes = memberNoteRepository.findAll();
        assertThat(memberNotes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
