package com.oop.backend.Repository;

import com.oop.backend.Entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    boolean existsByEmail(String email);
    CustomerEntity findByEmail(String e);
    CustomerEntity findByToken(String token);
}
