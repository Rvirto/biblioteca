package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.LoanVersion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Loan Version Repository class of ready-made and custom queries for database access
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
public interface LoanVersionRepository extends JpaRepository<LoanVersion, String> {
}
