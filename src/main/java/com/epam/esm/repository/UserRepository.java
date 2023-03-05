package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
    boolean existsByEmail(String email);
}
