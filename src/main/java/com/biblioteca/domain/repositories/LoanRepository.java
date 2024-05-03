package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Loan Repository class of ready-made and custom queries for database access
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    List<Loan> findByClientIdAndBookId(String clientId, String bookId);
}