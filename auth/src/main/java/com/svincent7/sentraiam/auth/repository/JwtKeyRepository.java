package com.svincent7.sentraiam.auth.repository;

import com.svincent7.sentraiam.auth.model.JwtKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtKeyRepository extends JpaRepository<JwtKey, String> {
    Optional<JwtKey> findFirstByTenantIdOrderByKeyVersionDesc(String tenantId);
}
