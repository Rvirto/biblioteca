package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, String> {
    Optional<Loan> findByClientIdAndBookId(String clientId, String bookId);
}
