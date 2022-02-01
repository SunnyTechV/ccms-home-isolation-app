package com.tecgvg.ccms.hi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecgvg.ccms.hi.domain.enumeration.QuestionType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Questions.
 */
@Entity
@Table(name = "questions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "question_desc")
    private String questionDesc;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @OneToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<QuestionsOptions> questionsOptions = new HashSet<>();

    @JsonIgnoreProperties(value = { "questions", "assessment" }, allowSetters = true)
    @OneToOne(mappedBy = "questions")
    private AssessmentAnswers assessmentAnswers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public Questions question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionDesc() {
        return this.questionDesc;
    }

    public Questions questionDesc(String questionDesc) {
        this.setQuestionDesc(questionDesc);
        return this;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public QuestionType getQuestionType() {
        return this.questionType;
    }

    public Questions questionType(QuestionType questionType) {
        this.setQuestionType(questionType);
        return this;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Questions active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Questions lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Questions lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<QuestionsOptions> getQuestionsOptions() {
        return this.questionsOptions;
    }

    public void setQuestionsOptions(Set<QuestionsOptions> questionsOptions) {
        if (this.questionsOptions != null) {
            this.questionsOptions.forEach(i -> i.setQuestions(null));
        }
        if (questionsOptions != null) {
            questionsOptions.forEach(i -> i.setQuestions(this));
        }
        this.questionsOptions = questionsOptions;
    }

    public Questions questionsOptions(Set<QuestionsOptions> questionsOptions) {
        this.setQuestionsOptions(questionsOptions);
        return this;
    }

    public Questions addQuestionsOptions(QuestionsOptions questionsOptions) {
        this.questionsOptions.add(questionsOptions);
        questionsOptions.setQuestions(this);
        return this;
    }

    public Questions removeQuestionsOptions(QuestionsOptions questionsOptions) {
        this.questionsOptions.remove(questionsOptions);
        questionsOptions.setQuestions(null);
        return this;
    }

    public AssessmentAnswers getAssessmentAnswers() {
        return this.assessmentAnswers;
    }

    public void setAssessmentAnswers(AssessmentAnswers assessmentAnswers) {
        if (this.assessmentAnswers != null) {
            this.assessmentAnswers.setQuestions(null);
        }
        if (assessmentAnswers != null) {
            assessmentAnswers.setQuestions(this);
        }
        this.assessmentAnswers = assessmentAnswers;
    }

    public Questions assessmentAnswers(AssessmentAnswers assessmentAnswers) {
        this.setAssessmentAnswers(assessmentAnswers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questions)) {
            return false;
        }
        return id != null && id.equals(((Questions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questions{" +
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
