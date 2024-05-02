package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.LoanVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanVersionRepository extends JpaRepository<LoanVersion, String> {
}
