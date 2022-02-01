package com.tecgvg.ccms.hi.web.rest;

import com.tecgvg.ccms.hi.repository.HomeIsolationsRepository;
import com.tecgvg.ccms.hi.service.HomeIsolationsQueryService;
import com.tecgvg.ccms.hi.service.HomeIsolationsService;
import com.tecgvg.ccms.hi.service.criteria.HomeIsolationsCriteria;
import com.tecgvg.ccms.hi.service.dto.HomeIsolationsDTO;
import com.tecgvg.ccms.hi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.tecgvg.ccms.hi.domain.HomeIsolations}.
 */
@RestController
@RequestMapping("/api")
public class HomeIsolationsResource {

    private final Logger log = LoggerFactory.getLogger(HomeIsolationsResource.class);

    private static final String ENTITY_NAME = "homeIsolations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomeIsolationsService homeIsolationsService;

    private final HomeIsolationsRepository homeIsolationsRepository;

    private final HomeIsolationsQueryService homeIsolationsQueryService;

    public HomeIsolationsResource(
        HomeIsolationsService homeIsolationsService,
        HomeIsolationsRepository homeIsolationsRepository,
        HomeIsolationsQueryService homeIsolationsQueryService
    ) {
        this.homeIsolationsService = homeIsolationsService;
        this.homeIsolationsRepository = homeIsolationsRepository;
        this.homeIsolationsQueryService = homeIsolationsQueryService;
    }

    /**
     * {@code POST  /home-isolations} : Create a new homeIsolations.
     *
     * @param homeIsolationsDTO the homeIsolationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homeIsolationsDTO, or with status {@code 400 (Bad Request)} if the homeIsolations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/home-isolations")
    public ResponseEntity<HomeIsolationsDTO> createHomeIsolations(@Valid @RequestBody HomeIsolationsDTO homeIsolationsDTO)
        throws URISyntaxException {
        log.debug("REST request to save HomeIsolations : {}", homeIsolationsDTO);
        if (homeIsolationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new homeIsolations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HomeIsolationsDTO result = homeIsolationsService.save(homeIsolationsDTO);
        return ResponseEntity
            .created(new URI("/api/home-isolations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /home-isolations/:id} : Updates an existing homeIsolations.
     *
     * @param id the id of the homeIsolationsDTO to save.
     * @param homeIsolationsDTO the homeIsolationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeIsolationsDTO,
     * or with status {@code 400 (Bad Request)} if the homeIsolationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homeIsolationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/home-isolations/{id}")
    public ResponseEntity<HomeIsolationsDTO> updateHomeIsolations(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HomeIsolationsDTO homeIsolationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HomeIsolations : {}, {}", id, homeIsolationsDTO);
        if (homeIsolationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homeIsolationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homeIsolationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HomeIsolationsDTO result = homeIsolationsService.save(homeIsolationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homeIsolationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /home-isolations/:id} : Partial updates given fields of an existing homeIsolations, field will ignore if it is null
     *
     * @param id the id of the homeIsolationsDTO to save.
     * @param homeIsolationsDTO the homeIsolationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeIsolationsDTO,
     * or with status {@code 400 (Bad Request)} if the homeIsolationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the homeIsolationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the homeIsolationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/home-isolations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HomeIsolationsDTO> partialUpdateHomeIsolations(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HomeIsolationsDTO homeIsolationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HomeIsolations partially : {}, {}", id, homeIsolationsDTO);
        if (homeIsolationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homeIsolationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homeIsolationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HomeIsolationsDTO> result = homeIsolationsService.partialUpdate(homeIsolationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homeIsolationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /home-isolations} : get all the homeIsolations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homeIsolations in body.
     */
    @GetMapping("/home-isolations")
    public ResponseEntity<List<HomeIsolationsDTO>> getAllHomeIsolations(
        HomeIsolationsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HomeIsolations by criteria: {}", criteria);
        Page<HomeIsolationsDTO> page = homeIsolationsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /home-isolations/count} : count all the homeIsolations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/home-isolations/count")
    public ResponseEntity<Long> countHomeIsolations(HomeIsolationsCriteria criteria) {
        log.debug("REST request to count HomeIsolations by criteria: {}", criteria);
        return ResponseEntity.ok().body(homeIsolationsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /home-isolations/:id} : get the "id" homeIsolations.
     *
     * @param id the id of the homeIsolationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homeIsolationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/home-isolations/{id}")
    public ResponseEntity<HomeIsolationsDTO> getHomeIsolations(@PathVariable Long id) {
        log.debug("REST request to get HomeIsolations : {}", id);
        Optional<HomeIsolationsDTO> homeIsolationsDTO = homeIsolationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homeIsolationsDTO);
    }

    /**
     * {@code DELETE  /home-isolations/:id} : delete the "id" homeIsolations.
     *
     * @param id the id of the homeIsolationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/home-isolations/{id}")
    public ResponseEntity<Void> deleteHomeIsolations(@PathVariable Long id) {
        log.debug("REST request to delete HomeIsolations : {}", id);
        homeIsolationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
