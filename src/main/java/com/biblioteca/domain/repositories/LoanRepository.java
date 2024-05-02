package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {
    List<Loan> findByClientIdAndBookId(String clientId, String bookId);
}