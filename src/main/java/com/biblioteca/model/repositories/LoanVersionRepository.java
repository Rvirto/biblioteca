package com.biblioteca.model.repositories;

import com.biblioteca.model.entities.LoanVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanVersionRepository extends JpaRepository<LoanVersion, String> {
}
