package com.biblioteca.model.builders;

import com.biblioteca.model.entities.Book;
import com.biblioteca.model.entities.Client;
import com.biblioteca.model.entities.Loan;

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
