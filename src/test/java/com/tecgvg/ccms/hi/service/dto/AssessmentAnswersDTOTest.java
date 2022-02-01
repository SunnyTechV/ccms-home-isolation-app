package com.tecgvg.ccms.hi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssessmentAnswersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssessmentAnswersDTO.class);
        AssessmentAnswersDTO assessmentAnswersDTO1 = new AssessmentAnswersDTO();
        assessmentAnswersDTO1.setId(1L);
        AssessmentAnswersDTO assessmentAnswersDTO2 = new AssessmentAnswersDTO();
        assertThat(assessmentAnswersDTO1).isNotEqualTo(assessmentAnswersDTO2);
        assessmentAnswersDTO2.setId(assessmentAnswersDTO1.getId());
        assertThat(assessmentAnswersDTO1).isEqualTo(assessmentAnswersDTO2);
        assessmentAnswersDTO2.setId(2L);
        assertThat(assessmentAnswersDTO1).isNotEqualTo(assessmentAnswersDTO2);
        assessmentAnswersDTO1.setId(null);
        assertThat(assessmentAnswersDTO1).isNotEqualTo(assessmentAnswersDTO2);
    }
}
