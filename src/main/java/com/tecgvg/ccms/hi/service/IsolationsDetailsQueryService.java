package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.*; // for static metamodels
import com.tecgvg.ccms.hi.domain.IsolationsDetails;
import com.tecgvg.ccms.hi.repository.IsolationsDetailsRepository;
import com.tecgvg.ccms.hi.service.criteria.IsolationsDetailsCriteria;
import com.tecgvg.ccms.hi.service.dto.IsolationsDetailsDTO;
import com.tecgvg.ccms.hi.service.mapper.IsolationsDetailsMapper;
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
 * Service for executing complex queries for {@link IsolationsDetails} entities in the database.
 * The main input is a {@link IsolationsDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IsolationsDetailsDTO} or a {@link Page} of {@link IsolationsDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IsolationsDetailsQueryService extends QueryService<IsolationsDetails> {

    private final Logger log = LoggerFactory.getLogger(IsolationsDetailsQueryService.class);

    private final IsolationsDetailsRepository isolationsDetailsRepository;

    private final IsolationsDetailsMapper isolationsDetailsMapper;

    public IsolationsDetailsQueryService(
        IsolationsDetailsRepository isolationsDetailsRepository,
        IsolationsDetailsMapper isolationsDetailsMapper
    ) {
        this.isolationsDetailsRepository = isolationsDetailsRepository;
        this.isolationsDetailsMapper = isolationsDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link IsolationsDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IsolationsDetailsDTO> findByCriteria(IsolationsDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IsolationsDetails> specification = createSpecification(criteria);
        return isolationsDetailsMapper.toDto(isolationsDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IsolationsDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IsolationsDetailsDTO> findByCriteria(IsolationsDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IsolationsDetails> specification = createSpecification(criteria);
        return isolationsDetailsRepository.findAll(specification, page).map(isolationsDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IsolationsDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IsolationsDetails> specification = createSpecification(criteria);
        return isolationsDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link IsolationsDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IsolationsDetails> createSpecification(IsolationsDetailsCriteria criteria) {
        Specification<IsolationsDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IsolationsDetails_.id));
            }
            if (criteria.getIsolationStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIsolationStartDate(), IsolationsDetails_.isolationStartDate));
            }
            if (criteria.getIsolationEndDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIsolationEndDate(), IsolationsDetails_.isolationEndDate));
            }
            if (criteria.getReferredDrName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferredDrName(), IsolationsDetails_.referredDrName));
            }
            if (criteria.getReferredDrMobile() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReferredDrMobile(), IsolationsDetails_.referredDrMobile));
            }
            if (criteria.getPrescriptionUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPrescriptionUrl(), IsolationsDetails_.prescriptionUrl));
            }
            if (criteria.getReportUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportUrl(), IsolationsDetails_.reportUrl));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), IsolationsDetails_.remarks));
            }
            if (criteria.getSelfRegistered() != null) {
                specification = specification.and(buildSpecification(criteria.getSelfRegistered(), IsolationsDetails_.selfRegistered));
            }
            if (criteria.getLastAssessment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastAssessment(), IsolationsDetails_.lastAssessment));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), IsolationsDetails_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), IsolationsDetails_.lastModifiedBy));
            }
            if (criteria.getHomeIsolationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHomeIsolationsId(),
                            root -> root.join(IsolationsDetails_.homeIsolations, JoinType.LEFT).get(HomeIsolations_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
