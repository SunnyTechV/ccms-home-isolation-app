package com.tecgvg.ccms.hi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tecgvg.ccms.hi.IntegrationTest;
import com.tecgvg.ccms.hi.domain.HomeIsolations;
import com.tecgvg.ccms.hi.domain.IsolationsDetails;
import com.tecgvg.ccms.hi.repository.IsolationsDetailsRepository;
import com.tecgvg.ccms.hi.service.criteria.IsolationsDetailsCriteria;
import com.tecgvg.ccms.hi.service.dto.IsolationsDetailsDTO;
import com.tecgvg.ccms.hi.service.mapper.IsolationsDetailsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IsolationsDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IsolationsDetailsResourceIT {

    private static final Instant DEFAULT_ISOLATION_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISOLATION_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ISOLATION_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISOLATION_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REFERRED_DR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REFERRED_DR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REFERRED_DR_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_REFERRED_DR_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_PRESCRIPTION_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRIPTION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_URL = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SELF_REGISTERED = false;
    private static final Boolean UPDATED_SELF_REGISTERED = true;

    private static final Instant DEFAULT_LAST_ASSESSMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ASSESSMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/isolations-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsolationsDetailsRepository isolationsDetailsRepository;

    @Autowired
    private IsolationsDetailsMapper isolationsDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsolationsDetailsMockMvc;

    private IsolationsDetails isolationsDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsolationsDetails createEntity(EntityManager em) {
        IsolationsDetails isolationsDetails = new IsolationsDetails()
            .isolationStartDate(DEFAULT_ISOLATION_START_DATE)
            .isolationEndDate(DEFAULT_ISOLATION_END_DATE)
            .referredDrName(DEFAULT_REFERRED_DR_NAME)
            .referredDrMobile(DEFAULT_REFERRED_DR_MOBILE)
            .prescriptionUrl(DEFAULT_PRESCRIPTION_URL)
            .reportUrl(DEFAULT_REPORT_URL)
            .remarks(DEFAULT_REMARKS)
            .selfRegistered(DEFAULT_SELF_REGISTERED)
            .lastAssessment(DEFAULT_LAST_ASSESSMENT)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return isolationsDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsolationsDetails createUpdatedEntity(EntityManager em) {
        IsolationsDetails isolationsDetails = new IsolationsDetails()
            .isolationStartDate(UPDATED_ISOLATION_START_DATE)
            .isolationEndDate(UPDATED_ISOLATION_END_DATE)
            .referredDrName(UPDATED_REFERRED_DR_NAME)
            .referredDrMobile(UPDATED_REFERRED_DR_MOBILE)
            .prescriptionUrl(UPDATED_PRESCRIPTION_URL)
            .reportUrl(UPDATED_REPORT_URL)
            .remarks(UPDATED_REMARKS)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastAssessment(UPDATED_LAST_ASSESSMENT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return isolationsDetails;
    }

    @BeforeEach
    public void initTest() {
        isolationsDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createIsolationsDetails() throws Exception {
        int databaseSizeBeforeCreate = isolationsDetailsRepository.findAll().size();
        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);
        restIsolationsDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        IsolationsDetails testIsolationsDetails = isolationsDetailsList.get(isolationsDetailsList.size() - 1);
        assertThat(testIsolationsDetails.getIsolationStartDate()).isEqualTo(DEFAULT_ISOLATION_START_DATE);
        assertThat(testIsolationsDetails.getIsolationEndDate()).isEqualTo(DEFAULT_ISOLATION_END_DATE);
        assertThat(testIsolationsDetails.getReferredDrName()).isEqualTo(DEFAULT_REFERRED_DR_NAME);
        assertThat(testIsolationsDetails.getReferredDrMobile()).isEqualTo(DEFAULT_REFERRED_DR_MOBILE);
        assertThat(testIsolationsDetails.getPrescriptionUrl()).isEqualTo(DEFAULT_PRESCRIPTION_URL);
        assertThat(testIsolationsDetails.getReportUrl()).isEqualTo(DEFAULT_REPORT_URL);
        assertThat(testIsolationsDetails.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testIsolationsDetails.getSelfRegistered()).isEqualTo(DEFAULT_SELF_REGISTERED);
        assertThat(testIsolationsDetails.getLastAssessment()).isEqualTo(DEFAULT_LAST_ASSESSMENT);
        assertThat(testIsolationsDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testIsolationsDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createIsolationsDetailsWithExistingId() throws Exception {
        // Create the IsolationsDetails with an existing ID
        isolationsDetails.setId(1L);
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        int databaseSizeBeforeCreate = isolationsDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsolationsDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIsolationsDetails() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isolationsDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].isolationStartDate").value(hasItem(DEFAULT_ISOLATION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].isolationEndDate").value(hasItem(DEFAULT_ISOLATION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].referredDrName").value(hasItem(DEFAULT_REFERRED_DR_NAME)))
            .andExpect(jsonPath("$.[*].referredDrMobile").value(hasItem(DEFAULT_REFERRED_DR_MOBILE)))
            .andExpect(jsonPath("$.[*].prescriptionUrl").value(hasItem(DEFAULT_PRESCRIPTION_URL)))
            .andExpect(jsonPath("$.[*].reportUrl").value(hasItem(DEFAULT_REPORT_URL)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].selfRegistered").value(hasItem(DEFAULT_SELF_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].lastAssessment").value(hasItem(DEFAULT_LAST_ASSESSMENT.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getIsolationsDetails() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get the isolationsDetails
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, isolationsDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isolationsDetails.getId().intValue()))
            .andExpect(jsonPath("$.isolationStartDate").value(DEFAULT_ISOLATION_START_DATE.toString()))
            .andExpect(jsonPath("$.isolationEndDate").value(DEFAULT_ISOLATION_END_DATE.toString()))
            .andExpect(jsonPath("$.referredDrName").value(DEFAULT_REFERRED_DR_NAME))
            .andExpect(jsonPath("$.referredDrMobile").value(DEFAULT_REFERRED_DR_MOBILE))
            .andExpect(jsonPath("$.prescriptionUrl").value(DEFAULT_PRESCRIPTION_URL))
            .andExpect(jsonPath("$.reportUrl").value(DEFAULT_REPORT_URL))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.selfRegistered").value(DEFAULT_SELF_REGISTERED.booleanValue()))
            .andExpect(jsonPath("$.lastAssessment").value(DEFAULT_LAST_ASSESSMENT.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getIsolationsDetailsByIdFiltering() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        Long id = isolationsDetails.getId();

        defaultIsolationsDetailsShouldBeFound("id.equals=" + id);
        defaultIsolationsDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultIsolationsDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIsolationsDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultIsolationsDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIsolationsDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationStartDate equals to DEFAULT_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldBeFound("isolationStartDate.equals=" + DEFAULT_ISOLATION_START_DATE);

        // Get all the isolationsDetailsList where isolationStartDate equals to UPDATED_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationStartDate.equals=" + UPDATED_ISOLATION_START_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationStartDate not equals to DEFAULT_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationStartDate.notEquals=" + DEFAULT_ISOLATION_START_DATE);

        // Get all the isolationsDetailsList where isolationStartDate not equals to UPDATED_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldBeFound("isolationStartDate.notEquals=" + UPDATED_ISOLATION_START_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationStartDate in DEFAULT_ISOLATION_START_DATE or UPDATED_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldBeFound("isolationStartDate.in=" + DEFAULT_ISOLATION_START_DATE + "," + UPDATED_ISOLATION_START_DATE);

        // Get all the isolationsDetailsList where isolationStartDate equals to UPDATED_ISOLATION_START_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationStartDate.in=" + UPDATED_ISOLATION_START_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationStartDate is not null
        defaultIsolationsDetailsShouldBeFound("isolationStartDate.specified=true");

        // Get all the isolationsDetailsList where isolationStartDate is null
        defaultIsolationsDetailsShouldNotBeFound("isolationStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationEndDate equals to DEFAULT_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldBeFound("isolationEndDate.equals=" + DEFAULT_ISOLATION_END_DATE);

        // Get all the isolationsDetailsList where isolationEndDate equals to UPDATED_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationEndDate.equals=" + UPDATED_ISOLATION_END_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationEndDate not equals to DEFAULT_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationEndDate.notEquals=" + DEFAULT_ISOLATION_END_DATE);

        // Get all the isolationsDetailsList where isolationEndDate not equals to UPDATED_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldBeFound("isolationEndDate.notEquals=" + UPDATED_ISOLATION_END_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationEndDate in DEFAULT_ISOLATION_END_DATE or UPDATED_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldBeFound("isolationEndDate.in=" + DEFAULT_ISOLATION_END_DATE + "," + UPDATED_ISOLATION_END_DATE);

        // Get all the isolationsDetailsList where isolationEndDate equals to UPDATED_ISOLATION_END_DATE
        defaultIsolationsDetailsShouldNotBeFound("isolationEndDate.in=" + UPDATED_ISOLATION_END_DATE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByIsolationEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where isolationEndDate is not null
        defaultIsolationsDetailsShouldBeFound("isolationEndDate.specified=true");

        // Get all the isolationsDetailsList where isolationEndDate is null
        defaultIsolationsDetailsShouldNotBeFound("isolationEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName equals to DEFAULT_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldBeFound("referredDrName.equals=" + DEFAULT_REFERRED_DR_NAME);

        // Get all the isolationsDetailsList where referredDrName equals to UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.equals=" + UPDATED_REFERRED_DR_NAME);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName not equals to DEFAULT_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.notEquals=" + DEFAULT_REFERRED_DR_NAME);

        // Get all the isolationsDetailsList where referredDrName not equals to UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldBeFound("referredDrName.notEquals=" + UPDATED_REFERRED_DR_NAME);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName in DEFAULT_REFERRED_DR_NAME or UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldBeFound("referredDrName.in=" + DEFAULT_REFERRED_DR_NAME + "," + UPDATED_REFERRED_DR_NAME);

        // Get all the isolationsDetailsList where referredDrName equals to UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.in=" + UPDATED_REFERRED_DR_NAME);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName is not null
        defaultIsolationsDetailsShouldBeFound("referredDrName.specified=true");

        // Get all the isolationsDetailsList where referredDrName is null
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName contains DEFAULT_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldBeFound("referredDrName.contains=" + DEFAULT_REFERRED_DR_NAME);

        // Get all the isolationsDetailsList where referredDrName contains UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.contains=" + UPDATED_REFERRED_DR_NAME);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrNameNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrName does not contain DEFAULT_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldNotBeFound("referredDrName.doesNotContain=" + DEFAULT_REFERRED_DR_NAME);

        // Get all the isolationsDetailsList where referredDrName does not contain UPDATED_REFERRED_DR_NAME
        defaultIsolationsDetailsShouldBeFound("referredDrName.doesNotContain=" + UPDATED_REFERRED_DR_NAME);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile equals to DEFAULT_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.equals=" + DEFAULT_REFERRED_DR_MOBILE);

        // Get all the isolationsDetailsList where referredDrMobile equals to UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.equals=" + UPDATED_REFERRED_DR_MOBILE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile not equals to DEFAULT_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.notEquals=" + DEFAULT_REFERRED_DR_MOBILE);

        // Get all the isolationsDetailsList where referredDrMobile not equals to UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.notEquals=" + UPDATED_REFERRED_DR_MOBILE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile in DEFAULT_REFERRED_DR_MOBILE or UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.in=" + DEFAULT_REFERRED_DR_MOBILE + "," + UPDATED_REFERRED_DR_MOBILE);

        // Get all the isolationsDetailsList where referredDrMobile equals to UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.in=" + UPDATED_REFERRED_DR_MOBILE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile is not null
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.specified=true");

        // Get all the isolationsDetailsList where referredDrMobile is null
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile contains DEFAULT_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.contains=" + DEFAULT_REFERRED_DR_MOBILE);

        // Get all the isolationsDetailsList where referredDrMobile contains UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.contains=" + UPDATED_REFERRED_DR_MOBILE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReferredDrMobileNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where referredDrMobile does not contain DEFAULT_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldNotBeFound("referredDrMobile.doesNotContain=" + DEFAULT_REFERRED_DR_MOBILE);

        // Get all the isolationsDetailsList where referredDrMobile does not contain UPDATED_REFERRED_DR_MOBILE
        defaultIsolationsDetailsShouldBeFound("referredDrMobile.doesNotContain=" + UPDATED_REFERRED_DR_MOBILE);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl equals to DEFAULT_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.equals=" + DEFAULT_PRESCRIPTION_URL);

        // Get all the isolationsDetailsList where prescriptionUrl equals to UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.equals=" + UPDATED_PRESCRIPTION_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl not equals to DEFAULT_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.notEquals=" + DEFAULT_PRESCRIPTION_URL);

        // Get all the isolationsDetailsList where prescriptionUrl not equals to UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.notEquals=" + UPDATED_PRESCRIPTION_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl in DEFAULT_PRESCRIPTION_URL or UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.in=" + DEFAULT_PRESCRIPTION_URL + "," + UPDATED_PRESCRIPTION_URL);

        // Get all the isolationsDetailsList where prescriptionUrl equals to UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.in=" + UPDATED_PRESCRIPTION_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl is not null
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.specified=true");

        // Get all the isolationsDetailsList where prescriptionUrl is null
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl contains DEFAULT_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.contains=" + DEFAULT_PRESCRIPTION_URL);

        // Get all the isolationsDetailsList where prescriptionUrl contains UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.contains=" + UPDATED_PRESCRIPTION_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByPrescriptionUrlNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where prescriptionUrl does not contain DEFAULT_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldNotBeFound("prescriptionUrl.doesNotContain=" + DEFAULT_PRESCRIPTION_URL);

        // Get all the isolationsDetailsList where prescriptionUrl does not contain UPDATED_PRESCRIPTION_URL
        defaultIsolationsDetailsShouldBeFound("prescriptionUrl.doesNotContain=" + UPDATED_PRESCRIPTION_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl equals to DEFAULT_REPORT_URL
        defaultIsolationsDetailsShouldBeFound("reportUrl.equals=" + DEFAULT_REPORT_URL);

        // Get all the isolationsDetailsList where reportUrl equals to UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.equals=" + UPDATED_REPORT_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl not equals to DEFAULT_REPORT_URL
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.notEquals=" + DEFAULT_REPORT_URL);

        // Get all the isolationsDetailsList where reportUrl not equals to UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldBeFound("reportUrl.notEquals=" + UPDATED_REPORT_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl in DEFAULT_REPORT_URL or UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldBeFound("reportUrl.in=" + DEFAULT_REPORT_URL + "," + UPDATED_REPORT_URL);

        // Get all the isolationsDetailsList where reportUrl equals to UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.in=" + UPDATED_REPORT_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl is not null
        defaultIsolationsDetailsShouldBeFound("reportUrl.specified=true");

        // Get all the isolationsDetailsList where reportUrl is null
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl contains DEFAULT_REPORT_URL
        defaultIsolationsDetailsShouldBeFound("reportUrl.contains=" + DEFAULT_REPORT_URL);

        // Get all the isolationsDetailsList where reportUrl contains UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.contains=" + UPDATED_REPORT_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByReportUrlNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where reportUrl does not contain DEFAULT_REPORT_URL
        defaultIsolationsDetailsShouldNotBeFound("reportUrl.doesNotContain=" + DEFAULT_REPORT_URL);

        // Get all the isolationsDetailsList where reportUrl does not contain UPDATED_REPORT_URL
        defaultIsolationsDetailsShouldBeFound("reportUrl.doesNotContain=" + UPDATED_REPORT_URL);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks equals to DEFAULT_REMARKS
        defaultIsolationsDetailsShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the isolationsDetailsList where remarks equals to UPDATED_REMARKS
        defaultIsolationsDetailsShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks not equals to DEFAULT_REMARKS
        defaultIsolationsDetailsShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the isolationsDetailsList where remarks not equals to UPDATED_REMARKS
        defaultIsolationsDetailsShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultIsolationsDetailsShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the isolationsDetailsList where remarks equals to UPDATED_REMARKS
        defaultIsolationsDetailsShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks is not null
        defaultIsolationsDetailsShouldBeFound("remarks.specified=true");

        // Get all the isolationsDetailsList where remarks is null
        defaultIsolationsDetailsShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks contains DEFAULT_REMARKS
        defaultIsolationsDetailsShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the isolationsDetailsList where remarks contains UPDATED_REMARKS
        defaultIsolationsDetailsShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where remarks does not contain DEFAULT_REMARKS
        defaultIsolationsDetailsShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the isolationsDetailsList where remarks does not contain UPDATED_REMARKS
        defaultIsolationsDetailsShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsBySelfRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where selfRegistered equals to DEFAULT_SELF_REGISTERED
        defaultIsolationsDetailsShouldBeFound("selfRegistered.equals=" + DEFAULT_SELF_REGISTERED);

        // Get all the isolationsDetailsList where selfRegistered equals to UPDATED_SELF_REGISTERED
        defaultIsolationsDetailsShouldNotBeFound("selfRegistered.equals=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsBySelfRegisteredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where selfRegistered not equals to DEFAULT_SELF_REGISTERED
        defaultIsolationsDetailsShouldNotBeFound("selfRegistered.notEquals=" + DEFAULT_SELF_REGISTERED);

        // Get all the isolationsDetailsList where selfRegistered not equals to UPDATED_SELF_REGISTERED
        defaultIsolationsDetailsShouldBeFound("selfRegistered.notEquals=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsBySelfRegisteredIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where selfRegistered in DEFAULT_SELF_REGISTERED or UPDATED_SELF_REGISTERED
        defaultIsolationsDetailsShouldBeFound("selfRegistered.in=" + DEFAULT_SELF_REGISTERED + "," + UPDATED_SELF_REGISTERED);

        // Get all the isolationsDetailsList where selfRegistered equals to UPDATED_SELF_REGISTERED
        defaultIsolationsDetailsShouldNotBeFound("selfRegistered.in=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsBySelfRegisteredIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where selfRegistered is not null
        defaultIsolationsDetailsShouldBeFound("selfRegistered.specified=true");

        // Get all the isolationsDetailsList where selfRegistered is null
        defaultIsolationsDetailsShouldNotBeFound("selfRegistered.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastAssessmentIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastAssessment equals to DEFAULT_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldBeFound("lastAssessment.equals=" + DEFAULT_LAST_ASSESSMENT);

        // Get all the isolationsDetailsList where lastAssessment equals to UPDATED_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldNotBeFound("lastAssessment.equals=" + UPDATED_LAST_ASSESSMENT);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastAssessmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastAssessment not equals to DEFAULT_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldNotBeFound("lastAssessment.notEquals=" + DEFAULT_LAST_ASSESSMENT);

        // Get all the isolationsDetailsList where lastAssessment not equals to UPDATED_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldBeFound("lastAssessment.notEquals=" + UPDATED_LAST_ASSESSMENT);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastAssessmentIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastAssessment in DEFAULT_LAST_ASSESSMENT or UPDATED_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldBeFound("lastAssessment.in=" + DEFAULT_LAST_ASSESSMENT + "," + UPDATED_LAST_ASSESSMENT);

        // Get all the isolationsDetailsList where lastAssessment equals to UPDATED_LAST_ASSESSMENT
        defaultIsolationsDetailsShouldNotBeFound("lastAssessment.in=" + UPDATED_LAST_ASSESSMENT);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastAssessmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastAssessment is not null
        defaultIsolationsDetailsShouldBeFound("lastAssessment.specified=true");

        // Get all the isolationsDetailsList where lastAssessment is null
        defaultIsolationsDetailsShouldNotBeFound("lastAssessment.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultIsolationsDetailsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the isolationsDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultIsolationsDetailsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultIsolationsDetailsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the isolationsDetailsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultIsolationsDetailsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultIsolationsDetailsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the isolationsDetailsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultIsolationsDetailsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModified is not null
        defaultIsolationsDetailsShouldBeFound("lastModified.specified=true");

        // Get all the isolationsDetailsList where lastModified is null
        defaultIsolationsDetailsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the isolationsDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the isolationsDetailsList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the isolationsDetailsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy is not null
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the isolationsDetailsList where lastModifiedBy is null
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the isolationsDetailsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        // Get all the isolationsDetailsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the isolationsDetailsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultIsolationsDetailsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllIsolationsDetailsByHomeIsolationsIsEqualToSomething() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);
        HomeIsolations homeIsolations;
        if (TestUtil.findAll(em, HomeIsolations.class).isEmpty()) {
            homeIsolations = HomeIsolationsResourceIT.createEntity(em);
            em.persist(homeIsolations);
            em.flush();
        } else {
            homeIsolations = TestUtil.findAll(em, HomeIsolations.class).get(0);
        }
        em.persist(homeIsolations);
        em.flush();
        isolationsDetails.setHomeIsolations(homeIsolations);
        homeIsolations.setIsolationsDetails(isolationsDetails);
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);
        Long homeIsolationsId = homeIsolations.getId();

        // Get all the isolationsDetailsList where homeIsolations equals to homeIsolationsId
        defaultIsolationsDetailsShouldBeFound("homeIsolationsId.equals=" + homeIsolationsId);

        // Get all the isolationsDetailsList where homeIsolations equals to (homeIsolationsId + 1)
        defaultIsolationsDetailsShouldNotBeFound("homeIsolationsId.equals=" + (homeIsolationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIsolationsDetailsShouldBeFound(String filter) throws Exception {
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isolationsDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].isolationStartDate").value(hasItem(DEFAULT_ISOLATION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].isolationEndDate").value(hasItem(DEFAULT_ISOLATION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].referredDrName").value(hasItem(DEFAULT_REFERRED_DR_NAME)))
            .andExpect(jsonPath("$.[*].referredDrMobile").value(hasItem(DEFAULT_REFERRED_DR_MOBILE)))
            .andExpect(jsonPath("$.[*].prescriptionUrl").value(hasItem(DEFAULT_PRESCRIPTION_URL)))
            .andExpect(jsonPath("$.[*].reportUrl").value(hasItem(DEFAULT_REPORT_URL)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].selfRegistered").value(hasItem(DEFAULT_SELF_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].lastAssessment").value(hasItem(DEFAULT_LAST_ASSESSMENT.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIsolationsDetailsShouldNotBeFound(String filter) throws Exception {
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIsolationsDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIsolationsDetails() throws Exception {
        // Get the isolationsDetails
        restIsolationsDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIsolationsDetails() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();

        // Update the isolationsDetails
        IsolationsDetails updatedIsolationsDetails = isolationsDetailsRepository.findById(isolationsDetails.getId()).get();
        // Disconnect from session so that the updates on updatedIsolationsDetails are not directly saved in db
        em.detach(updatedIsolationsDetails);
        updatedIsolationsDetails
            .isolationStartDate(UPDATED_ISOLATION_START_DATE)
            .isolationEndDate(UPDATED_ISOLATION_END_DATE)
            .referredDrName(UPDATED_REFERRED_DR_NAME)
            .referredDrMobile(UPDATED_REFERRED_DR_MOBILE)
            .prescriptionUrl(UPDATED_PRESCRIPTION_URL)
            .reportUrl(UPDATED_REPORT_URL)
            .remarks(UPDATED_REMARKS)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastAssessment(UPDATED_LAST_ASSESSMENT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(updatedIsolationsDetails);

        restIsolationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isolationsDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        IsolationsDetails testIsolationsDetails = isolationsDetailsList.get(isolationsDetailsList.size() - 1);
        assertThat(testIsolationsDetails.getIsolationStartDate()).isEqualTo(UPDATED_ISOLATION_START_DATE);
        assertThat(testIsolationsDetails.getIsolationEndDate()).isEqualTo(UPDATED_ISOLATION_END_DATE);
        assertThat(testIsolationsDetails.getReferredDrName()).isEqualTo(UPDATED_REFERRED_DR_NAME);
        assertThat(testIsolationsDetails.getReferredDrMobile()).isEqualTo(UPDATED_REFERRED_DR_MOBILE);
        assertThat(testIsolationsDetails.getPrescriptionUrl()).isEqualTo(UPDATED_PRESCRIPTION_URL);
        assertThat(testIsolationsDetails.getReportUrl()).isEqualTo(UPDATED_REPORT_URL);
        assertThat(testIsolationsDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testIsolationsDetails.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testIsolationsDetails.getLastAssessment()).isEqualTo(UPDATED_LAST_ASSESSMENT);
        assertThat(testIsolationsDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIsolationsDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isolationsDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIsolationsDetailsWithPatch() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();

        // Update the isolationsDetails using partial update
        IsolationsDetails partialUpdatedIsolationsDetails = new IsolationsDetails();
        partialUpdatedIsolationsDetails.setId(isolationsDetails.getId());

        partialUpdatedIsolationsDetails
            .isolationEndDate(UPDATED_ISOLATION_END_DATE)
            .referredDrName(UPDATED_REFERRED_DR_NAME)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restIsolationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsolationsDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsolationsDetails))
            )
            .andExpect(status().isOk());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        IsolationsDetails testIsolationsDetails = isolationsDetailsList.get(isolationsDetailsList.size() - 1);
        assertThat(testIsolationsDetails.getIsolationStartDate()).isEqualTo(DEFAULT_ISOLATION_START_DATE);
        assertThat(testIsolationsDetails.getIsolationEndDate()).isEqualTo(UPDATED_ISOLATION_END_DATE);
        assertThat(testIsolationsDetails.getReferredDrName()).isEqualTo(UPDATED_REFERRED_DR_NAME);
        assertThat(testIsolationsDetails.getReferredDrMobile()).isEqualTo(DEFAULT_REFERRED_DR_MOBILE);
        assertThat(testIsolationsDetails.getPrescriptionUrl()).isEqualTo(DEFAULT_PRESCRIPTION_URL);
        assertThat(testIsolationsDetails.getReportUrl()).isEqualTo(DEFAULT_REPORT_URL);
        assertThat(testIsolationsDetails.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testIsolationsDetails.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testIsolationsDetails.getLastAssessment()).isEqualTo(DEFAULT_LAST_ASSESSMENT);
        assertThat(testIsolationsDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIsolationsDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateIsolationsDetailsWithPatch() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();

        // Update the isolationsDetails using partial update
        IsolationsDetails partialUpdatedIsolationsDetails = new IsolationsDetails();
        partialUpdatedIsolationsDetails.setId(isolationsDetails.getId());

        partialUpdatedIsolationsDetails
            .isolationStartDate(UPDATED_ISOLATION_START_DATE)
            .isolationEndDate(UPDATED_ISOLATION_END_DATE)
            .referredDrName(UPDATED_REFERRED_DR_NAME)
            .referredDrMobile(UPDATED_REFERRED_DR_MOBILE)
            .prescriptionUrl(UPDATED_PRESCRIPTION_URL)
            .reportUrl(UPDATED_REPORT_URL)
            .remarks(UPDATED_REMARKS)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastAssessment(UPDATED_LAST_ASSESSMENT)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restIsolationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsolationsDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsolationsDetails))
            )
            .andExpect(status().isOk());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
        IsolationsDetails testIsolationsDetails = isolationsDetailsList.get(isolationsDetailsList.size() - 1);
        assertThat(testIsolationsDetails.getIsolationStartDate()).isEqualTo(UPDATED_ISOLATION_START_DATE);
        assertThat(testIsolationsDetails.getIsolationEndDate()).isEqualTo(UPDATED_ISOLATION_END_DATE);
        assertThat(testIsolationsDetails.getReferredDrName()).isEqualTo(UPDATED_REFERRED_DR_NAME);
        assertThat(testIsolationsDetails.getReferredDrMobile()).isEqualTo(UPDATED_REFERRED_DR_MOBILE);
        assertThat(testIsolationsDetails.getPrescriptionUrl()).isEqualTo(UPDATED_PRESCRIPTION_URL);
        assertThat(testIsolationsDetails.getReportUrl()).isEqualTo(UPDATED_REPORT_URL);
        assertThat(testIsolationsDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testIsolationsDetails.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testIsolationsDetails.getLastAssessment()).isEqualTo(UPDATED_LAST_ASSESSMENT);
        assertThat(testIsolationsDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testIsolationsDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isolationsDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsolationsDetails() throws Exception {
        int databaseSizeBeforeUpdate = isolationsDetailsRepository.findAll().size();
        isolationsDetails.setId(count.incrementAndGet());

        // Create the IsolationsDetails
        IsolationsDetailsDTO isolationsDetailsDTO = isolationsDetailsMapper.toDto(isolationsDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsolationsDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isolationsDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsolationsDetails in the database
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIsolationsDetails() throws Exception {
        // Initialize the database
        isolationsDetailsRepository.saveAndFlush(isolationsDetails);

        int databaseSizeBeforeDelete = isolationsDetailsRepository.findAll().size();

        // Delete the isolationsDetails
        restIsolationsDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, isolationsDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IsolationsDetails> isolationsDetailsList = isolationsDetailsRepository.findAll();
        assertThat(isolationsDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
