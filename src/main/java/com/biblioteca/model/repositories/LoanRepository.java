package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, String> {
}
