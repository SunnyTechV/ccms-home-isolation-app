package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.*; // for static metamodels
import com.tecgvg.ccms.hi.domain.Questions;
import com.tecgvg.ccms.hi.repository.QuestionsRepository;
import com.tecgvg.ccms.hi.service.criteria.QuestionsCriteria;
import com.tecgvg.ccms.hi.service.dto.QuestionsDTO;
import com.tecgvg.ccms.hi.service.mapper.QuestionsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Questions} entities in the database.
 * The main input is a {@link QuestionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionsDTO} or a {@link Page} of {@link QuestionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionsQueryService extends QueryService<Questions> {

    private final Logger log = LoggerFactory.getLogger(QuestionsQueryService.class);

    private final QuestionsRepository questionsRepository;

    private final QuestionsMapper questionsMapper;

    public QuestionsQueryService(QuestionsRepository questionsRepository, QuestionsMapper questionsMapper) {
        this.questionsRepository = questionsRepository;
        this.questionsMapper = questionsMapper;
    }

    /**
     * Return a {@link List} of {@link QuestionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionsDTO> findByCriteria(QuestionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Questions> specification = createSpecification(criteria);
        return questionsMapper.toDto(questionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionsDTO> findByCriteria(QuestionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Questions> specification = createSpecification(criteria);
        return questionsRepository.findAll(specification, page).map(questionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Questions> specification = createSpecification(criteria);
        return questionsRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Questions> createSpecification(QuestionsCriteria criteria) {
        Specification<Questions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Questions_.id));
            }
            if (criteria.getQuestion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestion(), Questions_.question));
            }
            if (criteria.getQuestionDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestionDesc(), Questions_.questionDesc));
            }
            if (criteria.getQuestionType() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionType(), Questions_.questionType));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Questions_.active));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), Questions_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Questions_.lastModifiedBy));
            }
            if (criteria.getQuestionsOptionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuestionsOptionsId(),
                            root -> root.join(Questions_.questionsOptions, JoinType.LEFT).get(QuestionsOptions_.id)
                        )
                    );
            }
            if (criteria.getAssessmentAnswersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssessmentAnswersId(),
                            root -> root.join(Questions_.assessmentAnswers, JoinType.LEFT).get(AssessmentAnswers_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
