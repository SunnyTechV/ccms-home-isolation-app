package com.tecgvg.ccms.hi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.tecgvg.ccms.hi.domain.AssessmentAnswers} entity.
 */
public class AssessmentAnswersDTO implements Serializable {

    private Long id;

    private String answer;

    private Instant lastModified;

    private String lastModifiedBy;

    private QuestionsDTO questions;

    private AssessmentDTO assessment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public QuestionsDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsDTO questions) {
        this.questions = questions;
    }

    public AssessmentDTO getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDTO assessment) {
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssessmentAnswersDTO)) {
            return false;
        }

        AssessmentAnswersDTO assessmentAnswersDTO = (AssessmentAnswersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assessmentAnswersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssessmentAnswersDTO{" +
            "id=" + getId() +
            ", answer='" + getAnswer() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", questions=" + getQuestions() +
            ", assessment=" + getAssessment() +
            "}";
    }
}
