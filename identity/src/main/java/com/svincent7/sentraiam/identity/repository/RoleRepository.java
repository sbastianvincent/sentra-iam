package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> getByRoleNameAndTenantId(String roleName, String tenantId);
}
