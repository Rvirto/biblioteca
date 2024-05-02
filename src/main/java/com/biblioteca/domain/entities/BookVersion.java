package com.biblioteca.domain.entities;

import com.biblioteca.domain.enumeration.BookStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "BOOK_VERSION")
public class BookVersion {

    @Id
    @Column(name = "IDT_BOOK_VERSION")
    private String id;

    @Column(name = "IDT_BOOK", nullable = false, updatable = false)
    private String bookId;

    @Column(name = "NUM_VERSION", nullable = false, updatable = false)
    private Integer version;

    @Column(name = "FLG_ENABLE", updatable = false)
    private Boolean enable;

    @Column(name = "DAT_CREATION", updatable = false)
    private ZonedDateTime creationDate;

    public BookVersion() {
    }

    public BookVersion(Book book, BookStatusEnum bookStatusEnum) {
        this.bookId = book.getId();
        this.enable = bookStatusEnum.getEnable();
        this.incrementVersion(book);
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = ZonedDateTime.now();
    }

    private void incrementVersion(Book book) {
        final Integer actualVersion = book.getVersion();
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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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
        BookVersion that = (BookVersion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookVersion{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", version=" + version +
                ", enable=" + enable +
                ", creationDate=" + creationDate +
                '}';
    }
}
