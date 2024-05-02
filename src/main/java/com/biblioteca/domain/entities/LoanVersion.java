package com.biblioteca.domain.entities;

import com.biblioteca.domain.enumeration.LoanStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "LOAN_VERSION")
public class LoanVersion {

    @Id
    @Column(name = "IDT_LOAN_VERSION")
    private String id;

    @Column(name = "IDT_LOAN", nullable = false, updatable = false)
    private String loanId;

    @Column(name = "NUM_VERSION", nullable = false, updatable = false)
    private Integer version;

    @Column(name = "COD_STATUS", nullable = false, updatable = false)
    private String status;

    @Column(name = "DAT_CREATION", updatable = false)
    private ZonedDateTime creationDate;

    public LoanVersion() {
    }

    public LoanVersion(Loan loan, LoanStatusEnum loanStatusEnum) {
        this.loanId = loan.getId();
        this.status = loanStatusEnum.name();
        this.incrementVersion(loan);
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = ZonedDateTime.now();
    }

    private void incrementVersion(Loan loan) {
        final Integer actualVersion = loan.getVersion();
        if (actualVersion != null) {
            this.setVersion(actualVersion + 1);
        } else {
            this.setVersion(1);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanVersion that = (LoanVersion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoanVersion{" +
                "id='" + id + '\'' +
                ", loanId='" + loanId + '\'' +
                ", version=" + version +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
