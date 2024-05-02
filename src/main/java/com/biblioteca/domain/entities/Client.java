package com.biblioteca.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @Column(name = "IDT_CLIENT")
    private String id;

    @Column(name = "DES_NAME", nullable = false, updatable = false)
    private String name;

    @Column(name = "DES_EMAIL", nullable = false, updatable = false)
    private String email;

    @Column(name = "DES_DOCUMENT_TYPE", nullable = false, updatable = false)
    private String documentType;

    @Column(name = "DES_DOCUMENT_VALUE", nullable = false, updatable = false)
    private String documentValue;

    @Column(name = "DAT_CREATION", updatable = false)
    private ZonedDateTime creationDate;

    public Client() {
    }

    public Client(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(String documentValue) {
        this.documentValue = documentValue;
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
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentValue='" + documentValue + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
