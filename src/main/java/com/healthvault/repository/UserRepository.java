package com.healthvault.repository;

import com.healthvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Data Access Layer for User entity.
 *
 * Key Java Concepts:
 * - Extends JpaRepository: inherits 20+ methods (save, findById, findAll, delete, etc.)
 *   without writing a single SQL query.
 * - Spring Data JPA auto-implements this interface at runtime.
 * - Method naming convention: findBy[FieldName] auto-generates the SQL query.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA auto-generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Auto-generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);
}
