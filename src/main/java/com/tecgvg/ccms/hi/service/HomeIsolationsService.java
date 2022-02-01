package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.HomeIsolations;
import com.tecgvg.ccms.hi.repository.HomeIsolationsRepository;
import com.tecgvg.ccms.hi.service.dto.HomeIsolationsDTO;
import com.tecgvg.ccms.hi.service.mapper.HomeIsolationsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HomeIsolations}.
 */
@Service
@Transactional
public class HomeIsolationsService {

    private final Logger log = LoggerFactory.getLogger(HomeIsolationsService.class);

    private final HomeIsolationsRepository homeIsolationsRepository;

    private final HomeIsolationsMapper homeIsolationsMapper;

    public HomeIsolationsService(HomeIsolationsRepository homeIsolationsRepository, HomeIsolationsMapper homeIsolationsMapper) {
        this.homeIsolationsRepository = homeIsolationsRepository;
        this.homeIsolationsMapper = homeIsolationsMapper;
    }

    /**
     * Save a homeIsolations.
     *
     * @param homeIsolationsDTO the entity to save.
     * @return the persisted entity.
     */
    public HomeIsolationsDTO save(HomeIsolationsDTO homeIsolationsDTO) {
        log.debug("Request to save HomeIsolations : {}", homeIsolationsDTO);
        HomeIsolations homeIsolations = homeIsolationsMapper.toEntity(homeIsolationsDTO);
        homeIsolations = homeIsolationsRepository.save(homeIsolations);
        return homeIsolationsMapper.toDto(homeIsolations);
    }

    /**
     * Partially update a homeIsolations.
     *
     * @param homeIsolationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HomeIsolationsDTO> partialUpdate(HomeIsolationsDTO homeIsolationsDTO) {
        log.debug("Request to partially update HomeIsolations : {}", homeIsolationsDTO);

        return homeIsolationsRepository
            .findById(homeIsolationsDTO.getId())
            .map(existingHomeIsolations -> {
                homeIsolationsMapper.partialUpdate(existingHomeIsolations, homeIsolationsDTO);

                return existingHomeIsolations;
            })
            .map(homeIsolationsRepository::save)
            .map(homeIsolationsMapper::toDto);
    }

    /**
     * Get all the homeIsolations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeIsolationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HomeIsolations");
        return homeIsolationsRepository.findAll(pageable).map(homeIsolationsMapper::toDto);
    }

    /**
     * Get one homeIsolations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HomeIsolationsDTO> findOne(Long id) {
        log.debug("Request to get HomeIsolations : {}", id);
        return homeIsolationsRepository.findById(id).map(homeIsolationsMapper::toDto);
    }

    /**
     * Delete the homeIsolations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HomeIsolations : {}", id);
        homeIsolationsRepository.deleteById(id);
    }
}
