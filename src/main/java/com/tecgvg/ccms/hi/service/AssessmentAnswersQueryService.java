package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.*; // for static metamodels
import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import com.tecgvg.ccms.hi.repository.AssessmentAnswersRepository;
import com.tecgvg.ccms.hi.service.criteria.AssessmentAnswersCriteria;
import com.tecgvg.ccms.hi.service.dto.AssessmentAnswersDTO;
import com.tecgvg.ccms.hi.service.mapper.AssessmentAnswersMapper;
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
 * Service for executing complex queries for {@link AssessmentAnswers} entities in the database.
 * The main input is a {@link AssessmentAnswersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssessmentAnswersDTO} or a {@link Page} of {@link AssessmentAnswersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssessmentAnswersQueryService extends QueryService<AssessmentAnswers> {

    private final Logger log = LoggerFactory.getLogger(AssessmentAnswersQueryService.class);

    private final AssessmentAnswersRepository assessmentAnswersRepository;

    private final AssessmentAnswersMapper assessmentAnswersMapper;

    public AssessmentAnswersQueryService(
        AssessmentAnswersRepository assessmentAnswersRepository,
        AssessmentAnswersMapper assessmentAnswersMapper
    ) {
        this.assessmentAnswersRepository = assessmentAnswersRepository;
        this.assessmentAnswersMapper = assessmentAnswersMapper;
    }

    /**
     * Return a {@link List} of {@link AssessmentAnswersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssessmentAnswersDTO> findByCriteria(AssessmentAnswersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssessmentAnswers> specification = createSpecification(criteria);
        return assessmentAnswersMapper.toDto(assessmentAnswersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssessmentAnswersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssessmentAnswersDTO> findByCriteria(AssessmentAnswersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssessmentAnswers> specification = createSpecification(criteria);
        return assessmentAnswersRepository.findAll(specification, page).map(assessmentAnswersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssessmentAnswersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssessmentAnswers> specification = createSpecification(criteria);
        return assessmentAnswersRepository.count(specification);
    }

    /**
     * Function to convert {@link AssessmentAnswersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssessmentAnswers> createSpecification(AssessmentAnswersCriteria criteria) {
        Specification<AssessmentAnswers> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssessmentAnswers_.id));
            }
            if (criteria.getAnswer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnswer(), AssessmentAnswers_.answer));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), AssessmentAnswers_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), AssessmentAnswers_.lastModifiedBy));
            }
            if (criteria.getQuestionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuestionsId(),
                            root -> root.join(AssessmentAnswers_.questions, JoinType.LEFT).get(Questions_.id)
                        )
                    );
            }
            if (criteria.getAssessmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssessmentId(),
                            root -> root.join(AssessmentAnswers_.assessment, JoinType.LEFT).get(Assessment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
