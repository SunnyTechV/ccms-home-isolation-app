package com.tecgvg.ccms.hi.service.criteria;

import com.tecgvg.ccms.hi.domain.enumeration.QuestionType;
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
 * Criteria class for the {@link com.tecgvg.ccms.hi.domain.Questions} entity. This class is used
 * in {@link com.tecgvg.ccms.hi.web.rest.QuestionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestionType
     */
    public static class QuestionTypeFilter extends Filter<QuestionType> {

        public QuestionTypeFilter() {}

        public QuestionTypeFilter(QuestionTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestionTypeFilter copy() {
            return new QuestionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter question;

    private StringFilter questionDesc;

    private QuestionTypeFilter questionType;

    private BooleanFilter active;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter questionsOptionsId;

    private LongFilter assessmentAnswersId;

    private Boolean distinct;

    public QuestionsCriteria() {}

    public QuestionsCriteria(QuestionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.question = other.question == null ? null : other.question.copy();
        this.questionDesc = other.questionDesc == null ? null : other.questionDesc.copy();
        this.questionType = other.questionType == null ? null : other.questionType.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.questionsOptionsId = other.questionsOptionsId == null ? null : other.questionsOptionsId.copy();
        this.assessmentAnswersId = other.assessmentAnswersId == null ? null : other.assessmentAnswersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionsCriteria copy() {
        return new QuestionsCriteria(this);
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

    public StringFilter getQuestion() {
        return question;
    }

    public StringFilter question() {
        if (question == null) {
            question = new StringFilter();
        }
        return question;
    }

    public void setQuestion(StringFilter question) {
        this.question = question;
    }

    public StringFilter getQuestionDesc() {
        return questionDesc;
    }

    public StringFilter questionDesc() {
        if (questionDesc == null) {
            questionDesc = new StringFilter();
        }
        return questionDesc;
    }

    public void setQuestionDesc(StringFilter questionDesc) {
        this.questionDesc = questionDesc;
    }

    public QuestionTypeFilter getQuestionType() {
        return questionType;
    }

    public QuestionTypeFilter questionType() {
        if (questionType == null) {
            questionType = new QuestionTypeFilter();
        }
        return questionType;
    }

    public void setQuestionType(QuestionTypeFilter questionType) {
        this.questionType = questionType;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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

    public LongFilter getQuestionsOptionsId() {
        return questionsOptionsId;
    }

    public LongFilter questionsOptionsId() {
        if (questionsOptionsId == null) {
            questionsOptionsId = new LongFilter();
        }
        return questionsOptionsId;
    }

    public void setQuestionsOptionsId(LongFilter questionsOptionsId) {
        this.questionsOptionsId = questionsOptionsId;
    }

    public LongFilter getAssessmentAnswersId() {
        return assessmentAnswersId;
    }

    public LongFilter assessmentAnswersId() {
        if (assessmentAnswersId == null) {
            assessmentAnswersId = new LongFilter();
        }
        return assessmentAnswersId;
    }

    public void setAssessmentAnswersId(LongFilter assessmentAnswersId) {
        this.assessmentAnswersId = assessmentAnswersId;
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
        final QuestionsCriteria that = (QuestionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(question, that.question) &&
            Objects.equals(questionDesc, that.questionDesc) &&
            Objects.equals(questionType, that.questionType) &&
            Objects.equals(active, that.active) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(questionsOptionsId, that.questionsOptionsId) &&
            Objects.equals(assessmentAnswersId, that.assessmentAnswersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            question,
            questionDesc,
            questionType,
            active,
            lastModified,
            lastModifiedBy,
            questionsOptionsId,
            assessmentAnswersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (question != null ? "question=" + question + ", " : "") +
            (questionDesc != null ? "questionDesc=" + questionDesc + ", " : "") +
            (questionType != null ? "questionType=" + questionType + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (questionsOptionsId != null ? "questionsOptionsId=" + questionsOptionsId + ", " : "") +
            (assessmentAnswersId != null ? "assessmentAnswersId=" + assessmentAnswersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
