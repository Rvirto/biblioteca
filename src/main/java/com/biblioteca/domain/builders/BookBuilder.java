package com.biblioteca.domain.builders;

import com.biblioteca.domain.entities.Book;

/**
 * Book Builder class used to dynamically create a Book entity
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class BookBuilder {

    private String title;
    private String author;
    private String yearPublication;

    public BookBuilder() {
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookBuilder withYearPublication(String yearPublication) {
        this.yearPublication = yearPublication;
        return this;
    }

    public Book build() {
        Book book = new Book();
        book.setTitle(this.title);
        book.setAuthor(this.author);
        book.setYearPublication(yearPublication);
        return book;
    }
}
