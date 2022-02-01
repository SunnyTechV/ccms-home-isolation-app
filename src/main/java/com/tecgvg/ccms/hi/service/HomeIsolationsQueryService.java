package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.*; // for static metamodels
import com.tecgvg.ccms.hi.domain.HomeIsolations;
import com.tecgvg.ccms.hi.repository.HomeIsolationsRepository;
import com.tecgvg.ccms.hi.service.criteria.HomeIsolationsCriteria;
import com.tecgvg.ccms.hi.service.dto.HomeIsolationsDTO;
import com.tecgvg.ccms.hi.service.mapper.HomeIsolationsMapper;
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
 * Service for executing complex queries for {@link HomeIsolations} entities in the database.
 * The main input is a {@link HomeIsolationsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomeIsolationsDTO} or a {@link Page} of {@link HomeIsolationsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomeIsolationsQueryService extends QueryService<HomeIsolations> {

    private final Logger log = LoggerFactory.getLogger(HomeIsolationsQueryService.class);

    private final HomeIsolationsRepository homeIsolationsRepository;

    private final HomeIsolationsMapper homeIsolationsMapper;

    public HomeIsolationsQueryService(HomeIsolationsRepository homeIsolationsRepository, HomeIsolationsMapper homeIsolationsMapper) {
        this.homeIsolationsRepository = homeIsolationsRepository;
        this.homeIsolationsMapper = homeIsolationsMapper;
    }

    /**
     * Return a {@link List} of {@link HomeIsolationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomeIsolationsDTO> findByCriteria(HomeIsolationsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HomeIsolations> specification = createSpecification(criteria);
        return homeIsolationsMapper.toDto(homeIsolationsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HomeIsolationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeIsolationsDTO> findByCriteria(HomeIsolationsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomeIsolations> specification = createSpecification(criteria);
        return homeIsolationsRepository.findAll(specification, page).map(homeIsolationsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomeIsolationsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HomeIsolations> specification = createSpecification(criteria);
        return homeIsolationsRepository.count(specification);
    }

    /**
     * Function to convert {@link HomeIsolationsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HomeIsolations> createSpecification(HomeIsolationsCriteria criteria) {
        Specification<HomeIsolations> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HomeIsolations_.id));
            }
            if (criteria.getIcmrId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIcmrId(), HomeIsolations_.icmrId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), HomeIsolations_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), HomeIsolations_.lastName));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), HomeIsolations_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), HomeIsolations_.longitude));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), HomeIsolations_.email));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), HomeIsolations_.imageUrl));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), HomeIsolations_.activated));
            }
            if (criteria.getMobileNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileNo(), HomeIsolations_.mobileNo));
            }
            if (criteria.getPasswordHash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPasswordHash(), HomeIsolations_.passwordHash));
            }
            if (criteria.getSecondaryContactNo() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSecondaryContactNo(), HomeIsolations_.secondaryContactNo));
            }
            if (criteria.getAadharCardNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAadharCardNo(), HomeIsolations_.aadharCardNo));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), HomeIsolations_.status));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAge(), HomeIsolations_.age));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGender(), HomeIsolations_.gender));
            }
            if (criteria.getStateId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStateId(), HomeIsolations_.stateId));
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDistrictId(), HomeIsolations_.districtId));
            }
            if (criteria.getTalukaId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTalukaId(), HomeIsolations_.talukaId));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCityId(), HomeIsolations_.cityId));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), HomeIsolations_.address));
            }
            if (criteria.getPincode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPincode(), HomeIsolations_.pincode));
            }
            if (criteria.getCollectionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCollectionDate(), HomeIsolations_.collectionDate));
            }
            if (criteria.getHospitalized() != null) {
                specification = specification.and(buildSpecification(criteria.getHospitalized(), HomeIsolations_.hospitalized));
            }
            if (criteria.getHospitalId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHospitalId(), HomeIsolations_.hospitalId));
            }
            if (criteria.getAddressLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLatitude(), HomeIsolations_.addressLatitude));
            }
            if (criteria.getAddressLongitude() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAddressLongitude(), HomeIsolations_.addressLongitude));
            }
            if (criteria.getCurrentLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrentLatitude(), HomeIsolations_.currentLatitude));
            }
            if (criteria.getCurrentLongitude() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCurrentLongitude(), HomeIsolations_.currentLongitude));
            }
            if (criteria.getHospitalizationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getHospitalizationDate(), HomeIsolations_.hospitalizationDate));
            }
            if (criteria.getHealthCondition() != null) {
                specification = specification.and(buildSpecification(criteria.getHealthCondition(), HomeIsolations_.healthCondition));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), HomeIsolations_.remarks));
            }
            if (criteria.getCcmsUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCcmsUserId(), HomeIsolations_.ccmsUserId));
            }
            if (criteria.getSelfRegistered() != null) {
                specification = specification.and(buildSpecification(criteria.getSelfRegistered(), HomeIsolations_.selfRegistered));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), HomeIsolations_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HomeIsolations_.lastModifiedBy));
            }
            if (criteria.getIsolationsDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIsolationsDetailsId(),
                            root -> root.join(HomeIsolations_.isolationsDetails, JoinType.LEFT).get(IsolationsDetails_.id)
                        )
                    );
            }
            if (criteria.getAssessmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssessmentId(),
                            root -> root.join(HomeIsolations_.assessments, JoinType.LEFT).get(Assessment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
