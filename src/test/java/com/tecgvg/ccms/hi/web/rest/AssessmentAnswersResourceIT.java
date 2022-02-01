package com.tecgvg.ccms.hi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tecgvg.ccms.hi.IntegrationTest;
import com.tecgvg.ccms.hi.domain.Assessment;
import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import com.tecgvg.ccms.hi.domain.Questions;
import com.tecgvg.ccms.hi.repository.AssessmentAnswersRepository;
import com.tecgvg.ccms.hi.service.criteria.AssessmentAnswersCriteria;
import com.tecgvg.ccms.hi.service.dto.AssessmentAnswersDTO;
import com.tecgvg.ccms.hi.service.mapper.AssessmentAnswersMapper;
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
 * Integration tests for the {@link AssessmentAnswersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssessmentAnswersResourceIT {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/assessment-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssessmentAnswersRepository assessmentAnswersRepository;

    @Autowired
    private AssessmentAnswersMapper assessmentAnswersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssessmentAnswersMockMvc;

    private AssessmentAnswers assessmentAnswers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssessmentAnswers createEntity(EntityManager em) {
        AssessmentAnswers assessmentAnswers = new AssessmentAnswers()
            .answer(DEFAULT_ANSWER)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return assessmentAnswers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssessmentAnswers createUpdatedEntity(EntityManager em) {
        AssessmentAnswers assessmentAnswers = new AssessmentAnswers()
            .answer(UPDATED_ANSWER)
            .lastModified(UPDATED_LAST_MODIFIED)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return assessmentAnswers;
    }

    @BeforeEach
    public void initTest() {
        assessmentAnswers = createEntity(em);
    }

    @Test
    @Transactional
    void createAssessmentAnswers() throws Exception {
        int databaseSizeBeforeCreate = assessmentAnswersRepository.findAll().size();
        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);
        restAssessmentAnswersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeCreate + 1);
        AssessmentAnswers testAssessmentAnswers = assessmentAnswersList.get(assessmentAnswersList.size() - 1);
        assertThat(testAssessmentAnswers.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testAssessmentAnswers.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testAssessmentAnswers.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void createAssessmentAnswersWithExistingId() throws Exception {
        // Create the AssessmentAnswers with an existing ID
        assessmentAnswers.setId(1L);
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        int databaseSizeBeforeCreate = assessmentAnswersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssessmentAnswersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswers() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessmentAnswers.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }

    @Test
    @Transactional
    void getAssessmentAnswers() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get the assessmentAnswers
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL_ID, assessmentAnswers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assessmentAnswers.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    void getAssessmentAnswersByIdFiltering() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        Long id = assessmentAnswers.getId();

        defaultAssessmentAnswersShouldBeFound("id.equals=" + id);
        defaultAssessmentAnswersShouldNotBeFound("id.notEquals=" + id);

        defaultAssessmentAnswersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssessmentAnswersShouldNotBeFound("id.greaterThan=" + id);

        defaultAssessmentAnswersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssessmentAnswersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer equals to DEFAULT_ANSWER
        defaultAssessmentAnswersShouldBeFound("answer.equals=" + DEFAULT_ANSWER);

        // Get all the assessmentAnswersList where answer equals to UPDATED_ANSWER
        defaultAssessmentAnswersShouldNotBeFound("answer.equals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer not equals to DEFAULT_ANSWER
        defaultAssessmentAnswersShouldNotBeFound("answer.notEquals=" + DEFAULT_ANSWER);

        // Get all the assessmentAnswersList where answer not equals to UPDATED_ANSWER
        defaultAssessmentAnswersShouldBeFound("answer.notEquals=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer in DEFAULT_ANSWER or UPDATED_ANSWER
        defaultAssessmentAnswersShouldBeFound("answer.in=" + DEFAULT_ANSWER + "," + UPDATED_ANSWER);

        // Get all the assessmentAnswersList where answer equals to UPDATED_ANSWER
        defaultAssessmentAnswersShouldNotBeFound("answer.in=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer is not null
        defaultAssessmentAnswersShouldBeFound("answer.specified=true");

        // Get all the assessmentAnswersList where answer is null
        defaultAssessmentAnswersShouldNotBeFound("answer.specified=false");
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerContainsSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer contains DEFAULT_ANSWER
        defaultAssessmentAnswersShouldBeFound("answer.contains=" + DEFAULT_ANSWER);

        // Get all the assessmentAnswersList where answer contains UPDATED_ANSWER
        defaultAssessmentAnswersShouldNotBeFound("answer.contains=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAnswerNotContainsSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where answer does not contain DEFAULT_ANSWER
        defaultAssessmentAnswersShouldNotBeFound("answer.doesNotContain=" + DEFAULT_ANSWER);

        // Get all the assessmentAnswersList where answer does not contain UPDATED_ANSWER
        defaultAssessmentAnswersShouldBeFound("answer.doesNotContain=" + UPDATED_ANSWER);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultAssessmentAnswersShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the assessmentAnswersList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAssessmentAnswersShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultAssessmentAnswersShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the assessmentAnswersList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultAssessmentAnswersShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultAssessmentAnswersShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the assessmentAnswersList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultAssessmentAnswersShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModified is not null
        defaultAssessmentAnswersShouldBeFound("lastModified.specified=true");

        // Get all the assessmentAnswersList where lastModified is null
        defaultAssessmentAnswersShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assessmentAnswersList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assessmentAnswersList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the assessmentAnswersList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy is not null
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.specified=true");

        // Get all the assessmentAnswersList where lastModifiedBy is null
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assessmentAnswersList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        // Get all the assessmentAnswersList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assessmentAnswersList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAssessmentAnswersShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByQuestionsIsEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);
        Questions questions;
        if (TestUtil.findAll(em, Questions.class).isEmpty()) {
            questions = QuestionsResourceIT.createEntity(em);
            em.persist(questions);
            em.flush();
        } else {
            questions = TestUtil.findAll(em, Questions.class).get(0);
        }
        em.persist(questions);
        em.flush();
        assessmentAnswers.setQuestions(questions);
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);
        Long questionsId = questions.getId();

        // Get all the assessmentAnswersList where questions equals to questionsId
        defaultAssessmentAnswersShouldBeFound("questionsId.equals=" + questionsId);

        // Get all the assessmentAnswersList where questions equals to (questionsId + 1)
        defaultAssessmentAnswersShouldNotBeFound("questionsId.equals=" + (questionsId + 1));
    }

    @Test
    @Transactional
    void getAllAssessmentAnswersByAssessmentIsEqualToSomething() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);
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
        assessmentAnswers.setAssessment(assessment);
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);
        Long assessmentId = assessment.getId();

        // Get all the assessmentAnswersList where assessment equals to assessmentId
        defaultAssessmentAnswersShouldBeFound("assessmentId.equals=" + assessmentId);

        // Get all the assessmentAnswersList where assessment equals to (assessmentId + 1)
        defaultAssessmentAnswersShouldNotBeFound("assessmentId.equals=" + (assessmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssessmentAnswersShouldBeFound(String filter) throws Exception {
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessmentAnswers.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));

        // Check, that the count call also returns 1
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssessmentAnswersShouldNotBeFound(String filter) throws Exception {
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssessmentAnswersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssessmentAnswers() throws Exception {
        // Get the assessmentAnswers
        restAssessmentAnswersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssessmentAnswers() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();

        // Update the assessmentAnswers
        AssessmentAnswers updatedAssessmentAnswers = assessmentAnswersRepository.findById(assessmentAnswers.getId()).get();
        // Disconnect from session so that the updates on updatedAssessmentAnswers are not directly saved in db
        em.detach(updatedAssessmentAnswers);
        updatedAssessmentAnswers.answer(UPDATED_ANSWER).lastModified(UPDATED_LAST_MODIFIED).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(updatedAssessmentAnswers);

        restAssessmentAnswersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessmentAnswersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
        AssessmentAnswers testAssessmentAnswers = assessmentAnswersList.get(assessmentAnswersList.size() - 1);
        assertThat(testAssessmentAnswers.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testAssessmentAnswers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAssessmentAnswers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void putNonExistingAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessmentAnswersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssessmentAnswersWithPatch() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();

        // Update the assessmentAnswers using partial update
        AssessmentAnswers partialUpdatedAssessmentAnswers = new AssessmentAnswers();
        partialUpdatedAssessmentAnswers.setId(assessmentAnswers.getId());

        partialUpdatedAssessmentAnswers.answer(UPDATED_ANSWER).lastModified(UPDATED_LAST_MODIFIED);

        restAssessmentAnswersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessmentAnswers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessmentAnswers))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
        AssessmentAnswers testAssessmentAnswers = assessmentAnswersList.get(assessmentAnswersList.size() - 1);
        assertThat(testAssessmentAnswers.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testAssessmentAnswers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAssessmentAnswers.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void fullUpdateAssessmentAnswersWithPatch() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();

        // Update the assessmentAnswers using partial update
        AssessmentAnswers partialUpdatedAssessmentAnswers = new AssessmentAnswers();
        partialUpdatedAssessmentAnswers.setId(assessmentAnswers.getId());

        partialUpdatedAssessmentAnswers.answer(UPDATED_ANSWER).lastModified(UPDATED_LAST_MODIFIED).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restAssessmentAnswersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessmentAnswers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessmentAnswers))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
        AssessmentAnswers testAssessmentAnswers = assessmentAnswersList.get(assessmentAnswersList.size() - 1);
        assertThat(testAssessmentAnswers.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testAssessmentAnswers.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testAssessmentAnswers.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assessmentAnswersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssessmentAnswers() throws Exception {
        int databaseSizeBeforeUpdate = assessmentAnswersRepository.findAll().size();
        assessmentAnswers.setId(count.incrementAndGet());

        // Create the AssessmentAnswers
        AssessmentAnswersDTO assessmentAnswersDTO = assessmentAnswersMapper.toDto(assessmentAnswers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentAnswersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentAnswersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssessmentAnswers in the database
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssessmentAnswers() throws Exception {
        // Initialize the database
        assessmentAnswersRepository.saveAndFlush(assessmentAnswers);

        int databaseSizeBeforeDelete = assessmentAnswersRepository.findAll().size();

        // Delete the assessmentAnswers
        restAssessmentAnswersMockMvc
            .perform(delete(ENTITY_API_URL_ID, assessmentAnswers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssessmentAnswers> assessmentAnswersList = assessmentAnswersRepository.findAll();
        assertThat(assessmentAnswersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
