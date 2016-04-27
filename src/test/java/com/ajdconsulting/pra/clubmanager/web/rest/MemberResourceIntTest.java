package com.ajdconsulting.pra.clubmanager.web.rest;

import com.ajdconsulting.pra.clubmanager.Application;
import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.repository.MemberRepository;

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
 * Test class for the MemberResource REST controller.
 *
 * @see MemberResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MemberResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";
    private static final String DEFAULT_ZIP = "AAAAA";
    private static final String UPDATED_ZIP = "BBBBB";
    private static final String DEFAULT_OCCUPATION = "AAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBBBB";

    private static final Boolean DEFAULT_VIEW_ONLINE = false;
    private static final Boolean UPDATED_VIEW_ONLINE = true;
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_JOINED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_JOINED = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberMockMvc;

    private Member member;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberResource memberResource = new MemberResource();
        ReflectionTestUtils.setField(memberResource, "memberRepository", memberRepository);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        member = new Member();
        member.setFirstName(DEFAULT_FIRST_NAME);
        member.setLastName(DEFAULT_LAST_NAME);
        member.setAddress(DEFAULT_ADDRESS);
        member.setCity(DEFAULT_CITY);
        member.setState(DEFAULT_STATE);
        member.setZip(DEFAULT_ZIP);
        member.setOccupation(DEFAULT_OCCUPATION);
        member.setPhone(DEFAULT_PHONE);
        member.setViewOnline(DEFAULT_VIEW_ONLINE);
        member.setEmail(DEFAULT_EMAIL);
        member.setBirthday(DEFAULT_BIRTHDAY);
        member.setDateJoined(DEFAULT_DATE_JOINED);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMember.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testMember.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testMember.getOccupation()).isEqualTo(DEFAULT_OCCUPATION);
        assertThat(testMember.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMember.getViewOnline()).isEqualTo(DEFAULT_VIEW_ONLINE);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testMember.getDateJoined()).isEqualTo(DEFAULT_DATE_JOINED);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setFirstName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setLastName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setAddress(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setCity(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setState(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setZip(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setPhone(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setEmail(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isBadRequest());

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the members
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
                .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].viewOnline").value(hasItem(DEFAULT_VIEW_ONLINE.booleanValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
                .andExpect(jsonPath("$.[*].dateJoined").value(hasItem(DEFAULT_DATE_JOINED.toString())));
    }

    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.viewOnline").value(DEFAULT_VIEW_ONLINE.booleanValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.dateJoined").value(DEFAULT_DATE_JOINED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

		int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        member.setFirstName(UPDATED_FIRST_NAME);
        member.setLastName(UPDATED_LAST_NAME);
        member.setAddress(UPDATED_ADDRESS);
        member.setCity(UPDATED_CITY);
        member.setState(UPDATED_STATE);
        member.setZip(UPDATED_ZIP);
        member.setOccupation(UPDATED_OCCUPATION);
        member.setPhone(UPDATED_PHONE);
        member.setViewOnline(UPDATED_VIEW_ONLINE);
        member.setEmail(UPDATED_EMAIL);
        member.setBirthday(UPDATED_BIRTHDAY);
        member.setDateJoined(UPDATED_DATE_JOINED);

        restMemberMockMvc.perform(put("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeUpdate);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMember.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testMember.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testMember.getOccupation()).isEqualTo(UPDATED_OCCUPATION);
        assertThat(testMember.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMember.getViewOnline()).isEqualTo(UPDATED_VIEW_ONLINE);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testMember.getDateJoined()).isEqualTo(UPDATED_DATE_JOINED);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

		int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Get the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeDelete - 1);
    }
}
