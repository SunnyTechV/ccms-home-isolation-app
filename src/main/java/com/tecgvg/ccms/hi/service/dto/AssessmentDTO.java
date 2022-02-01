package com.tecgvg.ccms.hi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tecgvg.ccms.hi.domain.Assessment} entity.
 */
public class AssessmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant assessmentDate;

    private Instant lastModified;

    private String lastModifiedBy;

    private HomeIsolationsDTO homeIsolations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Instant assessmentDate) {
        this.assessmentDate = assessmentDate;
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

    public HomeIsolationsDTO getHomeIsolations() {
        return homeIsolations;
    }

    public void setHomeIsolations(HomeIsolationsDTO homeIsolations) {
        this.homeIsolations = homeIsolations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssessmentDTO)) {
            return false;
        }

        AssessmentDTO assessmentDTO = (AssessmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assessmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssessmentDTO{" +
            "id=" + getId() +
            ", assessmentDate='" + getAssessmentDate() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", homeIsolations=" + getHomeIsolations() +
            "}";
    }
}
