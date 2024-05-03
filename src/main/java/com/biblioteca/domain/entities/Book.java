package com.biblioteca.domain.entities;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Book class used to store book records
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @Column(name = "IDT_BOOK")
    private String id;

    @Column(name = "DES_TITLE", nullable = false, updatable = false)
    private String title;

    @Column(name = "DES_AUTHOR", nullable = false, updatable = false)
    private String author;

    @Column(name = "DES_YEAR_PUBLICATION", nullable = false, updatable = false)
    private String yearPublication;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDT_BOOK", updatable = false)
    @Where(clause = "NUM_VERSION = (SELECT MAX(sub.NUM_VERSION) FROM BOOK_VERSION sub WHERE sub.IDT_BOOK = IDT_BOOK)")
    private Set<BookVersion> versions;

    @Column(name = "DAT_CREATION", updatable = false)
    private ZonedDateTime creationDate;

    public Book() {
    }

    public Book(String id) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYearPublication() {
        return yearPublication;
    }

    public void setYearPublication(String yearPublication) {
        this.yearPublication = yearPublication;
    }

    public Integer getVersion() {
        return this.getLastVersion().getVersion();
    }

    private BookVersion getLastVersion() {
        return Optional.ofNullable(versions).map(bookVersions -> bookVersions.stream().findFirst().get())
                .orElse(new BookVersion());
    }

    public Boolean isEnable() {
        return this.getLastVersion().isEnable();
    }
    public Boolean isNotEnable() {
        return !this.getLastVersion().isEnable();
    }

    public void setVersions(Set<BookVersion> versions) {
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
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearPublication='" + yearPublication + '\'' +
                ", versions=" + versions +
                ", creationDate=" + creationDate +
                '}';
    }
}
