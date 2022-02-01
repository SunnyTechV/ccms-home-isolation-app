package com.tecgvg.ccms.hi.repository;

import com.tecgvg.ccms.hi.domain.AssessmentAnswers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssessmentAnswers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssessmentAnswersRepository extends JpaRepository<AssessmentAnswers, Long>, JpaSpecificationExecutor<AssessmentAnswers> {}
