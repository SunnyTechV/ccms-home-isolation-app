package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.QuestionsOptions;
import com.tecgvg.ccms.hi.repository.QuestionsOptionsRepository;
import com.tecgvg.ccms.hi.service.dto.QuestionsOptionsDTO;
import com.tecgvg.ccms.hi.service.mapper.QuestionsOptionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionsOptions}.
 */
@Service
@Transactional
public class QuestionsOptionsService {

    private final Logger log = LoggerFactory.getLogger(QuestionsOptionsService.class);

    private final QuestionsOptionsRepository questionsOptionsRepository;

    private final QuestionsOptionsMapper questionsOptionsMapper;

    public QuestionsOptionsService(QuestionsOptionsRepository questionsOptionsRepository, QuestionsOptionsMapper questionsOptionsMapper) {
        this.questionsOptionsRepository = questionsOptionsRepository;
        this.questionsOptionsMapper = questionsOptionsMapper;
    }

    /**
     * Save a questionsOptions.
     *
     * @param questionsOptionsDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionsOptionsDTO save(QuestionsOptionsDTO questionsOptionsDTO) {
        log.debug("Request to save QuestionsOptions : {}", questionsOptionsDTO);
        QuestionsOptions questionsOptions = questionsOptionsMapper.toEntity(questionsOptionsDTO);
        questionsOptions = questionsOptionsRepository.save(questionsOptions);
        return questionsOptionsMapper.toDto(questionsOptions);
    }

    /**
     * Partially update a questionsOptions.
     *
     * @param questionsOptionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionsOptionsDTO> partialUpdate(QuestionsOptionsDTO questionsOptionsDTO) {
        log.debug("Request to partially update QuestionsOptions : {}", questionsOptionsDTO);

        return questionsOptionsRepository
            .findById(questionsOptionsDTO.getId())
            .map(existingQuestionsOptions -> {
                questionsOptionsMapper.partialUpdate(existingQuestionsOptions, questionsOptionsDTO);

                return existingQuestionsOptions;
            })
            .map(questionsOptionsRepository::save)
            .map(questionsOptionsMapper::toDto);
    }

    /**
     * Get all the questionsOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionsOptionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionsOptions");
        return questionsOptionsRepository.findAll(pageable).map(questionsOptionsMapper::toDto);
    }

    /**
     * Get one questionsOptions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionsOptionsDTO> findOne(Long id) {
        log.debug("Request to get QuestionsOptions : {}", id);
        return questionsOptionsRepository.findById(id).map(questionsOptionsMapper::toDto);
    }

    /**
     * Delete the questionsOptions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionsOptions : {}", id);
        questionsOptionsRepository.deleteById(id);
    }
}
