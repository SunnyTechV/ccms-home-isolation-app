package com.tecgvg.ccms.hi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tecgvg.ccms.hi.IntegrationTest;
import com.tecgvg.ccms.hi.domain.Assessment;
import com.tecgvg.ccms.hi.domain.HomeIsolations;
import com.tecgvg.ccms.hi.domain.IsolationsDetails;
import com.tecgvg.ccms.hi.domain.enumeration.HealthCondition;
import com.tecgvg.ccms.hi.domain.enumeration.IsolationStatus;
import com.tecgvg.ccms.hi.repository.HomeIsolationsRepository;
import com.tecgvg.ccms.hi.service.criteria.HomeIsolationsCriteria;
import com.tecgvg.ccms.hi.service.dto.HomeIsolationsDTO;
import com.tecgvg.ccms.hi.service.mapper.HomeIsolationsMapper;
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
 * Integration tests for the {@link HomeIsolationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HomeIsolationsResourceIT {

    private static final String DEFAULT_ICMR_ID = "AAAAAAAAAA";
    private static final String UPDATED_ICMR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_AADHAR_CARD_NO = "AAAAAAAAAA";
    private static final String UPDATED_AADHAR_CARD_NO = "BBBBBBBBBB";

    private static final IsolationStatus DEFAULT_STATUS = IsolationStatus.HOMEISOLATION;
    private static final IsolationStatus UPDATED_STATUS = IsolationStatus.HOSPITALISED;

    private static final String DEFAULT_AGE = "AAAAAAAAAA";
    private static final String UPDATED_AGE = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final Long DEFAULT_STATE_ID = 1L;
    private static final Long UPDATED_STATE_ID = 2L;
    private static final Long SMALLER_STATE_ID = 1L - 1L;

    private static final Long DEFAULT_DISTRICT_ID = 1L;
    private static final Long UPDATED_DISTRICT_ID = 2L;
    private static final Long SMALLER_DISTRICT_ID = 1L - 1L;

    private static final Long DEFAULT_TALUKA_ID = 1L;
    private static final Long UPDATED_TALUKA_ID = 2L;
    private static final Long SMALLER_TALUKA_ID = 1L - 1L;

    private static final Long DEFAULT_CITY_ID = 1L;
    private static final Long UPDATED_CITY_ID = 2L;
    private static final Long SMALLER_CITY_ID = 1L - 1L;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_COLLECTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COLLECTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_HOSPITALIZED = false;
    private static final Boolean UPDATED_HOSPITALIZED = true;

    private static final Long DEFAULT_HOSPITAL_ID = 1L;
    private static final Long UPDATED_HOSPITAL_ID = 2L;
    private static final Long SMALLER_HOSPITAL_ID = 1L - 1L;

    private static final String DEFAULT_ADDRESS_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_LONGITUDE = "BBBBBBBBBB";

    private static final Instant DEFAULT_HOSPITALIZATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HOSPITALIZATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final HealthCondition DEFAULT_HEALTH_CONDITION = HealthCondition.STABLE;
    private static final HealthCondition UPDATED_HEALTH_CONDITION = HealthCondition.CRITICAL;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Long DEFAULT_CCMS_USER_ID = 1L;
    private static final Long UPDATED_CCMS_USER_ID = 2L;
    private static final Long SMALLER_CCMS_USER_ID = 1L - 1L;

    private static final Boolean DEFAULT_SELF_REGISTERED = false;
    private static final Boolean UPDATED_SELF_REGISTERED = true;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/home-isolations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HomeIsolationsRepository homeIsolationsRepository;

    @Autowired
    private HomeIsolationsMapper homeIsolationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHomeIsolationsMockMvc;

    private HomeIsolations homeIsolations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomeIsolations createEntity(EntityManager em) {
        HomeIsolations homeIsolations = new HomeIsolations()
            .icmrId(DEFAULT_ICMR_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .email(DEFAULT_EMAIL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .activated(DEFAULT_ACTIVATED)
            .mobileNo(DEFAULT_MOBILE_NO)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .secondaryContactNo(DEFAULT_SECONDARY_CONTACT_NO)
            .aadharCardNo(DEFAULT_AADHAR_CARD_NO)
            .status(DEFAULT_STATUS)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .stateId(DEFAULT_STATE_ID)
            .districtId(DEFAULT_DISTRICT_ID)
            .talukaId(DEFAULT_TALUKA_ID)
            .cityId(DEFAULT_CITY_ID)
            .address(DEFAULT_ADDRESS)
            .pincode(DEFAULT_PINCODE)
            .collectionDate(DEFAULT_COLLECTION_DATE)
            .hospitalized(DEFAULT_HOSPITALIZED)
            .hospitalId(DEFAULT_HOSPITAL_ID)
            .addressLatitude(DEFAULT_ADDRESS_LATITUDE)
            .addressLongitude(DEFAULT_ADDRESS_LONGITUDE)
            .currentLatitude(DEFAULT_CURRENT_LATITUDE)
            .currentLongitude(DEFAULT_CURRENT_LONGITUDE)
            .hospitalizationDate(DEFAULT_HOSPITALIZATION_DATE)
            .healthCondition(DEFAULT_HEALTH_CONDITION)
            .remarks(DEFAULT_REMARKS)
            .ccmsUserId(DEFAULT_CCMS_USER_ID)
            .selfRegistered(DEFAULT_SELF_REGISTERED)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return homeIsolations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomeIsolations createUpdatedEntity(EntityManager em) {
        HomeIsolations homeIsolations = new HomeIsolations()
            .icmrId(UPDATED_ICMR_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .secondaryContactNo(UPDATED_SECONDARY_CONTACT_NO)
            .aadharCardNo(UPDATED_AADHAR_CARD_NO)
            .status(UPDATED_STATUS)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .stateId(UPDATED_STATE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .talukaId(UPDATED_TALUKA_ID)
            .cityId(UPDATED_CITY_ID)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .collectionDate(UPDATED_COLLECTION_DATE)
            .hospitalized(UPDATED_HOSPITALIZED)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .addressLatitude(UPDATED_ADDRESS_LATITUDE)
            .addressLongitude(UPDATED_ADDRESS_LONGITUDE)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .hospitalizationDate(UPDATED_HOSPITALIZATION_DATE)
            .healthCondition(UPDATED_HEALTH_CONDITION)
            .remarks(UPDATED_REMARKS)
            .ccmsUserId(UPDATED_CCMS_USER_ID)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return homeIsolations;
    }

    @BeforeEach
    public void initTest() {
        homeIsolations = createEntity(em);
    }

    @Test
    @Transactional
    void createHomeIsolations() throws Exception {
        int databaseSizeBeforeCreate = homeIsolationsRepository.findAll().size();
        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);
        restHomeIsolationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeCreate + 1);
        HomeIsolations testHomeIsolations = homeIsolationsList.get(homeIsolationsList.size() - 1);
        assertThat(testHomeIsolations.getIcmrId()).isEqualTo(DEFAULT_ICMR_ID);
        assertThat(testHomeIsolations.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testHomeIsolations.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testHomeIsolations.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testHomeIsolations.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHomeIsolations.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testHomeIsolations.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testHomeIsolations.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testHomeIsolations.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testHomeIsolations.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
        assertThat(testHomeIsolations.getSecondaryContactNo()).isEqualTo(DEFAULT_SECONDARY_CONTACT_NO);
        assertThat(testHomeIsolations.getAadharCardNo()).isEqualTo(DEFAULT_AADHAR_CARD_NO);
        assertThat(testHomeIsolations.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHomeIsolations.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testHomeIsolations.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testHomeIsolations.getStateId()).isEqualTo(DEFAULT_STATE_ID);
        assertThat(testHomeIsolations.getDistrictId()).isEqualTo(DEFAULT_DISTRICT_ID);
        assertThat(testHomeIsolations.getTalukaId()).isEqualTo(DEFAULT_TALUKA_ID);
        assertThat(testHomeIsolations.getCityId()).isEqualTo(DEFAULT_CITY_ID);
        assertThat(testHomeIsolations.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHomeIsolations.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testHomeIsolations.getCollectionDate()).isEqualTo(DEFAULT_COLLECTION_DATE);
        assertThat(testHomeIsolations.getHospitalized()).isEqualTo(DEFAULT_HOSPITALIZED);
        assertThat(testHomeIsolations.getHospitalId()).isEqualTo(DEFAULT_HOSPITAL_ID);
        assertThat(testHomeIsolations.getAddressLatitude()).isEqualTo(DEFAULT_ADDRESS_LATITUDE);
        assertThat(testHomeIsolations.getAddressLongitude()).isEqualTo(DEFAULT_ADDRESS_LONGITUDE);
        assertThat(testHomeIsolations.getCurrentLatitude()).isEqualTo(DEFAULT_CURRENT_LATITUDE);
        assertThat(testHomeIsolations.getCurrentLongitude()).isEqualTo(DEFAULT_CURRENT_LONGITUDE);
        assertThat(testHomeIsolations.getHospitalizationDate()).isEqualTo(DEFAULT_HOSPITALIZATION_DATE);
        assertThat(testHomeIsolations.getHealthCondition()).isEqualTo(DEFAULT_HEALTH_CONDITION);
        assertThat(testHomeIsolations.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testHomeIsolations.getCcmsUserId()).isEqualTo(DEFAULT_CCMS_USER_ID);
        assertThat(testHomeIsolations.getSelfRegistered()).isEqualTo(DEFAULT_SELF_REGISTERED);
        assertThat(testHomeIsolations.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testHomeIsolations.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createHomeIsolationsWithExistingId() throws Exception {
        // Create the HomeIsolations with an existing ID
        homeIsolations.setId(1L);
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        int databaseSizeBeforeCreate = homeIsolationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomeIsolationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMobileNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeIsolationsRepository.findAll().size();
        // set the field null
        homeIsolations.setMobileNo(null);

        // Create the HomeIsolations, which fails.
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        restHomeIsolationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeIsolationsRepository.findAll().size();
        // set the field null
        homeIsolations.setPasswordHash(null);

        // Create the HomeIsolations, which fails.
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        restHomeIsolationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAadharCardNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeIsolationsRepository.findAll().size();
        // set the field null
        homeIsolations.setAadharCardNo(null);

        // Create the HomeIsolations, which fails.
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        restHomeIsolationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHomeIsolations() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeIsolations.getId().intValue())))
            .andExpect(jsonPath("$.[*].icmrId").value(hasItem(DEFAULT_ICMR_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].secondaryContactNo").value(hasItem(DEFAULT_SECONDARY_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].aadharCardNo").value(hasItem(DEFAULT_AADHAR_CARD_NO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].stateId").value(hasItem(DEFAULT_STATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID.intValue())))
            .andExpect(jsonPath("$.[*].talukaId").value(hasItem(DEFAULT_TALUKA_ID.intValue())))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].collectionDate").value(hasItem(DEFAULT_COLLECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].hospitalized").value(hasItem(DEFAULT_HOSPITALIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].hospitalId").value(hasItem(DEFAULT_HOSPITAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].addressLatitude").value(hasItem(DEFAULT_ADDRESS_LATITUDE)))
            .andExpect(jsonPath("$.[*].addressLongitude").value(hasItem(DEFAULT_ADDRESS_LONGITUDE)))
            .andExpect(jsonPath("$.[*].currentLatitude").value(hasItem(DEFAULT_CURRENT_LATITUDE)))
            .andExpect(jsonPath("$.[*].currentLongitude").value(hasItem(DEFAULT_CURRENT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].hospitalizationDate").value(hasItem(DEFAULT_HOSPITALIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].healthCondition").value(hasItem(DEFAULT_HEALTH_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].ccmsUserId").value(hasItem(DEFAULT_CCMS_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].selfRegistered").value(hasItem(DEFAULT_SELF_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getHomeIsolations() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get the homeIsolations
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL_ID, homeIsolations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(homeIsolations.getId().intValue()))
            .andExpect(jsonPath("$.icmrId").value(DEFAULT_ICMR_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.secondaryContactNo").value(DEFAULT_SECONDARY_CONTACT_NO))
            .andExpect(jsonPath("$.aadharCardNo").value(DEFAULT_AADHAR_CARD_NO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.stateId").value(DEFAULT_STATE_ID.intValue()))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID.intValue()))
            .andExpect(jsonPath("$.talukaId").value(DEFAULT_TALUKA_ID.intValue()))
            .andExpect(jsonPath("$.cityId").value(DEFAULT_CITY_ID.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.collectionDate").value(DEFAULT_COLLECTION_DATE.toString()))
            .andExpect(jsonPath("$.hospitalized").value(DEFAULT_HOSPITALIZED.booleanValue()))
            .andExpect(jsonPath("$.hospitalId").value(DEFAULT_HOSPITAL_ID.intValue()))
            .andExpect(jsonPath("$.addressLatitude").value(DEFAULT_ADDRESS_LATITUDE))
            .andExpect(jsonPath("$.addressLongitude").value(DEFAULT_ADDRESS_LONGITUDE))
            .andExpect(jsonPath("$.currentLatitude").value(DEFAULT_CURRENT_LATITUDE))
            .andExpect(jsonPath("$.currentLongitude").value(DEFAULT_CURRENT_LONGITUDE))
            .andExpect(jsonPath("$.hospitalizationDate").value(DEFAULT_HOSPITALIZATION_DATE.toString()))
            .andExpect(jsonPath("$.healthCondition").value(DEFAULT_HEALTH_CONDITION.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.ccmsUserId").value(DEFAULT_CCMS_USER_ID.intValue()))
            .andExpect(jsonPath("$.selfRegistered").value(DEFAULT_SELF_REGISTERED.booleanValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getHomeIsolationsByIdFiltering() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        Long id = homeIsolations.getId();

        defaultHomeIsolationsShouldBeFound("id.equals=" + id);
        defaultHomeIsolationsShouldNotBeFound("id.notEquals=" + id);

        defaultHomeIsolationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHomeIsolationsShouldNotBeFound("id.greaterThan=" + id);

        defaultHomeIsolationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHomeIsolationsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId equals to DEFAULT_ICMR_ID
        defaultHomeIsolationsShouldBeFound("icmrId.equals=" + DEFAULT_ICMR_ID);

        // Get all the homeIsolationsList where icmrId equals to UPDATED_ICMR_ID
        defaultHomeIsolationsShouldNotBeFound("icmrId.equals=" + UPDATED_ICMR_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId not equals to DEFAULT_ICMR_ID
        defaultHomeIsolationsShouldNotBeFound("icmrId.notEquals=" + DEFAULT_ICMR_ID);

        // Get all the homeIsolationsList where icmrId not equals to UPDATED_ICMR_ID
        defaultHomeIsolationsShouldBeFound("icmrId.notEquals=" + UPDATED_ICMR_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId in DEFAULT_ICMR_ID or UPDATED_ICMR_ID
        defaultHomeIsolationsShouldBeFound("icmrId.in=" + DEFAULT_ICMR_ID + "," + UPDATED_ICMR_ID);

        // Get all the homeIsolationsList where icmrId equals to UPDATED_ICMR_ID
        defaultHomeIsolationsShouldNotBeFound("icmrId.in=" + UPDATED_ICMR_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId is not null
        defaultHomeIsolationsShouldBeFound("icmrId.specified=true");

        // Get all the homeIsolationsList where icmrId is null
        defaultHomeIsolationsShouldNotBeFound("icmrId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId contains DEFAULT_ICMR_ID
        defaultHomeIsolationsShouldBeFound("icmrId.contains=" + DEFAULT_ICMR_ID);

        // Get all the homeIsolationsList where icmrId contains UPDATED_ICMR_ID
        defaultHomeIsolationsShouldNotBeFound("icmrId.contains=" + UPDATED_ICMR_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIcmrIdNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where icmrId does not contain DEFAULT_ICMR_ID
        defaultHomeIsolationsShouldNotBeFound("icmrId.doesNotContain=" + DEFAULT_ICMR_ID);

        // Get all the homeIsolationsList where icmrId does not contain UPDATED_ICMR_ID
        defaultHomeIsolationsShouldBeFound("icmrId.doesNotContain=" + UPDATED_ICMR_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName equals to DEFAULT_FIRST_NAME
        defaultHomeIsolationsShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the homeIsolationsList where firstName equals to UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName not equals to DEFAULT_FIRST_NAME
        defaultHomeIsolationsShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the homeIsolationsList where firstName not equals to UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the homeIsolationsList where firstName equals to UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName is not null
        defaultHomeIsolationsShouldBeFound("firstName.specified=true");

        // Get all the homeIsolationsList where firstName is null
        defaultHomeIsolationsShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName contains DEFAULT_FIRST_NAME
        defaultHomeIsolationsShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the homeIsolationsList where firstName contains UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where firstName does not contain DEFAULT_FIRST_NAME
        defaultHomeIsolationsShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the homeIsolationsList where firstName does not contain UPDATED_FIRST_NAME
        defaultHomeIsolationsShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName equals to DEFAULT_LAST_NAME
        defaultHomeIsolationsShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the homeIsolationsList where lastName equals to UPDATED_LAST_NAME
        defaultHomeIsolationsShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName not equals to DEFAULT_LAST_NAME
        defaultHomeIsolationsShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the homeIsolationsList where lastName not equals to UPDATED_LAST_NAME
        defaultHomeIsolationsShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultHomeIsolationsShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the homeIsolationsList where lastName equals to UPDATED_LAST_NAME
        defaultHomeIsolationsShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName is not null
        defaultHomeIsolationsShouldBeFound("lastName.specified=true");

        // Get all the homeIsolationsList where lastName is null
        defaultHomeIsolationsShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName contains DEFAULT_LAST_NAME
        defaultHomeIsolationsShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the homeIsolationsList where lastName contains UPDATED_LAST_NAME
        defaultHomeIsolationsShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastName does not contain DEFAULT_LAST_NAME
        defaultHomeIsolationsShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the homeIsolationsList where lastName does not contain UPDATED_LAST_NAME
        defaultHomeIsolationsShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude equals to DEFAULT_LATITUDE
        defaultHomeIsolationsShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the homeIsolationsList where latitude equals to UPDATED_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude not equals to DEFAULT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the homeIsolationsList where latitude not equals to UPDATED_LATITUDE
        defaultHomeIsolationsShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultHomeIsolationsShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the homeIsolationsList where latitude equals to UPDATED_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude is not null
        defaultHomeIsolationsShouldBeFound("latitude.specified=true");

        // Get all the homeIsolationsList where latitude is null
        defaultHomeIsolationsShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude contains DEFAULT_LATITUDE
        defaultHomeIsolationsShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the homeIsolationsList where latitude contains UPDATED_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where latitude does not contain DEFAULT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the homeIsolationsList where latitude does not contain UPDATED_LATITUDE
        defaultHomeIsolationsShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude equals to DEFAULT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the homeIsolationsList where longitude equals to UPDATED_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude not equals to DEFAULT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the homeIsolationsList where longitude not equals to UPDATED_LONGITUDE
        defaultHomeIsolationsShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultHomeIsolationsShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the homeIsolationsList where longitude equals to UPDATED_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude is not null
        defaultHomeIsolationsShouldBeFound("longitude.specified=true");

        // Get all the homeIsolationsList where longitude is null
        defaultHomeIsolationsShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude contains DEFAULT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the homeIsolationsList where longitude contains UPDATED_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where longitude does not contain DEFAULT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the homeIsolationsList where longitude does not contain UPDATED_LONGITUDE
        defaultHomeIsolationsShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email equals to DEFAULT_EMAIL
        defaultHomeIsolationsShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the homeIsolationsList where email equals to UPDATED_EMAIL
        defaultHomeIsolationsShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email not equals to DEFAULT_EMAIL
        defaultHomeIsolationsShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the homeIsolationsList where email not equals to UPDATED_EMAIL
        defaultHomeIsolationsShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultHomeIsolationsShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the homeIsolationsList where email equals to UPDATED_EMAIL
        defaultHomeIsolationsShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email is not null
        defaultHomeIsolationsShouldBeFound("email.specified=true");

        // Get all the homeIsolationsList where email is null
        defaultHomeIsolationsShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email contains DEFAULT_EMAIL
        defaultHomeIsolationsShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the homeIsolationsList where email contains UPDATED_EMAIL
        defaultHomeIsolationsShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where email does not contain DEFAULT_EMAIL
        defaultHomeIsolationsShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the homeIsolationsList where email does not contain UPDATED_EMAIL
        defaultHomeIsolationsShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultHomeIsolationsShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the homeIsolationsList where imageUrl equals to UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultHomeIsolationsShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the homeIsolationsList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the homeIsolationsList where imageUrl equals to UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl is not null
        defaultHomeIsolationsShouldBeFound("imageUrl.specified=true");

        // Get all the homeIsolationsList where imageUrl is null
        defaultHomeIsolationsShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl contains DEFAULT_IMAGE_URL
        defaultHomeIsolationsShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the homeIsolationsList where imageUrl contains UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultHomeIsolationsShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the homeIsolationsList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultHomeIsolationsShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where activated equals to DEFAULT_ACTIVATED
        defaultHomeIsolationsShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the homeIsolationsList where activated equals to UPDATED_ACTIVATED
        defaultHomeIsolationsShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where activated not equals to DEFAULT_ACTIVATED
        defaultHomeIsolationsShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the homeIsolationsList where activated not equals to UPDATED_ACTIVATED
        defaultHomeIsolationsShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultHomeIsolationsShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the homeIsolationsList where activated equals to UPDATED_ACTIVATED
        defaultHomeIsolationsShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where activated is not null
        defaultHomeIsolationsShouldBeFound("activated.specified=true");

        // Get all the homeIsolationsList where activated is null
        defaultHomeIsolationsShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo equals to DEFAULT_MOBILE_NO
        defaultHomeIsolationsShouldBeFound("mobileNo.equals=" + DEFAULT_MOBILE_NO);

        // Get all the homeIsolationsList where mobileNo equals to UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldNotBeFound("mobileNo.equals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo not equals to DEFAULT_MOBILE_NO
        defaultHomeIsolationsShouldNotBeFound("mobileNo.notEquals=" + DEFAULT_MOBILE_NO);

        // Get all the homeIsolationsList where mobileNo not equals to UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldBeFound("mobileNo.notEquals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo in DEFAULT_MOBILE_NO or UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldBeFound("mobileNo.in=" + DEFAULT_MOBILE_NO + "," + UPDATED_MOBILE_NO);

        // Get all the homeIsolationsList where mobileNo equals to UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldNotBeFound("mobileNo.in=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo is not null
        defaultHomeIsolationsShouldBeFound("mobileNo.specified=true");

        // Get all the homeIsolationsList where mobileNo is null
        defaultHomeIsolationsShouldNotBeFound("mobileNo.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo contains DEFAULT_MOBILE_NO
        defaultHomeIsolationsShouldBeFound("mobileNo.contains=" + DEFAULT_MOBILE_NO);

        // Get all the homeIsolationsList where mobileNo contains UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldNotBeFound("mobileNo.contains=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByMobileNoNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where mobileNo does not contain DEFAULT_MOBILE_NO
        defaultHomeIsolationsShouldNotBeFound("mobileNo.doesNotContain=" + DEFAULT_MOBILE_NO);

        // Get all the homeIsolationsList where mobileNo does not contain UPDATED_MOBILE_NO
        defaultHomeIsolationsShouldBeFound("mobileNo.doesNotContain=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash equals to DEFAULT_PASSWORD_HASH
        defaultHomeIsolationsShouldBeFound("passwordHash.equals=" + DEFAULT_PASSWORD_HASH);

        // Get all the homeIsolationsList where passwordHash equals to UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldNotBeFound("passwordHash.equals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash not equals to DEFAULT_PASSWORD_HASH
        defaultHomeIsolationsShouldNotBeFound("passwordHash.notEquals=" + DEFAULT_PASSWORD_HASH);

        // Get all the homeIsolationsList where passwordHash not equals to UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldBeFound("passwordHash.notEquals=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash in DEFAULT_PASSWORD_HASH or UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldBeFound("passwordHash.in=" + DEFAULT_PASSWORD_HASH + "," + UPDATED_PASSWORD_HASH);

        // Get all the homeIsolationsList where passwordHash equals to UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldNotBeFound("passwordHash.in=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash is not null
        defaultHomeIsolationsShouldBeFound("passwordHash.specified=true");

        // Get all the homeIsolationsList where passwordHash is null
        defaultHomeIsolationsShouldNotBeFound("passwordHash.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash contains DEFAULT_PASSWORD_HASH
        defaultHomeIsolationsShouldBeFound("passwordHash.contains=" + DEFAULT_PASSWORD_HASH);

        // Get all the homeIsolationsList where passwordHash contains UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldNotBeFound("passwordHash.contains=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPasswordHashNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where passwordHash does not contain DEFAULT_PASSWORD_HASH
        defaultHomeIsolationsShouldNotBeFound("passwordHash.doesNotContain=" + DEFAULT_PASSWORD_HASH);

        // Get all the homeIsolationsList where passwordHash does not contain UPDATED_PASSWORD_HASH
        defaultHomeIsolationsShouldBeFound("passwordHash.doesNotContain=" + UPDATED_PASSWORD_HASH);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo equals to DEFAULT_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.equals=" + DEFAULT_SECONDARY_CONTACT_NO);

        // Get all the homeIsolationsList where secondaryContactNo equals to UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.equals=" + UPDATED_SECONDARY_CONTACT_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo not equals to DEFAULT_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.notEquals=" + DEFAULT_SECONDARY_CONTACT_NO);

        // Get all the homeIsolationsList where secondaryContactNo not equals to UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.notEquals=" + UPDATED_SECONDARY_CONTACT_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo in DEFAULT_SECONDARY_CONTACT_NO or UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.in=" + DEFAULT_SECONDARY_CONTACT_NO + "," + UPDATED_SECONDARY_CONTACT_NO);

        // Get all the homeIsolationsList where secondaryContactNo equals to UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.in=" + UPDATED_SECONDARY_CONTACT_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo is not null
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.specified=true");

        // Get all the homeIsolationsList where secondaryContactNo is null
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo contains DEFAULT_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.contains=" + DEFAULT_SECONDARY_CONTACT_NO);

        // Get all the homeIsolationsList where secondaryContactNo contains UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.contains=" + UPDATED_SECONDARY_CONTACT_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySecondaryContactNoNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where secondaryContactNo does not contain DEFAULT_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldNotBeFound("secondaryContactNo.doesNotContain=" + DEFAULT_SECONDARY_CONTACT_NO);

        // Get all the homeIsolationsList where secondaryContactNo does not contain UPDATED_SECONDARY_CONTACT_NO
        defaultHomeIsolationsShouldBeFound("secondaryContactNo.doesNotContain=" + UPDATED_SECONDARY_CONTACT_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo equals to DEFAULT_AADHAR_CARD_NO
        defaultHomeIsolationsShouldBeFound("aadharCardNo.equals=" + DEFAULT_AADHAR_CARD_NO);

        // Get all the homeIsolationsList where aadharCardNo equals to UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.equals=" + UPDATED_AADHAR_CARD_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo not equals to DEFAULT_AADHAR_CARD_NO
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.notEquals=" + DEFAULT_AADHAR_CARD_NO);

        // Get all the homeIsolationsList where aadharCardNo not equals to UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldBeFound("aadharCardNo.notEquals=" + UPDATED_AADHAR_CARD_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo in DEFAULT_AADHAR_CARD_NO or UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldBeFound("aadharCardNo.in=" + DEFAULT_AADHAR_CARD_NO + "," + UPDATED_AADHAR_CARD_NO);

        // Get all the homeIsolationsList where aadharCardNo equals to UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.in=" + UPDATED_AADHAR_CARD_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo is not null
        defaultHomeIsolationsShouldBeFound("aadharCardNo.specified=true");

        // Get all the homeIsolationsList where aadharCardNo is null
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo contains DEFAULT_AADHAR_CARD_NO
        defaultHomeIsolationsShouldBeFound("aadharCardNo.contains=" + DEFAULT_AADHAR_CARD_NO);

        // Get all the homeIsolationsList where aadharCardNo contains UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.contains=" + UPDATED_AADHAR_CARD_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAadharCardNoNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where aadharCardNo does not contain DEFAULT_AADHAR_CARD_NO
        defaultHomeIsolationsShouldNotBeFound("aadharCardNo.doesNotContain=" + DEFAULT_AADHAR_CARD_NO);

        // Get all the homeIsolationsList where aadharCardNo does not contain UPDATED_AADHAR_CARD_NO
        defaultHomeIsolationsShouldBeFound("aadharCardNo.doesNotContain=" + UPDATED_AADHAR_CARD_NO);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where status equals to DEFAULT_STATUS
        defaultHomeIsolationsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the homeIsolationsList where status equals to UPDATED_STATUS
        defaultHomeIsolationsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where status not equals to DEFAULT_STATUS
        defaultHomeIsolationsShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the homeIsolationsList where status not equals to UPDATED_STATUS
        defaultHomeIsolationsShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultHomeIsolationsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the homeIsolationsList where status equals to UPDATED_STATUS
        defaultHomeIsolationsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where status is not null
        defaultHomeIsolationsShouldBeFound("status.specified=true");

        // Get all the homeIsolationsList where status is null
        defaultHomeIsolationsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age equals to DEFAULT_AGE
        defaultHomeIsolationsShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the homeIsolationsList where age equals to UPDATED_AGE
        defaultHomeIsolationsShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age not equals to DEFAULT_AGE
        defaultHomeIsolationsShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the homeIsolationsList where age not equals to UPDATED_AGE
        defaultHomeIsolationsShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age in DEFAULT_AGE or UPDATED_AGE
        defaultHomeIsolationsShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the homeIsolationsList where age equals to UPDATED_AGE
        defaultHomeIsolationsShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age is not null
        defaultHomeIsolationsShouldBeFound("age.specified=true");

        // Get all the homeIsolationsList where age is null
        defaultHomeIsolationsShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age contains DEFAULT_AGE
        defaultHomeIsolationsShouldBeFound("age.contains=" + DEFAULT_AGE);

        // Get all the homeIsolationsList where age contains UPDATED_AGE
        defaultHomeIsolationsShouldNotBeFound("age.contains=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAgeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where age does not contain DEFAULT_AGE
        defaultHomeIsolationsShouldNotBeFound("age.doesNotContain=" + DEFAULT_AGE);

        // Get all the homeIsolationsList where age does not contain UPDATED_AGE
        defaultHomeIsolationsShouldBeFound("age.doesNotContain=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender equals to DEFAULT_GENDER
        defaultHomeIsolationsShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the homeIsolationsList where gender equals to UPDATED_GENDER
        defaultHomeIsolationsShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender not equals to DEFAULT_GENDER
        defaultHomeIsolationsShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the homeIsolationsList where gender not equals to UPDATED_GENDER
        defaultHomeIsolationsShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultHomeIsolationsShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the homeIsolationsList where gender equals to UPDATED_GENDER
        defaultHomeIsolationsShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender is not null
        defaultHomeIsolationsShouldBeFound("gender.specified=true");

        // Get all the homeIsolationsList where gender is null
        defaultHomeIsolationsShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender contains DEFAULT_GENDER
        defaultHomeIsolationsShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the homeIsolationsList where gender contains UPDATED_GENDER
        defaultHomeIsolationsShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where gender does not contain DEFAULT_GENDER
        defaultHomeIsolationsShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the homeIsolationsList where gender does not contain UPDATED_GENDER
        defaultHomeIsolationsShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId equals to DEFAULT_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.equals=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId equals to UPDATED_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.equals=" + UPDATED_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId not equals to DEFAULT_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.notEquals=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId not equals to UPDATED_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.notEquals=" + UPDATED_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId in DEFAULT_STATE_ID or UPDATED_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.in=" + DEFAULT_STATE_ID + "," + UPDATED_STATE_ID);

        // Get all the homeIsolationsList where stateId equals to UPDATED_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.in=" + UPDATED_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId is not null
        defaultHomeIsolationsShouldBeFound("stateId.specified=true");

        // Get all the homeIsolationsList where stateId is null
        defaultHomeIsolationsShouldNotBeFound("stateId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId is greater than or equal to DEFAULT_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.greaterThanOrEqual=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId is greater than or equal to UPDATED_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.greaterThanOrEqual=" + UPDATED_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId is less than or equal to DEFAULT_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.lessThanOrEqual=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId is less than or equal to SMALLER_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.lessThanOrEqual=" + SMALLER_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId is less than DEFAULT_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.lessThan=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId is less than UPDATED_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.lessThan=" + UPDATED_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByStateIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where stateId is greater than DEFAULT_STATE_ID
        defaultHomeIsolationsShouldNotBeFound("stateId.greaterThan=" + DEFAULT_STATE_ID);

        // Get all the homeIsolationsList where stateId is greater than SMALLER_STATE_ID
        defaultHomeIsolationsShouldBeFound("stateId.greaterThan=" + SMALLER_STATE_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId equals to DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.equals=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId equals to UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId not equals to DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.notEquals=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId not equals to UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.notEquals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId in DEFAULT_DISTRICT_ID or UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId equals to UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.in=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId is not null
        defaultHomeIsolationsShouldBeFound("districtId.specified=true");

        // Get all the homeIsolationsList where districtId is null
        defaultHomeIsolationsShouldNotBeFound("districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId is greater than or equal to DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId is greater than or equal to UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId is less than or equal to DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId is less than or equal to SMALLER_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId is less than DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.lessThan=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId is less than UPDATED_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.lessThan=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where districtId is greater than DEFAULT_DISTRICT_ID
        defaultHomeIsolationsShouldNotBeFound("districtId.greaterThan=" + DEFAULT_DISTRICT_ID);

        // Get all the homeIsolationsList where districtId is greater than SMALLER_DISTRICT_ID
        defaultHomeIsolationsShouldBeFound("districtId.greaterThan=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId equals to DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.equals=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId equals to UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.equals=" + UPDATED_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId not equals to DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.notEquals=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId not equals to UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.notEquals=" + UPDATED_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId in DEFAULT_TALUKA_ID or UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.in=" + DEFAULT_TALUKA_ID + "," + UPDATED_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId equals to UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.in=" + UPDATED_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId is not null
        defaultHomeIsolationsShouldBeFound("talukaId.specified=true");

        // Get all the homeIsolationsList where talukaId is null
        defaultHomeIsolationsShouldNotBeFound("talukaId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId is greater than or equal to DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.greaterThanOrEqual=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId is greater than or equal to UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.greaterThanOrEqual=" + UPDATED_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId is less than or equal to DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.lessThanOrEqual=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId is less than or equal to SMALLER_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.lessThanOrEqual=" + SMALLER_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId is less than DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.lessThan=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId is less than UPDATED_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.lessThan=" + UPDATED_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByTalukaIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where talukaId is greater than DEFAULT_TALUKA_ID
        defaultHomeIsolationsShouldNotBeFound("talukaId.greaterThan=" + DEFAULT_TALUKA_ID);

        // Get all the homeIsolationsList where talukaId is greater than SMALLER_TALUKA_ID
        defaultHomeIsolationsShouldBeFound("talukaId.greaterThan=" + SMALLER_TALUKA_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId equals to DEFAULT_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.equals=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId equals to UPDATED_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.equals=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId not equals to DEFAULT_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.notEquals=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId not equals to UPDATED_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.notEquals=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId in DEFAULT_CITY_ID or UPDATED_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.in=" + DEFAULT_CITY_ID + "," + UPDATED_CITY_ID);

        // Get all the homeIsolationsList where cityId equals to UPDATED_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.in=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId is not null
        defaultHomeIsolationsShouldBeFound("cityId.specified=true");

        // Get all the homeIsolationsList where cityId is null
        defaultHomeIsolationsShouldNotBeFound("cityId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId is greater than or equal to DEFAULT_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.greaterThanOrEqual=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId is greater than or equal to UPDATED_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.greaterThanOrEqual=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId is less than or equal to DEFAULT_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.lessThanOrEqual=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId is less than or equal to SMALLER_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.lessThanOrEqual=" + SMALLER_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId is less than DEFAULT_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.lessThan=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId is less than UPDATED_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.lessThan=" + UPDATED_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where cityId is greater than DEFAULT_CITY_ID
        defaultHomeIsolationsShouldNotBeFound("cityId.greaterThan=" + DEFAULT_CITY_ID);

        // Get all the homeIsolationsList where cityId is greater than SMALLER_CITY_ID
        defaultHomeIsolationsShouldBeFound("cityId.greaterThan=" + SMALLER_CITY_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address equals to DEFAULT_ADDRESS
        defaultHomeIsolationsShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the homeIsolationsList where address equals to UPDATED_ADDRESS
        defaultHomeIsolationsShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address not equals to DEFAULT_ADDRESS
        defaultHomeIsolationsShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the homeIsolationsList where address not equals to UPDATED_ADDRESS
        defaultHomeIsolationsShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHomeIsolationsShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the homeIsolationsList where address equals to UPDATED_ADDRESS
        defaultHomeIsolationsShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address is not null
        defaultHomeIsolationsShouldBeFound("address.specified=true");

        // Get all the homeIsolationsList where address is null
        defaultHomeIsolationsShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address contains DEFAULT_ADDRESS
        defaultHomeIsolationsShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the homeIsolationsList where address contains UPDATED_ADDRESS
        defaultHomeIsolationsShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where address does not contain DEFAULT_ADDRESS
        defaultHomeIsolationsShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the homeIsolationsList where address does not contain UPDATED_ADDRESS
        defaultHomeIsolationsShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode equals to DEFAULT_PINCODE
        defaultHomeIsolationsShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the homeIsolationsList where pincode equals to UPDATED_PINCODE
        defaultHomeIsolationsShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode not equals to DEFAULT_PINCODE
        defaultHomeIsolationsShouldNotBeFound("pincode.notEquals=" + DEFAULT_PINCODE);

        // Get all the homeIsolationsList where pincode not equals to UPDATED_PINCODE
        defaultHomeIsolationsShouldBeFound("pincode.notEquals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultHomeIsolationsShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the homeIsolationsList where pincode equals to UPDATED_PINCODE
        defaultHomeIsolationsShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode is not null
        defaultHomeIsolationsShouldBeFound("pincode.specified=true");

        // Get all the homeIsolationsList where pincode is null
        defaultHomeIsolationsShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode contains DEFAULT_PINCODE
        defaultHomeIsolationsShouldBeFound("pincode.contains=" + DEFAULT_PINCODE);

        // Get all the homeIsolationsList where pincode contains UPDATED_PINCODE
        defaultHomeIsolationsShouldNotBeFound("pincode.contains=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByPincodeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where pincode does not contain DEFAULT_PINCODE
        defaultHomeIsolationsShouldNotBeFound("pincode.doesNotContain=" + DEFAULT_PINCODE);

        // Get all the homeIsolationsList where pincode does not contain UPDATED_PINCODE
        defaultHomeIsolationsShouldBeFound("pincode.doesNotContain=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCollectionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where collectionDate equals to DEFAULT_COLLECTION_DATE
        defaultHomeIsolationsShouldBeFound("collectionDate.equals=" + DEFAULT_COLLECTION_DATE);

        // Get all the homeIsolationsList where collectionDate equals to UPDATED_COLLECTION_DATE
        defaultHomeIsolationsShouldNotBeFound("collectionDate.equals=" + UPDATED_COLLECTION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCollectionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where collectionDate not equals to DEFAULT_COLLECTION_DATE
        defaultHomeIsolationsShouldNotBeFound("collectionDate.notEquals=" + DEFAULT_COLLECTION_DATE);

        // Get all the homeIsolationsList where collectionDate not equals to UPDATED_COLLECTION_DATE
        defaultHomeIsolationsShouldBeFound("collectionDate.notEquals=" + UPDATED_COLLECTION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCollectionDateIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where collectionDate in DEFAULT_COLLECTION_DATE or UPDATED_COLLECTION_DATE
        defaultHomeIsolationsShouldBeFound("collectionDate.in=" + DEFAULT_COLLECTION_DATE + "," + UPDATED_COLLECTION_DATE);

        // Get all the homeIsolationsList where collectionDate equals to UPDATED_COLLECTION_DATE
        defaultHomeIsolationsShouldNotBeFound("collectionDate.in=" + UPDATED_COLLECTION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCollectionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where collectionDate is not null
        defaultHomeIsolationsShouldBeFound("collectionDate.specified=true");

        // Get all the homeIsolationsList where collectionDate is null
        defaultHomeIsolationsShouldNotBeFound("collectionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizedIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalized equals to DEFAULT_HOSPITALIZED
        defaultHomeIsolationsShouldBeFound("hospitalized.equals=" + DEFAULT_HOSPITALIZED);

        // Get all the homeIsolationsList where hospitalized equals to UPDATED_HOSPITALIZED
        defaultHomeIsolationsShouldNotBeFound("hospitalized.equals=" + UPDATED_HOSPITALIZED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalized not equals to DEFAULT_HOSPITALIZED
        defaultHomeIsolationsShouldNotBeFound("hospitalized.notEquals=" + DEFAULT_HOSPITALIZED);

        // Get all the homeIsolationsList where hospitalized not equals to UPDATED_HOSPITALIZED
        defaultHomeIsolationsShouldBeFound("hospitalized.notEquals=" + UPDATED_HOSPITALIZED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizedIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalized in DEFAULT_HOSPITALIZED or UPDATED_HOSPITALIZED
        defaultHomeIsolationsShouldBeFound("hospitalized.in=" + DEFAULT_HOSPITALIZED + "," + UPDATED_HOSPITALIZED);

        // Get all the homeIsolationsList where hospitalized equals to UPDATED_HOSPITALIZED
        defaultHomeIsolationsShouldNotBeFound("hospitalized.in=" + UPDATED_HOSPITALIZED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizedIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalized is not null
        defaultHomeIsolationsShouldBeFound("hospitalized.specified=true");

        // Get all the homeIsolationsList where hospitalized is null
        defaultHomeIsolationsShouldNotBeFound("hospitalized.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId equals to DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.equals=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId equals to UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.equals=" + UPDATED_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId not equals to DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.notEquals=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId not equals to UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.notEquals=" + UPDATED_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId in DEFAULT_HOSPITAL_ID or UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.in=" + DEFAULT_HOSPITAL_ID + "," + UPDATED_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId equals to UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.in=" + UPDATED_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId is not null
        defaultHomeIsolationsShouldBeFound("hospitalId.specified=true");

        // Get all the homeIsolationsList where hospitalId is null
        defaultHomeIsolationsShouldNotBeFound("hospitalId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId is greater than or equal to DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.greaterThanOrEqual=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId is greater than or equal to UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.greaterThanOrEqual=" + UPDATED_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId is less than or equal to DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.lessThanOrEqual=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId is less than or equal to SMALLER_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.lessThanOrEqual=" + SMALLER_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId is less than DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.lessThan=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId is less than UPDATED_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.lessThan=" + UPDATED_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalId is greater than DEFAULT_HOSPITAL_ID
        defaultHomeIsolationsShouldNotBeFound("hospitalId.greaterThan=" + DEFAULT_HOSPITAL_ID);

        // Get all the homeIsolationsList where hospitalId is greater than SMALLER_HOSPITAL_ID
        defaultHomeIsolationsShouldBeFound("hospitalId.greaterThan=" + SMALLER_HOSPITAL_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude equals to DEFAULT_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldBeFound("addressLatitude.equals=" + DEFAULT_ADDRESS_LATITUDE);

        // Get all the homeIsolationsList where addressLatitude equals to UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.equals=" + UPDATED_ADDRESS_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude not equals to DEFAULT_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.notEquals=" + DEFAULT_ADDRESS_LATITUDE);

        // Get all the homeIsolationsList where addressLatitude not equals to UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldBeFound("addressLatitude.notEquals=" + UPDATED_ADDRESS_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude in DEFAULT_ADDRESS_LATITUDE or UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldBeFound("addressLatitude.in=" + DEFAULT_ADDRESS_LATITUDE + "," + UPDATED_ADDRESS_LATITUDE);

        // Get all the homeIsolationsList where addressLatitude equals to UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.in=" + UPDATED_ADDRESS_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude is not null
        defaultHomeIsolationsShouldBeFound("addressLatitude.specified=true");

        // Get all the homeIsolationsList where addressLatitude is null
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude contains DEFAULT_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldBeFound("addressLatitude.contains=" + DEFAULT_ADDRESS_LATITUDE);

        // Get all the homeIsolationsList where addressLatitude contains UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.contains=" + UPDATED_ADDRESS_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLatitude does not contain DEFAULT_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLatitude.doesNotContain=" + DEFAULT_ADDRESS_LATITUDE);

        // Get all the homeIsolationsList where addressLatitude does not contain UPDATED_ADDRESS_LATITUDE
        defaultHomeIsolationsShouldBeFound("addressLatitude.doesNotContain=" + UPDATED_ADDRESS_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude equals to DEFAULT_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldBeFound("addressLongitude.equals=" + DEFAULT_ADDRESS_LONGITUDE);

        // Get all the homeIsolationsList where addressLongitude equals to UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.equals=" + UPDATED_ADDRESS_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude not equals to DEFAULT_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.notEquals=" + DEFAULT_ADDRESS_LONGITUDE);

        // Get all the homeIsolationsList where addressLongitude not equals to UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldBeFound("addressLongitude.notEquals=" + UPDATED_ADDRESS_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude in DEFAULT_ADDRESS_LONGITUDE or UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldBeFound("addressLongitude.in=" + DEFAULT_ADDRESS_LONGITUDE + "," + UPDATED_ADDRESS_LONGITUDE);

        // Get all the homeIsolationsList where addressLongitude equals to UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.in=" + UPDATED_ADDRESS_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude is not null
        defaultHomeIsolationsShouldBeFound("addressLongitude.specified=true");

        // Get all the homeIsolationsList where addressLongitude is null
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude contains DEFAULT_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldBeFound("addressLongitude.contains=" + DEFAULT_ADDRESS_LONGITUDE);

        // Get all the homeIsolationsList where addressLongitude contains UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.contains=" + UPDATED_ADDRESS_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAddressLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where addressLongitude does not contain DEFAULT_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("addressLongitude.doesNotContain=" + DEFAULT_ADDRESS_LONGITUDE);

        // Get all the homeIsolationsList where addressLongitude does not contain UPDATED_ADDRESS_LONGITUDE
        defaultHomeIsolationsShouldBeFound("addressLongitude.doesNotContain=" + UPDATED_ADDRESS_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude equals to DEFAULT_CURRENT_LATITUDE
        defaultHomeIsolationsShouldBeFound("currentLatitude.equals=" + DEFAULT_CURRENT_LATITUDE);

        // Get all the homeIsolationsList where currentLatitude equals to UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.equals=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude not equals to DEFAULT_CURRENT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.notEquals=" + DEFAULT_CURRENT_LATITUDE);

        // Get all the homeIsolationsList where currentLatitude not equals to UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldBeFound("currentLatitude.notEquals=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude in DEFAULT_CURRENT_LATITUDE or UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldBeFound("currentLatitude.in=" + DEFAULT_CURRENT_LATITUDE + "," + UPDATED_CURRENT_LATITUDE);

        // Get all the homeIsolationsList where currentLatitude equals to UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.in=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude is not null
        defaultHomeIsolationsShouldBeFound("currentLatitude.specified=true");

        // Get all the homeIsolationsList where currentLatitude is null
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude contains DEFAULT_CURRENT_LATITUDE
        defaultHomeIsolationsShouldBeFound("currentLatitude.contains=" + DEFAULT_CURRENT_LATITUDE);

        // Get all the homeIsolationsList where currentLatitude contains UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.contains=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLatitude does not contain DEFAULT_CURRENT_LATITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLatitude.doesNotContain=" + DEFAULT_CURRENT_LATITUDE);

        // Get all the homeIsolationsList where currentLatitude does not contain UPDATED_CURRENT_LATITUDE
        defaultHomeIsolationsShouldBeFound("currentLatitude.doesNotContain=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude equals to DEFAULT_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("currentLongitude.equals=" + DEFAULT_CURRENT_LONGITUDE);

        // Get all the homeIsolationsList where currentLongitude equals to UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.equals=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude not equals to DEFAULT_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.notEquals=" + DEFAULT_CURRENT_LONGITUDE);

        // Get all the homeIsolationsList where currentLongitude not equals to UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("currentLongitude.notEquals=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude in DEFAULT_CURRENT_LONGITUDE or UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("currentLongitude.in=" + DEFAULT_CURRENT_LONGITUDE + "," + UPDATED_CURRENT_LONGITUDE);

        // Get all the homeIsolationsList where currentLongitude equals to UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.in=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude is not null
        defaultHomeIsolationsShouldBeFound("currentLongitude.specified=true");

        // Get all the homeIsolationsList where currentLongitude is null
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude contains DEFAULT_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("currentLongitude.contains=" + DEFAULT_CURRENT_LONGITUDE);

        // Get all the homeIsolationsList where currentLongitude contains UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.contains=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCurrentLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where currentLongitude does not contain DEFAULT_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldNotBeFound("currentLongitude.doesNotContain=" + DEFAULT_CURRENT_LONGITUDE);

        // Get all the homeIsolationsList where currentLongitude does not contain UPDATED_CURRENT_LONGITUDE
        defaultHomeIsolationsShouldBeFound("currentLongitude.doesNotContain=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalizationDate equals to DEFAULT_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldBeFound("hospitalizationDate.equals=" + DEFAULT_HOSPITALIZATION_DATE);

        // Get all the homeIsolationsList where hospitalizationDate equals to UPDATED_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldNotBeFound("hospitalizationDate.equals=" + UPDATED_HOSPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalizationDate not equals to DEFAULT_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldNotBeFound("hospitalizationDate.notEquals=" + DEFAULT_HOSPITALIZATION_DATE);

        // Get all the homeIsolationsList where hospitalizationDate not equals to UPDATED_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldBeFound("hospitalizationDate.notEquals=" + UPDATED_HOSPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizationDateIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalizationDate in DEFAULT_HOSPITALIZATION_DATE or UPDATED_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldBeFound("hospitalizationDate.in=" + DEFAULT_HOSPITALIZATION_DATE + "," + UPDATED_HOSPITALIZATION_DATE);

        // Get all the homeIsolationsList where hospitalizationDate equals to UPDATED_HOSPITALIZATION_DATE
        defaultHomeIsolationsShouldNotBeFound("hospitalizationDate.in=" + UPDATED_HOSPITALIZATION_DATE);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHospitalizationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where hospitalizationDate is not null
        defaultHomeIsolationsShouldBeFound("hospitalizationDate.specified=true");

        // Get all the homeIsolationsList where hospitalizationDate is null
        defaultHomeIsolationsShouldNotBeFound("hospitalizationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHealthConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where healthCondition equals to DEFAULT_HEALTH_CONDITION
        defaultHomeIsolationsShouldBeFound("healthCondition.equals=" + DEFAULT_HEALTH_CONDITION);

        // Get all the homeIsolationsList where healthCondition equals to UPDATED_HEALTH_CONDITION
        defaultHomeIsolationsShouldNotBeFound("healthCondition.equals=" + UPDATED_HEALTH_CONDITION);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHealthConditionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where healthCondition not equals to DEFAULT_HEALTH_CONDITION
        defaultHomeIsolationsShouldNotBeFound("healthCondition.notEquals=" + DEFAULT_HEALTH_CONDITION);

        // Get all the homeIsolationsList where healthCondition not equals to UPDATED_HEALTH_CONDITION
        defaultHomeIsolationsShouldBeFound("healthCondition.notEquals=" + UPDATED_HEALTH_CONDITION);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHealthConditionIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where healthCondition in DEFAULT_HEALTH_CONDITION or UPDATED_HEALTH_CONDITION
        defaultHomeIsolationsShouldBeFound("healthCondition.in=" + DEFAULT_HEALTH_CONDITION + "," + UPDATED_HEALTH_CONDITION);

        // Get all the homeIsolationsList where healthCondition equals to UPDATED_HEALTH_CONDITION
        defaultHomeIsolationsShouldNotBeFound("healthCondition.in=" + UPDATED_HEALTH_CONDITION);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByHealthConditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where healthCondition is not null
        defaultHomeIsolationsShouldBeFound("healthCondition.specified=true");

        // Get all the homeIsolationsList where healthCondition is null
        defaultHomeIsolationsShouldNotBeFound("healthCondition.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks equals to DEFAULT_REMARKS
        defaultHomeIsolationsShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the homeIsolationsList where remarks equals to UPDATED_REMARKS
        defaultHomeIsolationsShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks not equals to DEFAULT_REMARKS
        defaultHomeIsolationsShouldNotBeFound("remarks.notEquals=" + DEFAULT_REMARKS);

        // Get all the homeIsolationsList where remarks not equals to UPDATED_REMARKS
        defaultHomeIsolationsShouldBeFound("remarks.notEquals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultHomeIsolationsShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the homeIsolationsList where remarks equals to UPDATED_REMARKS
        defaultHomeIsolationsShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks is not null
        defaultHomeIsolationsShouldBeFound("remarks.specified=true");

        // Get all the homeIsolationsList where remarks is null
        defaultHomeIsolationsShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks contains DEFAULT_REMARKS
        defaultHomeIsolationsShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the homeIsolationsList where remarks contains UPDATED_REMARKS
        defaultHomeIsolationsShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where remarks does not contain DEFAULT_REMARKS
        defaultHomeIsolationsShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the homeIsolationsList where remarks does not contain UPDATED_REMARKS
        defaultHomeIsolationsShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId equals to DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.equals=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId equals to UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.equals=" + UPDATED_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId not equals to DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.notEquals=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId not equals to UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.notEquals=" + UPDATED_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId in DEFAULT_CCMS_USER_ID or UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.in=" + DEFAULT_CCMS_USER_ID + "," + UPDATED_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId equals to UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.in=" + UPDATED_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId is not null
        defaultHomeIsolationsShouldBeFound("ccmsUserId.specified=true");

        // Get all the homeIsolationsList where ccmsUserId is null
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId is greater than or equal to DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.greaterThanOrEqual=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId is greater than or equal to UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.greaterThanOrEqual=" + UPDATED_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId is less than or equal to DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.lessThanOrEqual=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId is less than or equal to SMALLER_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.lessThanOrEqual=" + SMALLER_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId is less than DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.lessThan=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId is less than UPDATED_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.lessThan=" + UPDATED_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByCcmsUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where ccmsUserId is greater than DEFAULT_CCMS_USER_ID
        defaultHomeIsolationsShouldNotBeFound("ccmsUserId.greaterThan=" + DEFAULT_CCMS_USER_ID);

        // Get all the homeIsolationsList where ccmsUserId is greater than SMALLER_CCMS_USER_ID
        defaultHomeIsolationsShouldBeFound("ccmsUserId.greaterThan=" + SMALLER_CCMS_USER_ID);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySelfRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where selfRegistered equals to DEFAULT_SELF_REGISTERED
        defaultHomeIsolationsShouldBeFound("selfRegistered.equals=" + DEFAULT_SELF_REGISTERED);

        // Get all the homeIsolationsList where selfRegistered equals to UPDATED_SELF_REGISTERED
        defaultHomeIsolationsShouldNotBeFound("selfRegistered.equals=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySelfRegisteredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where selfRegistered not equals to DEFAULT_SELF_REGISTERED
        defaultHomeIsolationsShouldNotBeFound("selfRegistered.notEquals=" + DEFAULT_SELF_REGISTERED);

        // Get all the homeIsolationsList where selfRegistered not equals to UPDATED_SELF_REGISTERED
        defaultHomeIsolationsShouldBeFound("selfRegistered.notEquals=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySelfRegisteredIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where selfRegistered in DEFAULT_SELF_REGISTERED or UPDATED_SELF_REGISTERED
        defaultHomeIsolationsShouldBeFound("selfRegistered.in=" + DEFAULT_SELF_REGISTERED + "," + UPDATED_SELF_REGISTERED);

        // Get all the homeIsolationsList where selfRegistered equals to UPDATED_SELF_REGISTERED
        defaultHomeIsolationsShouldNotBeFound("selfRegistered.in=" + UPDATED_SELF_REGISTERED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsBySelfRegisteredIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where selfRegistered is not null
        defaultHomeIsolationsShouldBeFound("selfRegistered.specified=true");

        // Get all the homeIsolationsList where selfRegistered is null
        defaultHomeIsolationsShouldNotBeFound("selfRegistered.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultHomeIsolationsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the homeIsolationsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultHomeIsolationsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultHomeIsolationsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the homeIsolationsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultHomeIsolationsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultHomeIsolationsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the homeIsolationsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultHomeIsolationsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModified is not null
        defaultHomeIsolationsShouldBeFound("lastModified.specified=true");

        // Get all the homeIsolationsList where lastModified is null
        defaultHomeIsolationsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the homeIsolationsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the homeIsolationsList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the homeIsolationsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy is not null
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the homeIsolationsList where lastModifiedBy is null
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the homeIsolationsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        // Get all the homeIsolationsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the homeIsolationsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultHomeIsolationsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByIsolationsDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);
        IsolationsDetails isolationsDetails;
        if (TestUtil.findAll(em, IsolationsDetails.class).isEmpty()) {
            isolationsDetails = IsolationsDetailsResourceIT.createEntity(em);
            em.persist(isolationsDetails);
            em.flush();
        } else {
            isolationsDetails = TestUtil.findAll(em, IsolationsDetails.class).get(0);
        }
        em.persist(isolationsDetails);
        em.flush();
        homeIsolations.setIsolationsDetails(isolationsDetails);
        homeIsolationsRepository.saveAndFlush(homeIsolations);
        Long isolationsDetailsId = isolationsDetails.getId();

        // Get all the homeIsolationsList where isolationsDetails equals to isolationsDetailsId
        defaultHomeIsolationsShouldBeFound("isolationsDetailsId.equals=" + isolationsDetailsId);

        // Get all the homeIsolationsList where isolationsDetails equals to (isolationsDetailsId + 1)
        defaultHomeIsolationsShouldNotBeFound("isolationsDetailsId.equals=" + (isolationsDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllHomeIsolationsByAssessmentIsEqualToSomething() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);
        Assessment assessment;
        if (TestUtil.findAll(em, Assessment.class).isEmpty()) {
            assessment = AssessmentResourceIT.createEntity(em);
            em.persist(assessment);
            em.flush();
        } else {
            assessment = TestUtil.findAll(em, Assessment.class).get(0);
        }
        em.persist(assessment);
        em.flush();
        homeIsolations.addAssessment(assessment);
        homeIsolationsRepository.saveAndFlush(homeIsolations);
        Long assessmentId = assessment.getId();

        // Get all the homeIsolationsList where assessment equals to assessmentId
        defaultHomeIsolationsShouldBeFound("assessmentId.equals=" + assessmentId);

        // Get all the homeIsolationsList where assessment equals to (assessmentId + 1)
        defaultHomeIsolationsShouldNotBeFound("assessmentId.equals=" + (assessmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomeIsolationsShouldBeFound(String filter) throws Exception {
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeIsolations.getId().intValue())))
            .andExpect(jsonPath("$.[*].icmrId").value(hasItem(DEFAULT_ICMR_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].secondaryContactNo").value(hasItem(DEFAULT_SECONDARY_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].aadharCardNo").value(hasItem(DEFAULT_AADHAR_CARD_NO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].stateId").value(hasItem(DEFAULT_STATE_ID.intValue())))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID.intValue())))
            .andExpect(jsonPath("$.[*].talukaId").value(hasItem(DEFAULT_TALUKA_ID.intValue())))
            .andExpect(jsonPath("$.[*].cityId").value(hasItem(DEFAULT_CITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].collectionDate").value(hasItem(DEFAULT_COLLECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].hospitalized").value(hasItem(DEFAULT_HOSPITALIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].hospitalId").value(hasItem(DEFAULT_HOSPITAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].addressLatitude").value(hasItem(DEFAULT_ADDRESS_LATITUDE)))
            .andExpect(jsonPath("$.[*].addressLongitude").value(hasItem(DEFAULT_ADDRESS_LONGITUDE)))
            .andExpect(jsonPath("$.[*].currentLatitude").value(hasItem(DEFAULT_CURRENT_LATITUDE)))
            .andExpect(jsonPath("$.[*].currentLongitude").value(hasItem(DEFAULT_CURRENT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].hospitalizationDate").value(hasItem(DEFAULT_HOSPITALIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].healthCondition").value(hasItem(DEFAULT_HEALTH_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].ccmsUserId").value(hasItem(DEFAULT_CCMS_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].selfRegistered").value(hasItem(DEFAULT_SELF_REGISTERED.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomeIsolationsShouldNotBeFound(String filter) throws Exception {
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomeIsolationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHomeIsolations() throws Exception {
        // Get the homeIsolations
        restHomeIsolationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHomeIsolations() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();

        // Update the homeIsolations
        HomeIsolations updatedHomeIsolations = homeIsolationsRepository.findById(homeIsolations.getId()).get();
        // Disconnect from session so that the updates on updatedHomeIsolations are not directly saved in db
        em.detach(updatedHomeIsolations);
        updatedHomeIsolations
            .icmrId(UPDATED_ICMR_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .secondaryContactNo(UPDATED_SECONDARY_CONTACT_NO)
            .aadharCardNo(UPDATED_AADHAR_CARD_NO)
            .status(UPDATED_STATUS)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .stateId(UPDATED_STATE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .talukaId(UPDATED_TALUKA_ID)
            .cityId(UPDATED_CITY_ID)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .collectionDate(UPDATED_COLLECTION_DATE)
            .hospitalized(UPDATED_HOSPITALIZED)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .addressLatitude(UPDATED_ADDRESS_LATITUDE)
            .addressLongitude(UPDATED_ADDRESS_LONGITUDE)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .hospitalizationDate(UPDATED_HOSPITALIZATION_DATE)
            .healthCondition(UPDATED_HEALTH_CONDITION)
            .remarks(UPDATED_REMARKS)
            .ccmsUserId(UPDATED_CCMS_USER_ID)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(updatedHomeIsolations);

        restHomeIsolationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, homeIsolationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
        HomeIsolations testHomeIsolations = homeIsolationsList.get(homeIsolationsList.size() - 1);
        assertThat(testHomeIsolations.getIcmrId()).isEqualTo(UPDATED_ICMR_ID);
        assertThat(testHomeIsolations.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testHomeIsolations.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHomeIsolations.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testHomeIsolations.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHomeIsolations.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHomeIsolations.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testHomeIsolations.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testHomeIsolations.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testHomeIsolations.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testHomeIsolations.getSecondaryContactNo()).isEqualTo(UPDATED_SECONDARY_CONTACT_NO);
        assertThat(testHomeIsolations.getAadharCardNo()).isEqualTo(UPDATED_AADHAR_CARD_NO);
        assertThat(testHomeIsolations.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHomeIsolations.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testHomeIsolations.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testHomeIsolations.getStateId()).isEqualTo(UPDATED_STATE_ID);
        assertThat(testHomeIsolations.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testHomeIsolations.getTalukaId()).isEqualTo(UPDATED_TALUKA_ID);
        assertThat(testHomeIsolations.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testHomeIsolations.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHomeIsolations.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testHomeIsolations.getCollectionDate()).isEqualTo(UPDATED_COLLECTION_DATE);
        assertThat(testHomeIsolations.getHospitalized()).isEqualTo(UPDATED_HOSPITALIZED);
        assertThat(testHomeIsolations.getHospitalId()).isEqualTo(UPDATED_HOSPITAL_ID);
        assertThat(testHomeIsolations.getAddressLatitude()).isEqualTo(UPDATED_ADDRESS_LATITUDE);
        assertThat(testHomeIsolations.getAddressLongitude()).isEqualTo(UPDATED_ADDRESS_LONGITUDE);
        assertThat(testHomeIsolations.getCurrentLatitude()).isEqualTo(UPDATED_CURRENT_LATITUDE);
        assertThat(testHomeIsolations.getCurrentLongitude()).isEqualTo(UPDATED_CURRENT_LONGITUDE);
        assertThat(testHomeIsolations.getHospitalizationDate()).isEqualTo(UPDATED_HOSPITALIZATION_DATE);
        assertThat(testHomeIsolations.getHealthCondition()).isEqualTo(UPDATED_HEALTH_CONDITION);
        assertThat(testHomeIsolations.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testHomeIsolations.getCcmsUserId()).isEqualTo(UPDATED_CCMS_USER_ID);
        assertThat(testHomeIsolations.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testHomeIsolations.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testHomeIsolations.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, homeIsolationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHomeIsolationsWithPatch() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();

        // Update the homeIsolations using partial update
        HomeIsolations partialUpdatedHomeIsolations = new HomeIsolations();
        partialUpdatedHomeIsolations.setId(homeIsolations.getId());

        partialUpdatedHomeIsolations
            .icmrId(UPDATED_ICMR_ID)
            .lastName(UPDATED_LAST_NAME)
            .longitude(UPDATED_LONGITUDE)
            .email(UPDATED_EMAIL)
            .mobileNo(UPDATED_MOBILE_NO)
            .gender(UPDATED_GENDER)
            .stateId(UPDATED_STATE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .cityId(UPDATED_CITY_ID)
            .pincode(UPDATED_PINCODE)
            .collectionDate(UPDATED_COLLECTION_DATE)
            .addressLatitude(UPDATED_ADDRESS_LATITUDE)
            .addressLongitude(UPDATED_ADDRESS_LONGITUDE)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .healthCondition(UPDATED_HEALTH_CONDITION)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastModified(UPDATED_LAST_MODIFIED);

        restHomeIsolationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomeIsolations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomeIsolations))
            )
            .andExpect(status().isOk());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
        HomeIsolations testHomeIsolations = homeIsolationsList.get(homeIsolationsList.size() - 1);
        assertThat(testHomeIsolations.getIcmrId()).isEqualTo(UPDATED_ICMR_ID);
        assertThat(testHomeIsolations.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testHomeIsolations.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHomeIsolations.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testHomeIsolations.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHomeIsolations.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHomeIsolations.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testHomeIsolations.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testHomeIsolations.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testHomeIsolations.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
        assertThat(testHomeIsolations.getSecondaryContactNo()).isEqualTo(DEFAULT_SECONDARY_CONTACT_NO);
        assertThat(testHomeIsolations.getAadharCardNo()).isEqualTo(DEFAULT_AADHAR_CARD_NO);
        assertThat(testHomeIsolations.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHomeIsolations.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testHomeIsolations.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testHomeIsolations.getStateId()).isEqualTo(UPDATED_STATE_ID);
        assertThat(testHomeIsolations.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testHomeIsolations.getTalukaId()).isEqualTo(DEFAULT_TALUKA_ID);
        assertThat(testHomeIsolations.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testHomeIsolations.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHomeIsolations.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testHomeIsolations.getCollectionDate()).isEqualTo(UPDATED_COLLECTION_DATE);
        assertThat(testHomeIsolations.getHospitalized()).isEqualTo(DEFAULT_HOSPITALIZED);
        assertThat(testHomeIsolations.getHospitalId()).isEqualTo(DEFAULT_HOSPITAL_ID);
        assertThat(testHomeIsolations.getAddressLatitude()).isEqualTo(UPDATED_ADDRESS_LATITUDE);
        assertThat(testHomeIsolations.getAddressLongitude()).isEqualTo(UPDATED_ADDRESS_LONGITUDE);
        assertThat(testHomeIsolations.getCurrentLatitude()).isEqualTo(UPDATED_CURRENT_LATITUDE);
        assertThat(testHomeIsolations.getCurrentLongitude()).isEqualTo(UPDATED_CURRENT_LONGITUDE);
        assertThat(testHomeIsolations.getHospitalizationDate()).isEqualTo(DEFAULT_HOSPITALIZATION_DATE);
        assertThat(testHomeIsolations.getHealthCondition()).isEqualTo(UPDATED_HEALTH_CONDITION);
        assertThat(testHomeIsolations.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testHomeIsolations.getCcmsUserId()).isEqualTo(DEFAULT_CCMS_USER_ID);
        assertThat(testHomeIsolations.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testHomeIsolations.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testHomeIsolations.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateHomeIsolationsWithPatch() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();

        // Update the homeIsolations using partial update
        HomeIsolations partialUpdatedHomeIsolations = new HomeIsolations();
        partialUpdatedHomeIsolations.setId(homeIsolations.getId());

        partialUpdatedHomeIsolations
            .icmrId(UPDATED_ICMR_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .mobileNo(UPDATED_MOBILE_NO)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .secondaryContactNo(UPDATED_SECONDARY_CONTACT_NO)
            .aadharCardNo(UPDATED_AADHAR_CARD_NO)
            .status(UPDATED_STATUS)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .stateId(UPDATED_STATE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .talukaId(UPDATED_TALUKA_ID)
            .cityId(UPDATED_CITY_ID)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .collectionDate(UPDATED_COLLECTION_DATE)
            .hospitalized(UPDATED_HOSPITALIZED)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .addressLatitude(UPDATED_ADDRESS_LATITUDE)
            .addressLongitude(UPDATED_ADDRESS_LONGITUDE)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .hospitalizationDate(UPDATED_HOSPITALIZATION_DATE)
            .healthCondition(UPDATED_HEALTH_CONDITION)
            .remarks(UPDATED_REMARKS)
            .ccmsUserId(UPDATED_CCMS_USER_ID)
            .selfRegistered(UPDATED_SELF_REGISTERED)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restHomeIsolationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomeIsolations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomeIsolations))
            )
            .andExpect(status().isOk());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
        HomeIsolations testHomeIsolations = homeIsolationsList.get(homeIsolationsList.size() - 1);
        assertThat(testHomeIsolations.getIcmrId()).isEqualTo(UPDATED_ICMR_ID);
        assertThat(testHomeIsolations.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testHomeIsolations.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHomeIsolations.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testHomeIsolations.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHomeIsolations.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHomeIsolations.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testHomeIsolations.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testHomeIsolations.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testHomeIsolations.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testHomeIsolations.getSecondaryContactNo()).isEqualTo(UPDATED_SECONDARY_CONTACT_NO);
        assertThat(testHomeIsolations.getAadharCardNo()).isEqualTo(UPDATED_AADHAR_CARD_NO);
        assertThat(testHomeIsolations.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHomeIsolations.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testHomeIsolations.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testHomeIsolations.getStateId()).isEqualTo(UPDATED_STATE_ID);
        assertThat(testHomeIsolations.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testHomeIsolations.getTalukaId()).isEqualTo(UPDATED_TALUKA_ID);
        assertThat(testHomeIsolations.getCityId()).isEqualTo(UPDATED_CITY_ID);
        assertThat(testHomeIsolations.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHomeIsolations.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testHomeIsolations.getCollectionDate()).isEqualTo(UPDATED_COLLECTION_DATE);
        assertThat(testHomeIsolations.getHospitalized()).isEqualTo(UPDATED_HOSPITALIZED);
        assertThat(testHomeIsolations.getHospitalId()).isEqualTo(UPDATED_HOSPITAL_ID);
        assertThat(testHomeIsolations.getAddressLatitude()).isEqualTo(UPDATED_ADDRESS_LATITUDE);
        assertThat(testHomeIsolations.getAddressLongitude()).isEqualTo(UPDATED_ADDRESS_LONGITUDE);
        assertThat(testHomeIsolations.getCurrentLatitude()).isEqualTo(UPDATED_CURRENT_LATITUDE);
        assertThat(testHomeIsolations.getCurrentLongitude()).isEqualTo(UPDATED_CURRENT_LONGITUDE);
        assertThat(testHomeIsolations.getHospitalizationDate()).isEqualTo(UPDATED_HOSPITALIZATION_DATE);
        assertThat(testHomeIsolations.getHealthCondition()).isEqualTo(UPDATED_HEALTH_CONDITION);
        assertThat(testHomeIsolations.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testHomeIsolations.getCcmsUserId()).isEqualTo(UPDATED_CCMS_USER_ID);
        assertThat(testHomeIsolations.getSelfRegistered()).isEqualTo(UPDATED_SELF_REGISTERED);
        assertThat(testHomeIsolations.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testHomeIsolations.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, homeIsolationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHomeIsolations() throws Exception {
        int databaseSizeBeforeUpdate = homeIsolationsRepository.findAll().size();
        homeIsolations.setId(count.incrementAndGet());

        // Create the HomeIsolations
        HomeIsolationsDTO homeIsolationsDTO = homeIsolationsMapper.toDto(homeIsolations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomeIsolationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homeIsolationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomeIsolations in the database
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHomeIsolations() throws Exception {
        // Initialize the database
        homeIsolationsRepository.saveAndFlush(homeIsolations);

        int databaseSizeBeforeDelete = homeIsolationsRepository.findAll().size();

        // Delete the homeIsolations
        restHomeIsolationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, homeIsolations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HomeIsolations> homeIsolationsList = homeIsolationsRepository.findAll();
        assertThat(homeIsolationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
