package com.biblioteca.domain.repositories;

import com.biblioteca.domain.entities.BookVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Book Version Repository class of ready-made and custom queries for database access
 *
 * @author Renato Virto (renatovirtomoreira@outlook.com)
 * @since 1.0.0
 */
@Repository
public interface BookVersionRepository extends JpaRepository<BookVersion, String> {
}
