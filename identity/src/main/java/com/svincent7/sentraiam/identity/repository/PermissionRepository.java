package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    Optional<PermissionEntity> findByPermission(String permission);
    List<PermissionEntity> findByUserVisible(boolean userVisible);
}
