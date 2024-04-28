package com.biblioteca.model.services;

import com.biblioteca.model.entities.Loan;
import com.biblioteca.model.entities.LoanVersion;
import com.biblioteca.model.enumeration.LoanStatusEnum;
import com.biblioteca.model.exceptions.BadRequestException;
import com.biblioteca.model.repositories.LoanRepository;
import com.biblioteca.model.repositories.LoanVersionRepository;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.biblioteca.model.enumeration.BookStatusEnum.AVAILABLE;
import static com.biblioteca.model.enumeration.BookStatusEnum.UNAVAILABLE;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.BOOK_IS_NOT_AVAILABLE;
import static com.biblioteca.model.enumeration.ExceptionMessagesEnum.LOAN_ALREADY_RETURNED;
import static com.biblioteca.model.enumeration.LoanStatusEnum.BORROWED;
import static com.biblioteca.model.enumeration.LoanStatusEnum.RETURNED;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanVersionRepository loanVersionRepository;
    private final BookService bookService;

    @Autowired
    public LoanService(LoanRepository loanRepository, LoanVersionRepository loanVersionRepository, BookService bookService) {
        this.loanRepository = loanRepository;
        this.loanVersionRepository = loanVersionRepository;
        this.bookService = bookService;
    }

    public Optional<Loan> findById(String loanId) {
        return this.loanRepository.findById(loanId);
    }

    @Transactional
    public Loan loan(Loan loan) {
        if (loan.getBook().isNotEnable()) {
            throw new BadRequestException(BOOK_IS_NOT_AVAILABLE);
        }

        loan = this.loanRepository.save(loan);
        loan = this.updateLoanStatus(loan, BORROWED);
        this.bookService.updateBookStatus(loan.getBook(), UNAVAILABLE);
        return loan;
    }

    @Transactional
    public Loan devolution(Loan loan) {
        if (!loan.getStatus().equals(BORROWED.name())) {
            throw new BadRequestException(LOAN_ALREADY_RETURNED);
        }

        loan = this.updateLoanStatus(loan, RETURNED);
        this.bookService.updateBookStatus(loan.getBook(), AVAILABLE);
        return loan;
    }

    private Loan updateLoanStatus(Loan loan, LoanStatusEnum loanStatusEnum) {
        LoanVersion loanVersion = new LoanVersion(loan, loanStatusEnum);
        this.loanVersionRepository.save(loanVersion);
        return loan;
    }

    public Page<Loan> getByLoan(Loan loan, Pageable pageable, LoanStatusEnum status) {
        List<Loan> loans = this.loanRepository.findAll(Example.of(loan));
        if (status != null) {
            loans = loans.stream().filter(loanSearched -> loanSearched.getStatus().equals(status.name())).collect(Collectors.toList());
        }
        return new PageImpl<>(loans, pageable, loans.size());
    }

    public Optional<Loan> findLoan(Loan loan) {
        return this.loanRepository.findByClientIdAndBookId(loan.getClient().getId(), loan.getBook().getId());
    }
}
