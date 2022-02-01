package com.tecgvg.ccms.hi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tecgvg.ccms.hi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssessmentAnswersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssessmentAnswers.class);
        AssessmentAnswers assessmentAnswers1 = new AssessmentAnswers();
        assessmentAnswers1.setId(1L);
        AssessmentAnswers assessmentAnswers2 = new AssessmentAnswers();
        assessmentAnswers2.setId(assessmentAnswers1.getId());
        assertThat(assessmentAnswers1).isEqualTo(assessmentAnswers2);
        assessmentAnswers2.setId(2L);
        assertThat(assessmentAnswers1).isNotEqualTo(assessmentAnswers2);
        assessmentAnswers1.setId(null);
        assertThat(assessmentAnswers1).isNotEqualTo(assessmentAnswers2);
    }
}
