package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, String> {
    Optional<TenantEntity> findByTenantName(String tenantName);
}
