package com.biblioteca.domain.services;

import com.biblioteca.domain.entities.Loan;
import com.biblioteca.domain.entities.LoanVersion;
import com.biblioteca.domain.enumeration.LoanStatusEnum;
import com.biblioteca.domain.exceptions.BadRequestException;
import com.biblioteca.domain.repositories.LoanRepository;
import com.biblioteca.domain.repositories.LoanVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import static com.biblioteca.domain.enumeration.BookStatusEnum.AVAILABLE;
import static com.biblioteca.domain.enumeration.BookStatusEnum.UNAVAILABLE;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.BOOK_IS_NOT_AVAILABLE;
import static com.biblioteca.domain.enumeration.ExceptionMessagesEnum.LOAN_ALREADY_RETURNED;
import static com.biblioteca.domain.enumeration.LoanStatusEnum.BORROWED;
import static com.biblioteca.domain.enumeration.LoanStatusEnum.RETURNED;

/**
 * Loan Service class used to meet any need related to a loan
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
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

    /**
     * Loan method used to make a loan by creating and updating all necessary tables
     * @param loan
     * @return Loan
     */
    @Transactional
    public Loan loan(Loan loan) {
        try {
            if (loan.getBook().isNotEnable()) {
                throw new BadRequestException(BOOK_IS_NOT_AVAILABLE);
            }

            loan = this.loanRepository.save(loan);
            loan = this.updateLoanStatus(loan, BORROWED);
            this.bookService.updateBookStatus(loan.getBook(), UNAVAILABLE);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(BOOK_IS_NOT_AVAILABLE);
        }
        return loan;
    }

    /**
     * Devolution method used to make a devolution by updating all necessary tables
     * @param loan
     * @return Loan
     */
    @Transactional
    public Loan devolution(Loan loan) {
        if (!loan.getStatus().equals(BORROWED.name())) {
            throw new BadRequestException(LOAN_ALREADY_RETURNED);
        }

        loan = this.updateLoanStatus(loan, RETURNED);
        this.bookService.updateBookStatus(loan.getBook(), AVAILABLE);
        return loan;
    }

    /**
     * Method used to version control the loan status
     * @param loan
     * @param loanStatusEnum
     * @return Loan
     */
    private Loan updateLoanStatus(Loan loan, LoanStatusEnum loanStatusEnum) {
        LoanVersion loanVersion = new LoanVersion(loan, loanStatusEnum);
        loanVersion = this.loanVersionRepository.save(loanVersion);
        loan.setVersions(Collections.singleton(loanVersion));
        return loan;
    }

    /**
     * Method used to perform loan consultation dynamically according to parameters
     * @param loan
     * @param status
     * @param pageable
     * @return Page<Loan>
     */
    public Page<Loan> getByLoan(Loan loan, Pageable pageable, LoanStatusEnum status) {
        List<Loan> loans = this.loanRepository.findAll(Example.of(loan));
        if (status != null) {
            loans = loans.stream().filter(loanSearched -> loanSearched.getStatus().equals(status.name())).collect(Collectors.toList());
        }
        return new PageImpl<>(loans, pageable, loans.size());
    }

    /**
     * Method used to perform a loan query using clientId and BookId, returning only what is available
     * @param loan
     * @return Optional<Loan>
     */
    public Optional<Loan> findLoan(Loan loan) {
        return this.loanRepository.findByClientIdAndBookId(loan.getClient().getId(), loan.getBook().getId())
                .stream().filter(loanSearched -> !loanSearched.getStatus().equals(RETURNED.name())).findFirst();
    }
}
