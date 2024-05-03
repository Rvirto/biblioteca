package com.biblioteca.domain.builders;

import com.biblioteca.domain.entities.Book;
import com.biblioteca.domain.entities.Client;
import com.biblioteca.domain.entities.Loan;

/**
 * Loan Builder class used to dynamically create a Loan entity
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public class LoanBuilder {

    private String clientId;
    private String bookId;

    public LoanBuilder() {
    }

    public static LoanBuilder builder() {
        return new LoanBuilder();
    }

    public LoanBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public LoanBuilder withBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public Loan build() {
        Loan loan = new Loan();
        loan.setClient(new Client(this.clientId));
        loan.setBook(new Book(this.bookId));
        return loan;
    }
}
