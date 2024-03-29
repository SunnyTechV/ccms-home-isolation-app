package com.tecgvg.ccms.hi.service.dto;

import com.tecgvg.ccms.hi.domain.enumeration.QuestionType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.tecgvg.ccms.hi.domain.Questions} entity.
 */
public class QuestionsDTO implements Serializable {

    private Long id;

    private String question;

    private String questionDesc;

    private QuestionType questionType;

    private Boolean active;

    private Instant lastModified;

    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionsDTO)) {
            return false;
        }

        QuestionsDTO questionsDTO = (QuestionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionsDTO{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", questionDesc='" + getQuestionDesc() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            ", active='" + getActive() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
