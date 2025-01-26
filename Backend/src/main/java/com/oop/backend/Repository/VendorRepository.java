package com.oop.backend.Repository;

import com.oop.backend.Entities.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<VendorEntity, String> {
    VendorEntity findByToken(String token);
    boolean existsByEmail(String email);
    VendorEntity findByEmail(String email);

}
