package com.tecgvg.ccms.hi.service;

import com.tecgvg.ccms.hi.domain.IsolationsDetails;
import com.tecgvg.ccms.hi.repository.IsolationsDetailsRepository;
import com.tecgvg.ccms.hi.service.dto.IsolationsDetailsDTO;
import com.tecgvg.ccms.hi.service.mapper.IsolationsDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsolationsDetails}.
 */
@Service
@Transactional
public class IsolationsDetailsService {

    private final Logger log = LoggerFactory.getLogger(IsolationsDetailsService.class);

    private final IsolationsDetailsRepository isolationsDetailsRepository;

    private final IsolationsDetailsMapper isolationsDetailsMapper;

    public IsolationsDetailsService(
        IsolationsDetailsRepository isolationsDetailsRepository,
        IsolationsDetailsMapper isolationsDetailsMapper
    ) {
        this.isolationsDetailsRepository = isolationsDetailsRepository;
        this.isolationsDetailsMapper = isolationsDetailsMapper;
    }

    /**
     * Save a isolationsDetails.
     *
     * @param isolationsDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public IsolationsDetailsDTO save(IsolationsDetailsDTO isolationsDetailsDTO) {
        log.debug("Request to save IsolationsDetails : {}", isolationsDetailsDTO);
        IsolationsDetails isolationsDetails = isolationsDetailsMapper.toEntity(isolationsDetailsDTO);
        isolationsDetails = isolationsDetailsRepository.save(isolationsDetails);
        return isolationsDetailsMapper.toDto(isolationsDetails);
    }

    /**
     * Partially update a isolationsDetails.
     *
     * @param isolationsDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IsolationsDetailsDTO> partialUpdate(IsolationsDetailsDTO isolationsDetailsDTO) {
        log.debug("Request to partially update IsolationsDetails : {}", isolationsDetailsDTO);

        return isolationsDetailsRepository
            .findById(isolationsDetailsDTO.getId())
            .map(existingIsolationsDetails -> {
                isolationsDetailsMapper.partialUpdate(existingIsolationsDetails, isolationsDetailsDTO);

                return existingIsolationsDetails;
            })
            .map(isolationsDetailsRepository::save)
            .map(isolationsDetailsMapper::toDto);
    }

    /**
     * Get all the isolationsDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IsolationsDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsolationsDetails");
        return isolationsDetailsRepository.findAll(pageable).map(isolationsDetailsMapper::toDto);
    }

    /**
     *  Get all the isolationsDetails where HomeIsolations is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IsolationsDetailsDTO> findAllWhereHomeIsolationsIsNull() {
        log.debug("Request to get all isolationsDetails where HomeIsolations is null");
        return StreamSupport
            .stream(isolationsDetailsRepository.findAll().spliterator(), false)
            .filter(isolationsDetails -> isolationsDetails.getHomeIsolations() == null)
            .map(isolationsDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one isolationsDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IsolationsDetailsDTO> findOne(Long id) {
        log.debug("Request to get IsolationsDetails : {}", id);
        return isolationsDetailsRepository.findById(id).map(isolationsDetailsMapper::toDto);
    }

    /**
     * Delete the isolationsDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IsolationsDetails : {}", id);
        isolationsDetailsRepository.deleteById(id);
    }
}
