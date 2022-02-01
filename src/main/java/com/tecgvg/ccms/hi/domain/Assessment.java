package com.tecgvg.ccms.hi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assessment.
 */
@Entity
@Table(name = "assessment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "assessment_date", nullable = false)
    private Instant assessmentDate;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @OneToMany(mappedBy = "assessment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "assessment" }, allowSetters = true)
    private Set<AssessmentAnswers> assessmentAnswers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "isolationsDetails", "assessments" }, allowSetters = true)
    private HomeIsolations homeIsolations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assessment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssessmentDate() {
        return this.assessmentDate;
    }

    public Assessment assessmentDate(Instant assessmentDate) {
        this.setAssessmentDate(assessmentDate);
        return this;
    }

    public void setAssessmentDate(Instant assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Assessment lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Assessment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<AssessmentAnswers> getAssessmentAnswers() {
        return this.assessmentAnswers;
    }

    public void setAssessmentAnswers(Set<AssessmentAnswers> assessmentAnswers) {
        if (this.assessmentAnswers != null) {
            this.assessmentAnswers.forEach(i -> i.setAssessment(null));
        }
        if (assessmentAnswers != null) {
            assessmentAnswers.forEach(i -> i.setAssessment(this));
        }
        this.assessmentAnswers = assessmentAnswers;
    }

    public Assessment assessmentAnswers(Set<AssessmentAnswers> assessmentAnswers) {
        this.setAssessmentAnswers(assessmentAnswers);
        return this;
    }

    public Assessment addAssessmentAnswers(AssessmentAnswers assessmentAnswers) {
        this.assessmentAnswers.add(assessmentAnswers);
        assessmentAnswers.setAssessment(this);
        return this;
    }

    public Assessment removeAssessmentAnswers(AssessmentAnswers assessmentAnswers) {
        this.assessmentAnswers.remove(assessmentAnswers);
        assessmentAnswers.setAssessment(null);
        return this;
    }

    public HomeIsolations getHomeIsolations() {
        return this.homeIsolations;
    }

    public void setHomeIsolations(HomeIsolations homeIsolations) {
        this.homeIsolations = homeIsolations;
    }

    public Assessment homeIsolations(HomeIsolations homeIsolations) {
        this.setHomeIsolations(homeIsolations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assessment)) {
            return false;
        }
        return id != null && id.equals(((Assessment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assessment{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
