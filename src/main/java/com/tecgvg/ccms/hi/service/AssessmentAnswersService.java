package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import com.tecgvg.ccms.hi.repository.AssessmentAnswersRepository;
import com.tecgvg.ccms.hi.service.dto.AssessmentAnswersDTO;
import com.tecgvg.ccms.hi.service.mapper.AssessmentAnswersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssessmentAnswers}.
 */
@Service
@Transactional
public class AssessmentAnswersService {

    private final Logger log = LoggerFactory.getLogger(AssessmentAnswersService.class);

    private final AssessmentAnswersRepository assessmentAnswersRepository;

    private final AssessmentAnswersMapper assessmentAnswersMapper;

    public AssessmentAnswersService(
        AssessmentAnswersRepository assessmentAnswersRepository,
        AssessmentAnswersMapper assessmentAnswersMapper
    ) {
        this.assessmentAnswersRepository = assessmentAnswersRepository;
        this.assessmentAnswersMapper = assessmentAnswersMapper;
    }

    /**
     * Save a assessmentAnswers.
     *
     * @param assessmentAnswersDTO the entity to save.
     * @return the persisted entity.
     */
    public AssessmentAnswersDTO save(AssessmentAnswersDTO assessmentAnswersDTO) {
        log.debug("Request to save AssessmentAnswers : {}", assessmentAnswersDTO);
        AssessmentAnswers assessmentAnswers = assessmentAnswersMapper.toEntity(assessmentAnswersDTO);
        assessmentAnswers = assessmentAnswersRepository.save(assessmentAnswers);
        return assessmentAnswersMapper.toDto(assessmentAnswers);
    }

    /**
     * Partially update a assessmentAnswers.
     *
     * @param assessmentAnswersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssessmentAnswersDTO> partialUpdate(AssessmentAnswersDTO assessmentAnswersDTO) {
        log.debug("Request to partially update AssessmentAnswers : {}", assessmentAnswersDTO);

        return assessmentAnswersRepository
            .findById(assessmentAnswersDTO.getId())
            .map(existingAssessmentAnswers -> {
                assessmentAnswersMapper.partialUpdate(existingAssessmentAnswers, assessmentAnswersDTO);

                return existingAssessmentAnswers;
            })
            .map(assessmentAnswersRepository::save)
            .map(assessmentAnswersMapper::toDto);
    }

    /**
     * Get all the assessmentAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssessmentAnswersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssessmentAnswers");
        return assessmentAnswersRepository.findAll(pageable).map(assessmentAnswersMapper::toDto);
    }

    /**
     * Get one assessmentAnswers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssessmentAnswersDTO> findOne(Long id) {
        log.debug("Request to get AssessmentAnswers : {}", id);
        return assessmentAnswersRepository.findById(id).map(assessmentAnswersMapper::toDto);
    }

    /**
     * Delete the assessmentAnswers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssessmentAnswers : {}", id);
        assessmentAnswersRepository.deleteById(id);
    }
}
