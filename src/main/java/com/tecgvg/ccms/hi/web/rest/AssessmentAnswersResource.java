package com.tecgvg.ccms.hi.web.rest;

import com.tecgvg.ccms.hi.repository.AssessmentAnswersRepository;
import com.tecgvg.ccms.hi.service.AssessmentAnswersQueryService;
import com.tecgvg.ccms.hi.service.AssessmentAnswersService;
import com.tecgvg.ccms.hi.service.criteria.AssessmentAnswersCriteria;
import com.tecgvg.ccms.hi.service.dto.AssessmentAnswersDTO;
import com.tecgvg.ccms.hi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.tecgvg.ccms.hi.domain.AssessmentAnswers}.
 */
@RestController
@RequestMapping("/api")
public class AssessmentAnswersResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentAnswersResource.class);

    private static final String ENTITY_NAME = "assessmentAnswers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssessmentAnswersService assessmentAnswersService;

    private final AssessmentAnswersRepository assessmentAnswersRepository;

    private final AssessmentAnswersQueryService assessmentAnswersQueryService;

    public AssessmentAnswersResource(
        AssessmentAnswersService assessmentAnswersService,
        AssessmentAnswersRepository assessmentAnswersRepository,
        AssessmentAnswersQueryService assessmentAnswersQueryService
    ) {
        this.assessmentAnswersService = assessmentAnswersService;
        this.assessmentAnswersRepository = assessmentAnswersRepository;
        this.assessmentAnswersQueryService = assessmentAnswersQueryService;
    }

    /**
     * {@code POST  /assessment-answers} : Create a new assessmentAnswers.
     *
     * @param assessmentAnswersDTO the assessmentAnswersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assessmentAnswersDTO, or with status {@code 400 (Bad Request)} if the assessmentAnswers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assessment-answers")
    public ResponseEntity<AssessmentAnswersDTO> createAssessmentAnswers(@RequestBody AssessmentAnswersDTO assessmentAnswersDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssessmentAnswers : {}", assessmentAnswersDTO);
        if (assessmentAnswersDTO.getId() != null) {
            throw new BadRequestAlertException("A new assessmentAnswers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssessmentAnswersDTO result = assessmentAnswersService.save(assessmentAnswersDTO);
        return ResponseEntity
            .created(new URI("/api/assessment-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assessment-answers/:id} : Updates an existing assessmentAnswers.
     *
     * @param id the id of the assessmentAnswersDTO to save.
     * @param assessmentAnswersDTO the assessmentAnswersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessmentAnswersDTO,
     * or with status {@code 400 (Bad Request)} if the assessmentAnswersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assessmentAnswersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assessment-answers/{id}")
    public ResponseEntity<AssessmentAnswersDTO> updateAssessmentAnswers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssessmentAnswersDTO assessmentAnswersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssessmentAnswers : {}, {}", id, assessmentAnswersDTO);
        if (assessmentAnswersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessmentAnswersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentAnswersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssessmentAnswersDTO result = assessmentAnswersService.save(assessmentAnswersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessmentAnswersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assessment-answers/:id} : Partial updates given fields of an existing assessmentAnswers, field will ignore if it is null
     *
     * @param id the id of the assessmentAnswersDTO to save.
     * @param assessmentAnswersDTO the assessmentAnswersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessmentAnswersDTO,
     * or with status {@code 400 (Bad Request)} if the assessmentAnswersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assessmentAnswersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assessmentAnswersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assessment-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssessmentAnswersDTO> partialUpdateAssessmentAnswers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AssessmentAnswersDTO assessmentAnswersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssessmentAnswers partially : {}, {}", id, assessmentAnswersDTO);
        if (assessmentAnswersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessmentAnswersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentAnswersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssessmentAnswersDTO> result = assessmentAnswersService.partialUpdate(assessmentAnswersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessmentAnswersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assessment-answers} : get all the assessmentAnswers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assessmentAnswers in body.
     */
    @GetMapping("/assessment-answers")
    public ResponseEntity<List<AssessmentAnswersDTO>> getAllAssessmentAnswers(
        AssessmentAnswersCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssessmentAnswers by criteria: {}", criteria);
        Page<AssessmentAnswersDTO> page = assessmentAnswersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assessment-answers/count} : count all the assessmentAnswers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assessment-answers/count")
    public ResponseEntity<Long> countAssessmentAnswers(AssessmentAnswersCriteria criteria) {
        log.debug("REST request to count AssessmentAnswers by criteria: {}", criteria);
        return ResponseEntity.ok().body(assessmentAnswersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assessment-answers/:id} : get the "id" assessmentAnswers.
     *
     * @param id the id of the assessmentAnswersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assessmentAnswersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assessment-answers/{id}")
    public ResponseEntity<AssessmentAnswersDTO> getAssessmentAnswers(@PathVariable Long id) {
        log.debug("REST request to get AssessmentAnswers : {}", id);
        Optional<AssessmentAnswersDTO> assessmentAnswersDTO = assessmentAnswersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assessmentAnswersDTO);
    }

    /**
     * {@code DELETE  /assessment-answers/:id} : delete the "id" assessmentAnswers.
     *
     * @param id the id of the assessmentAnswersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assessment-answers/{id}")
    public ResponseEntity<Void> deleteAssessmentAnswers(@PathVariable Long id) {
        log.debug("REST request to delete AssessmentAnswers : {}", id);
        assessmentAnswersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
