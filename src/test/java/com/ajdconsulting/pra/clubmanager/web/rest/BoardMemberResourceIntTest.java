package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.BoardMember;
import com.ajdconsulting.pra.clubmanager.repository.BoardMemberRepository;

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
 * Test class for the BoardMemberResource REST controller.
 *
 * @see BoardMemberResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BoardMemberResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_YEAR = 2016;
    private static final Integer UPDATED_YEAR = 2017;

    @Inject
    private BoardMemberRepository boardMemberRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBoardMemberMockMvc;

    private BoardMember boardMember;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoardMemberResource boardMemberResource = new BoardMemberResource();
        ReflectionTestUtils.setField(boardMemberResource, "boardMemberRepository", boardMemberRepository);
        this.restBoardMemberMockMvc = MockMvcBuilders.standaloneSetup(boardMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        boardMember = new BoardMember();
        boardMember.setTitle(DEFAULT_TITLE);
        boardMember.setYear(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createBoardMember() throws Exception {
        int databaseSizeBeforeCreate = boardMemberRepository.findAll().size();

        // Create the BoardMember

        restBoardMemberMockMvc.perform(post("/api/boardMembers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardMember)))
                .andExpect(status().isCreated());

        // Validate the BoardMember in the database
        List<BoardMember> boardMembers = boardMemberRepository.findAll();
        assertThat(boardMembers).hasSize(databaseSizeBeforeCreate + 1);
        BoardMember testBoardMember = boardMembers.get(boardMembers.size() - 1);
        assertThat(testBoardMember.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBoardMember.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardMemberRepository.findAll().size();
        // set the field null
        boardMember.setTitle(null);

        // Create the BoardMember, which fails.

        restBoardMemberMockMvc.perform(post("/api/boardMembers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardMember)))
                .andExpect(status().isBadRequest());

        List<BoardMember> boardMembers = boardMemberRepository.findAll();
        assertThat(boardMembers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardMemberRepository.findAll().size();
        // set the field null
        boardMember.setYear(null);

        // Create the BoardMember, which fails.

        restBoardMemberMockMvc.perform(post("/api/boardMembers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardMember)))
                .andExpect(status().isBadRequest());

        List<BoardMember> boardMembers = boardMemberRepository.findAll();
        assertThat(boardMembers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoardMembers() throws Exception {
        // Initialize the database
        boardMemberRepository.saveAndFlush(boardMember);

        // Get all the boardMembers
        restBoardMemberMockMvc.perform(get("/api/boardMembers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(boardMember.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getBoardMember() throws Exception {
        // Initialize the database
        boardMemberRepository.saveAndFlush(boardMember);

        // Get the boardMember
        restBoardMemberMockMvc.perform(get("/api/boardMembers/{id}", boardMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(boardMember.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingBoardMember() throws Exception {
        // Get the boardMember
        restBoardMemberMockMvc.perform(get("/api/boardMembers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardMember() throws Exception {
        // Initialize the database
        boardMemberRepository.saveAndFlush(boardMember);

		int databaseSizeBeforeUpdate = boardMemberRepository.findAll().size();

        // Update the boardMember
        boardMember.setTitle(UPDATED_TITLE);
        boardMember.setYear(UPDATED_YEAR);

        restBoardMemberMockMvc.perform(put("/api/boardMembers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardMember)))
                .andExpect(status().isOk());

        // Validate the BoardMember in the database
        List<BoardMember> boardMembers = boardMemberRepository.findAll();
        assertThat(boardMembers).hasSize(databaseSizeBeforeUpdate);
        BoardMember testBoardMember = boardMembers.get(boardMembers.size() - 1);
        assertThat(testBoardMember.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBoardMember.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deleteBoardMember() throws Exception {
        // Initialize the database
        boardMemberRepository.saveAndFlush(boardMember);

		int databaseSizeBeforeDelete = boardMemberRepository.findAll().size();

        // Get the boardMember
        restBoardMemberMockMvc.perform(delete("/api/boardMembers/{id}", boardMember.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BoardMember> boardMembers = boardMemberRepository.findAll();
        assertThat(boardMembers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
