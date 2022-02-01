package com.tecgvg.ccms.hi.web.rest;

import com.tecgvg.ccms.hi.repository.IsolationsDetailsRepository;
import com.tecgvg.ccms.hi.service.IsolationsDetailsQueryService;
import com.tecgvg.ccms.hi.service.IsolationsDetailsService;
import com.tecgvg.ccms.hi.service.criteria.IsolationsDetailsCriteria;
import com.tecgvg.ccms.hi.service.dto.IsolationsDetailsDTO;
import com.tecgvg.ccms.hi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tecgvg.ccms.hi.domain.IsolationsDetails}.
 */
@RestController
@RequestMapping("/api")
public class IsolationsDetailsResource {

    private final Logger log = LoggerFactory.getLogger(IsolationsDetailsResource.class);

    private static final String ENTITY_NAME = "isolationsDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsolationsDetailsService isolationsDetailsService;

    private final IsolationsDetailsRepository isolationsDetailsRepository;

    private final IsolationsDetailsQueryService isolationsDetailsQueryService;

    public IsolationsDetailsResource(
        IsolationsDetailsService isolationsDetailsService,
        IsolationsDetailsRepository isolationsDetailsRepository,
        IsolationsDetailsQueryService isolationsDetailsQueryService
    ) {
        this.isolationsDetailsService = isolationsDetailsService;
        this.isolationsDetailsRepository = isolationsDetailsRepository;
        this.isolationsDetailsQueryService = isolationsDetailsQueryService;
    }

    /**
     * {@code POST  /isolations-details} : Create a new isolationsDetails.
     *
     * @param isolationsDetailsDTO the isolationsDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isolationsDetailsDTO, or with status {@code 400 (Bad Request)} if the isolationsDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/isolations-details")
    public ResponseEntity<IsolationsDetailsDTO> createIsolationsDetails(@RequestBody IsolationsDetailsDTO isolationsDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save IsolationsDetails : {}", isolationsDetailsDTO);
        if (isolationsDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new isolationsDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsolationsDetailsDTO result = isolationsDetailsService.save(isolationsDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/isolations-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /isolations-details/:id} : Updates an existing isolationsDetails.
     *
     * @param id the id of the isolationsDetailsDTO to save.
     * @param isolationsDetailsDTO the isolationsDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isolationsDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the isolationsDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isolationsDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/isolations-details/{id}")
    public ResponseEntity<IsolationsDetailsDTO> updateIsolationsDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IsolationsDetailsDTO isolationsDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IsolationsDetails : {}, {}", id, isolationsDetailsDTO);
        if (isolationsDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isolationsDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isolationsDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsolationsDetailsDTO result = isolationsDetailsService.save(isolationsDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isolationsDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /isolations-details/:id} : Partial updates given fields of an existing isolationsDetails, field will ignore if it is null
     *
     * @param id the id of the isolationsDetailsDTO to save.
     * @param isolationsDetailsDTO the isolationsDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isolationsDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the isolationsDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isolationsDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isolationsDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/isolations-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsolationsDetailsDTO> partialUpdateIsolationsDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IsolationsDetailsDTO isolationsDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IsolationsDetails partially : {}, {}", id, isolationsDetailsDTO);
        if (isolationsDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isolationsDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isolationsDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsolationsDetailsDTO> result = isolationsDetailsService.partialUpdate(isolationsDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isolationsDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /isolations-details} : get all the isolationsDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isolationsDetails in body.
     */
    @GetMapping("/isolations-details")
    public ResponseEntity<List<IsolationsDetailsDTO>> getAllIsolationsDetails(
        IsolationsDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get IsolationsDetails by criteria: {}", criteria);
        Page<IsolationsDetailsDTO> page = isolationsDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /isolations-details/count} : count all the isolationsDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/isolations-details/count")
    public ResponseEntity<Long> countIsolationsDetails(IsolationsDetailsCriteria criteria) {
        log.debug("REST request to count IsolationsDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(isolationsDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /isolations-details/:id} : get the "id" isolationsDetails.
     *
     * @param id the id of the isolationsDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isolationsDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/isolations-details/{id}")
    public ResponseEntity<IsolationsDetailsDTO> getIsolationsDetails(@PathVariable Long id) {
        log.debug("REST request to get IsolationsDetails : {}", id);
        Optional<IsolationsDetailsDTO> isolationsDetailsDTO = isolationsDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isolationsDetailsDTO);
    }

    /**
     * {@code DELETE  /isolations-details/:id} : delete the "id" isolationsDetails.
     *
     * @param id the id of the isolationsDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/isolations-details/{id}")
    public ResponseEntity<Void> deleteIsolationsDetails(@PathVariable Long id) {
        log.debug("REST request to delete IsolationsDetails : {}", id);
        isolationsDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
