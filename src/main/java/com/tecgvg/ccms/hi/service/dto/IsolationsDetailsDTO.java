package com.tecgvg.ccms.hi.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.tecgvg.ccms.hi.domain.IsolationsDetails} entity.
 */
public class IsolationsDetailsDTO implements Serializable {

    private Long id;

    private Instant isolationStartDate;

    private Instant isolationEndDate;

    private String referredDrName;

    private String referredDrMobile;

    private String prescriptionUrl;

    private String reportUrl;

    private String remarks;

    private Boolean selfRegistered;

    private Instant lastAssessment;

    private Instant lastModified;

    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIsolationStartDate() {
        return isolationStartDate;
    }

    public void setIsolationStartDate(Instant isolationStartDate) {
        this.isolationStartDate = isolationStartDate;
    }

    public Instant getIsolationEndDate() {
        return isolationEndDate;
    }

    public void setIsolationEndDate(Instant isolationEndDate) {
        this.isolationEndDate = isolationEndDate;
    }

    public String getReferredDrName() {
        return referredDrName;
    }

    public void setReferredDrName(String referredDrName) {
        this.referredDrName = referredDrName;
    }

    public String getReferredDrMobile() {
        return referredDrMobile;
    }

    public void setReferredDrMobile(String referredDrMobile) {
        this.referredDrMobile = referredDrMobile;
    }

    public String getPrescriptionUrl() {
        return prescriptionUrl;
    }

    public void setPrescriptionUrl(String prescriptionUrl) {
        this.prescriptionUrl = prescriptionUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getSelfRegistered() {
        return selfRegistered;
    }

    public void setSelfRegistered(Boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
    }

    public Instant getLastAssessment() {
        return lastAssessment;
    }

    public void setLastAssessment(Instant lastAssessment) {
        this.lastAssessment = lastAssessment;
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
        if (!(o instanceof IsolationsDetailsDTO)) {
            return false;
        }

        IsolationsDetailsDTO isolationsDetailsDTO = (IsolationsDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isolationsDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsolationsDetailsDTO{" +
            "id=" + getId() +
            ", isolationStartDate='" + getIsolationStartDate() + "'" +
            ", isolationEndDate='" + getIsolationEndDate() + "'" +
            ", referredDrName='" + getReferredDrName() + "'" +
            ", referredDrMobile='" + getReferredDrMobile() + "'" +
            ", prescriptionUrl='" + getPrescriptionUrl() + "'" +
            ", reportUrl='" + getReportUrl() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", selfRegistered='" + getSelfRegistered() + "'" +
            ", lastAssessment='" + getLastAssessment() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
