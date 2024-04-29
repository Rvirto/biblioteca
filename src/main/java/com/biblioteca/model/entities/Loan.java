package com.biblioteca.model.entities;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "LOAN")
public class Loan {

    @Id
    @Column(name = "IDT_LOAN")
    private String id;

    @OneToOne
    @JoinColumn(name = "IDT_BOOK", nullable = false, updatable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "IDT_CLIENT", nullable = false, updatable = false)
    private Client client;

    @Column(name = "DAT_DEVOLUTION", updatable = false)
    private ZonedDateTime devolutionDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDT_LOAN", updatable = false)
    @Where(clause = "NUM_VERSION = (SELECT MAX(sub.NUM_VERSION) FROM LOAN_VERSION sub WHERE sub.IDT_LOAN = IDT_LOAN)")
    private Set<LoanVersion> versions;

    @Column(name = "DAT_CREATION", updatable = false)
    private ZonedDateTime creationDate;

    public Loan() {
    }

    public Loan(Book book, Client client) {
        this.book = book;
        this.client = client;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = ZonedDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ZonedDateTime getDevolutionDate() {
        return devolutionDate;
    }

    public void setDevolutionDate(ZonedDateTime devolutionDate) {
        this.devolutionDate = devolutionDate;
    }

    public Integer getVersion() {
        return this.getLastVersion().getVersion();
    }

    private LoanVersion getLastVersion() {
        return Optional.ofNullable(versions).map(loanVersions -> loanVersions.stream().findFirst().get())
                .orElse(new LoanVersion());
    }

    public String getStatus() {
        return this.getLastVersion().getStatus();
    }

    public void setVersions(Set<LoanVersion> versions) {
        this.versions = versions;
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
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", book=" + book +
                ", client=" + client +
                ", devolutionDate=" + devolutionDate +
                ", versions=" + versions +
                ", creationDate=" + creationDate +
                '}';
    }
}
