package com.tecgvg.ccms.hi.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.tecgvg.ccms.hi.domain.AssessmentAnswers} entity. This class is used
 * in {@link com.tecgvg.ccms.hi.web.rest.AssessmentAnswersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assessment-answers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssessmentAnswersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter answer;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter questionsId;

    private LongFilter assessmentId;

    private Boolean distinct;

    public AssessmentAnswersCriteria() {}

    public AssessmentAnswersCriteria(AssessmentAnswersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.answer = other.answer == null ? null : other.answer.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.questionsId = other.questionsId == null ? null : other.questionsId.copy();
        this.assessmentId = other.assessmentId == null ? null : other.assessmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssessmentAnswersCriteria copy() {
        return new AssessmentAnswersCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAnswer() {
        return answer;
    }

    public StringFilter answer() {
        if (answer == null) {
            answer = new StringFilter();
        }
        return answer;
    }

    public void setAnswer(StringFilter answer) {
        this.answer = answer;
    }

    public InstantFilter getLastModified() {
        return lastModified;
    }

    public InstantFilter lastModified() {
        if (lastModified == null) {
            lastModified = new InstantFilter();
        }
        return lastModified;
    }

    public void setLastModified(InstantFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LongFilter getQuestionsId() {
        return questionsId;
    }

    public LongFilter questionsId() {
        if (questionsId == null) {
            questionsId = new LongFilter();
        }
        return questionsId;
    }

    public void setQuestionsId(LongFilter questionsId) {
        this.questionsId = questionsId;
    }

    public LongFilter getAssessmentId() {
        return assessmentId;
    }

    public LongFilter assessmentId() {
        if (assessmentId == null) {
            assessmentId = new LongFilter();
        }
        return assessmentId;
    }

    public void setAssessmentId(LongFilter assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssessmentAnswersCriteria that = (AssessmentAnswersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(answer, that.answer) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(questionsId, that.questionsId) &&
            Objects.equals(assessmentId, that.assessmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, lastModified, lastModifiedBy, questionsId, assessmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssessmentAnswersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (answer != null ? "answer=" + answer + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (questionsId != null ? "questionsId=" + questionsId + ", " : "") +
            (assessmentId != null ? "assessmentId=" + assessmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
