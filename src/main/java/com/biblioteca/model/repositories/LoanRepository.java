package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {
    List<Loan> findByClientIdAndBookId(String clientId, String bookId);
}