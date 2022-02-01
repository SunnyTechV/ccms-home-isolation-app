package com.tecgvg.ccms.hi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tecgvg.ccms.hi.IntegrationTest;
import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import com.tecgvg.ccms.hi.domain.Questions;
import com.tecgvg.ccms.hi.domain.QuestionsOptions;
import com.tecgvg.ccms.hi.domain.enumeration.QuestionType;
import com.tecgvg.ccms.hi.repository.QuestionsRepository;
import com.tecgvg.ccms.hi.service.criteria.QuestionsCriteria;
import com.tecgvg.ccms.hi.service.dto.QuestionsDTO;
import com.tecgvg.ccms.hi.service.mapper.QuestionsMapper;
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
 * Integration tests for the {@link QuestionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionsResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_DESC = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_DESC = "BBBBBBBBBB";

    private static final QuestionType DEFAULT_QUESTION_TYPE = QuestionType.FREETEXT;
    private static final QuestionType UPDATED_QUESTION_TYPE = QuestionType.MULTISELECT;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
            .question(DEFAULT_QUESTION)
            .questionDesc(DEFAULT_QUESTION_DESC)
            .questionType(DEFAULT_QUESTION_TYPE)
            .active(DEFAULT_ACTIVE)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return questions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createUpdatedEntity(EntityManager em) {
        Questions questions = new Questions()
            .question(UPDATED_QUESTION)
            .questionDesc(UPDATED_QUESTION_DESC)
            .questionType(UPDATED_QUESTION_TYPE)
            .active(UPDATED_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return questions;
    }

    @BeforeEach
    public void initTest() {
        questions = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();
        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testQuestions.getQuestionDesc()).isEqualTo(DEFAULT_QUESTION_DESC);
        assertThat(testQuestions.getQuestionType()).isEqualTo(DEFAULT_QUESTION_TYPE);
        assertThat(testQuestions.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testQuestions.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testQuestions.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createQuestionsWithExistingId() throws Exception {
        // Create the Questions with an existing ID
        questions.setId(1L);
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].questionDesc").value(hasItem(DEFAULT_QUESTION_DESC)))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL_ID, questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.questionDesc").value(DEFAULT_QUESTION_DESC))
            .andExpect(jsonPath("$.questionType").value(DEFAULT_QUESTION_TYPE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        Long id = questions.getId();

        defaultQuestionsShouldBeFound("id.equals=" + id);
        defaultQuestionsShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionsShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question equals to DEFAULT_QUESTION
        defaultQuestionsShouldBeFound("question.equals=" + DEFAULT_QUESTION);

        // Get all the questionsList where question equals to UPDATED_QUESTION
        defaultQuestionsShouldNotBeFound("question.equals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question not equals to DEFAULT_QUESTION
        defaultQuestionsShouldNotBeFound("question.notEquals=" + DEFAULT_QUESTION);

        // Get all the questionsList where question not equals to UPDATED_QUESTION
        defaultQuestionsShouldBeFound("question.notEquals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question in DEFAULT_QUESTION or UPDATED_QUESTION
        defaultQuestionsShouldBeFound("question.in=" + DEFAULT_QUESTION + "," + UPDATED_QUESTION);

        // Get all the questionsList where question equals to UPDATED_QUESTION
        defaultQuestionsShouldNotBeFound("question.in=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question is not null
        defaultQuestionsShouldBeFound("question.specified=true");

        // Get all the questionsList where question is null
        defaultQuestionsShouldNotBeFound("question.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question contains DEFAULT_QUESTION
        defaultQuestionsShouldBeFound("question.contains=" + DEFAULT_QUESTION);

        // Get all the questionsList where question contains UPDATED_QUESTION
        defaultQuestionsShouldNotBeFound("question.contains=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionNotContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where question does not contain DEFAULT_QUESTION
        defaultQuestionsShouldNotBeFound("question.doesNotContain=" + DEFAULT_QUESTION);

        // Get all the questionsList where question does not contain UPDATED_QUESTION
        defaultQuestionsShouldBeFound("question.doesNotContain=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc equals to DEFAULT_QUESTION_DESC
        defaultQuestionsShouldBeFound("questionDesc.equals=" + DEFAULT_QUESTION_DESC);

        // Get all the questionsList where questionDesc equals to UPDATED_QUESTION_DESC
        defaultQuestionsShouldNotBeFound("questionDesc.equals=" + UPDATED_QUESTION_DESC);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc not equals to DEFAULT_QUESTION_DESC
        defaultQuestionsShouldNotBeFound("questionDesc.notEquals=" + DEFAULT_QUESTION_DESC);

        // Get all the questionsList where questionDesc not equals to UPDATED_QUESTION_DESC
        defaultQuestionsShouldBeFound("questionDesc.notEquals=" + UPDATED_QUESTION_DESC);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc in DEFAULT_QUESTION_DESC or UPDATED_QUESTION_DESC
        defaultQuestionsShouldBeFound("questionDesc.in=" + DEFAULT_QUESTION_DESC + "," + UPDATED_QUESTION_DESC);

        // Get all the questionsList where questionDesc equals to UPDATED_QUESTION_DESC
        defaultQuestionsShouldNotBeFound("questionDesc.in=" + UPDATED_QUESTION_DESC);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc is not null
        defaultQuestionsShouldBeFound("questionDesc.specified=true");

        // Get all the questionsList where questionDesc is null
        defaultQuestionsShouldNotBeFound("questionDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc contains DEFAULT_QUESTION_DESC
        defaultQuestionsShouldBeFound("questionDesc.contains=" + DEFAULT_QUESTION_DESC);

        // Get all the questionsList where questionDesc contains UPDATED_QUESTION_DESC
        defaultQuestionsShouldNotBeFound("questionDesc.contains=" + UPDATED_QUESTION_DESC);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionDescNotContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionDesc does not contain DEFAULT_QUESTION_DESC
        defaultQuestionsShouldNotBeFound("questionDesc.doesNotContain=" + DEFAULT_QUESTION_DESC);

        // Get all the questionsList where questionDesc does not contain UPDATED_QUESTION_DESC
        defaultQuestionsShouldBeFound("questionDesc.doesNotContain=" + UPDATED_QUESTION_DESC);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionType equals to DEFAULT_QUESTION_TYPE
        defaultQuestionsShouldBeFound("questionType.equals=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionsList where questionType equals to UPDATED_QUESTION_TYPE
        defaultQuestionsShouldNotBeFound("questionType.equals=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionType not equals to DEFAULT_QUESTION_TYPE
        defaultQuestionsShouldNotBeFound("questionType.notEquals=" + DEFAULT_QUESTION_TYPE);

        // Get all the questionsList where questionType not equals to UPDATED_QUESTION_TYPE
        defaultQuestionsShouldBeFound("questionType.notEquals=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionType in DEFAULT_QUESTION_TYPE or UPDATED_QUESTION_TYPE
        defaultQuestionsShouldBeFound("questionType.in=" + DEFAULT_QUESTION_TYPE + "," + UPDATED_QUESTION_TYPE);

        // Get all the questionsList where questionType equals to UPDATED_QUESTION_TYPE
        defaultQuestionsShouldNotBeFound("questionType.in=" + UPDATED_QUESTION_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where questionType is not null
        defaultQuestionsShouldBeFound("questionType.specified=true");

        // Get all the questionsList where questionType is null
        defaultQuestionsShouldNotBeFound("questionType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where active equals to DEFAULT_ACTIVE
        defaultQuestionsShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the questionsList where active equals to UPDATED_ACTIVE
        defaultQuestionsShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllQuestionsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where active not equals to DEFAULT_ACTIVE
        defaultQuestionsShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the questionsList where active not equals to UPDATED_ACTIVE
        defaultQuestionsShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllQuestionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultQuestionsShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the questionsList where active equals to UPDATED_ACTIVE
        defaultQuestionsShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllQuestionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where active is not null
        defaultQuestionsShouldBeFound("active.specified=true");

        // Get all the questionsList where active is null
        defaultQuestionsShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultQuestionsShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionsShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultQuestionsShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the questionsList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultQuestionsShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultQuestionsShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the questionsList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultQuestionsShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModified is not null
        defaultQuestionsShouldBeFound("lastModified.specified=true");

        // Get all the questionsList where lastModified is null
        defaultQuestionsShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultQuestionsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultQuestionsShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionsList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the questionsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy is not null
        defaultQuestionsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the questionsList where lastModifiedBy is null
        defaultQuestionsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultQuestionsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultQuestionsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the questionsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultQuestionsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllQuestionsByQuestionsOptionsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);
        QuestionsOptions questionsOptions;
        if (TestUtil.findAll(em, QuestionsOptions.class).isEmpty()) {
            questionsOptions = QuestionsOptionsResourceIT.createEntity(em);
            em.persist(questionsOptions);
            em.flush();
        } else {
            questionsOptions = TestUtil.findAll(em, QuestionsOptions.class).get(0);
        }
        em.persist(questionsOptions);
        em.flush();
        questions.addQuestionsOptions(questionsOptions);
        questionsRepository.saveAndFlush(questions);
        Long questionsOptionsId = questionsOptions.getId();

        // Get all the questionsList where questionsOptions equals to questionsOptionsId
        defaultQuestionsShouldBeFound("questionsOptionsId.equals=" + questionsOptionsId);

        // Get all the questionsList where questionsOptions equals to (questionsOptionsId + 1)
        defaultQuestionsShouldNotBeFound("questionsOptionsId.equals=" + (questionsOptionsId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByAssessmentAnswersIsEqualToSomething() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);
        AssessmentAnswers assessmentAnswers;
        if (TestUtil.findAll(em, AssessmentAnswers.class).isEmpty()) {
            assessmentAnswers = AssessmentAnswersResourceIT.createEntity(em);
            em.persist(assessmentAnswers);
            em.flush();
        } else {
            assessmentAnswers = TestUtil.findAll(em, AssessmentAnswers.class).get(0);
        }
        em.persist(assessmentAnswers);
        em.flush();
        questions.setAssessmentAnswers(assessmentAnswers);
        assessmentAnswers.setQuestions(questions);
        questionsRepository.saveAndFlush(questions);
        Long assessmentAnswersId = assessmentAnswers.getId();

        // Get all the questionsList where assessmentAnswers equals to assessmentAnswersId
        defaultQuestionsShouldBeFound("assessmentAnswersId.equals=" + assessmentAnswersId);

        // Get all the questionsList where assessmentAnswers equals to (assessmentAnswersId + 1)
        defaultQuestionsShouldNotBeFound("assessmentAnswersId.equals=" + (assessmentAnswersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionsShouldBeFound(String filter) throws Exception {
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].questionDesc").value(hasItem(DEFAULT_QUESTION_DESC)))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionsShouldNotBeFound(String filter) throws Exception {
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findById(questions.getId()).get();
        // Disconnect from session so that the updates on updatedQuestions are not directly saved in db
        em.detach(updatedQuestions);
        updatedQuestions
            .question(UPDATED_QUESTION)
            .questionDesc(UPDATED_QUESTION_DESC)
            .questionType(UPDATED_QUESTION_TYPE)
            .active(UPDATED_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        QuestionsDTO questionsDTO = questionsMapper.toDto(updatedQuestions);

        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestions.getQuestionDesc()).isEqualTo(UPDATED_QUESTION_DESC);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testQuestions.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testQuestions.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestions.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions
            .question(UPDATED_QUESTION)
            .questionType(UPDATED_QUESTION_TYPE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestions.getQuestionDesc()).isEqualTo(DEFAULT_QUESTION_DESC);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testQuestions.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testQuestions.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestions.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateQuestionsWithPatch() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions using partial update
        Questions partialUpdatedQuestions = new Questions();
        partialUpdatedQuestions.setId(questions.getId());

        partialUpdatedQuestions
            .question(UPDATED_QUESTION)
            .questionDesc(UPDATED_QUESTION_DESC)
            .questionType(UPDATED_QUESTION_TYPE)
            .active(UPDATED_ACTIVE)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestions))
            )
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestions.getQuestionDesc()).isEqualTo(UPDATED_QUESTION_DESC);
        assertThat(testQuestions.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testQuestions.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testQuestions.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testQuestions.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();
        questions.setId(count.incrementAndGet());

        // Create the Questions
        QuestionsDTO questionsDTO = questionsMapper.toDto(questions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Delete the questions
        restQuestionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, questions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
