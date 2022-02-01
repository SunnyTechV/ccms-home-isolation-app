package com.tecgvg.ccms.hi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IsolationsDetails.
 */
@Entity
@Table(name = "isolations_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IsolationsDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "isolation_start_date")
    private Instant isolationStartDate;

    @Column(name = "isolation_end_date")
    private Instant isolationEndDate;

    @Column(name = "referred_dr_name")
    private String referredDrName;

    @Column(name = "referred_dr_mobile")
    private String referredDrMobile;

    @Column(name = "prescription_url")
    private String prescriptionUrl;

    @Column(name = "report_url")
    private String reportUrl;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "self_registered")
    private Boolean selfRegistered;

    @Column(name = "last_assessment")
    private Instant lastAssessment;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @JsonIgnoreProperties(value = { "isolationsDetails", "assessments" }, allowSetters = true)
    @OneToOne(mappedBy = "isolationsDetails")
    private HomeIsolations homeIsolations;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IsolationsDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIsolationStartDate() {
        return this.isolationStartDate;
    }

    public IsolationsDetails isolationStartDate(Instant isolationStartDate) {
        this.setIsolationStartDate(isolationStartDate);
        return this;
    }

    public void setIsolationStartDate(Instant isolationStartDate) {
        this.isolationStartDate = isolationStartDate;
    }

    public Instant getIsolationEndDate() {
        return this.isolationEndDate;
    }

    public IsolationsDetails isolationEndDate(Instant isolationEndDate) {
        this.setIsolationEndDate(isolationEndDate);
        return this;
    }

    public void setIsolationEndDate(Instant isolationEndDate) {
        this.isolationEndDate = isolationEndDate;
    }

    public String getReferredDrName() {
        return this.referredDrName;
    }

    public IsolationsDetails referredDrName(String referredDrName) {
        this.setReferredDrName(referredDrName);
        return this;
    }

    public void setReferredDrName(String referredDrName) {
        this.referredDrName = referredDrName;
    }

    public String getReferredDrMobile() {
        return this.referredDrMobile;
    }

    public IsolationsDetails referredDrMobile(String referredDrMobile) {
        this.setReferredDrMobile(referredDrMobile);
        return this;
    }

    public void setReferredDrMobile(String referredDrMobile) {
        this.referredDrMobile = referredDrMobile;
    }

    public String getPrescriptionUrl() {
        return this.prescriptionUrl;
    }

    public IsolationsDetails prescriptionUrl(String prescriptionUrl) {
        this.setPrescriptionUrl(prescriptionUrl);
        return this;
    }

    public void setPrescriptionUrl(String prescriptionUrl) {
        this.prescriptionUrl = prescriptionUrl;
    }

    public String getReportUrl() {
        return this.reportUrl;
    }

    public IsolationsDetails reportUrl(String reportUrl) {
        this.setReportUrl(reportUrl);
        return this;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public IsolationsDetails remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getSelfRegistered() {
        return this.selfRegistered;
    }

    public IsolationsDetails selfRegistered(Boolean selfRegistered) {
        this.setSelfRegistered(selfRegistered);
        return this;
    }

    public void setSelfRegistered(Boolean selfRegistered) {
        this.selfRegistered = selfRegistered;
    }

    public Instant getLastAssessment() {
        return this.lastAssessment;
    }

    public IsolationsDetails lastAssessment(Instant lastAssessment) {
        this.setLastAssessment(lastAssessment);
        return this;
    }

    public void setLastAssessment(Instant lastAssessment) {
        this.lastAssessment = lastAssessment;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public IsolationsDetails lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public IsolationsDetails lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public HomeIsolations getHomeIsolations() {
        return this.homeIsolations;
    }

    public void setHomeIsolations(HomeIsolations homeIsolations) {
        if (this.homeIsolations != null) {
            this.homeIsolations.setIsolationsDetails(null);
        }
        if (homeIsolations != null) {
            homeIsolations.setIsolationsDetails(this);
        }
        this.homeIsolations = homeIsolations;
    }

    public IsolationsDetails homeIsolations(HomeIsolations homeIsolations) {
        this.setHomeIsolations(homeIsolations);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsolationsDetails)) {
            return false;
        }
        return id != null && id.equals(((IsolationsDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsolationsDetails{" +
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
