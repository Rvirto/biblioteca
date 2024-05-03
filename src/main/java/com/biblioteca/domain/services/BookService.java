package com.biblioteca.domain.services;

import com.biblioteca.api.rest.models.request.BookRequestModel;
import com.biblioteca.domain.entities.Book;
import com.biblioteca.domain.entities.BookVersion;
import com.biblioteca.domain.enumeration.BookStatusEnum;
import com.biblioteca.domain.repositories.BookRepository;
import com.biblioteca.domain.repositories.BookVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Book Service class used to meet any need related to a book
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookVersionRepository bookVersionRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookVersionRepository bookVersionRepository) {
        this.bookRepository = bookRepository;
        this.bookVersionRepository = bookVersionRepository;
    }


    public Optional<Book> findById(String bookId) {
        return bookRepository.findById(bookId);
    }

    /**
     * Method used to perform book consultation dynamically according to parameters
     * @param book
     * @param pageable
     * @param status
     * @return Page<Book>
     */
    public Page<Book> getByBook(Book book, Pageable pageable, BookStatusEnum status) {
        List<Book> books = this.bookRepository.findAll(Example.of(book));
        if (status != null) {
            books = books.stream().filter(bookSearched -> bookSearched.isEnable().equals(status.getEnable())).collect(Collectors.toList());
        }
        return new PageImpl<>(books, pageable, books.size());
    }

    /**
     * Method used to insert new books into the database
     * @param book
     * @param bookRequestModel
     * @return
     */
    @Transactional
    public Book create(Book book, BookRequestModel bookRequestModel) {
        book = this.bookRepository.save(book);
        book = this.updateBookStatus(book, bookRequestModel.getStatus());
        return book;
    }

    /**
     * Method used to version control the book status
     * @param book
     * @param status
     * @return Book
     */
    public Book updateBookStatus(Book book, BookStatusEnum status) {
        BookVersion bookVersion = new BookVersion(book, status);
        bookVersion = this.bookVersionRepository.save(bookVersion);
        book.setVersions(Collections.singleton(bookVersion));
        return book;
    }

    public List<Book> findBook(Book book) {
        return this.bookRepository.findAll(Example.of(book));
    }
}
