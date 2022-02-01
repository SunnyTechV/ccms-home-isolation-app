package com.tecgvg.ccms.hi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssessmentAnswersMapperTest {

    private AssessmentAnswersMapper assessmentAnswersMapper;

    @BeforeEach
    public void setUp() {
        assessmentAnswersMapper = new AssessmentAnswersMapperImpl();
    }
}
