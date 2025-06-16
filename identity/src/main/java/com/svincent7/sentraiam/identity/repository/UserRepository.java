package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> getByUsernameAndTenantId(String username, String tenantId);
}
