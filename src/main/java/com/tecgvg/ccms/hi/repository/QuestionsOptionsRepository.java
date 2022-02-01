package com.tecgvg.ccms.hi.repository;

import com.tecgvg.ccms.hi.domain.QuestionsOptions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionsOptions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionsOptionsRepository extends JpaRepository<QuestionsOptions, Long>, JpaSpecificationExecutor<QuestionsOptions> {}
