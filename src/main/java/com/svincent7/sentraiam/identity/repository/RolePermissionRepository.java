package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.RolePermissionKey;
import com.svincent7.sentraiam.identity.model.RolePermissionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionMapping, RolePermissionKey> {
    List<RolePermissionMapping> findByRoleId(String roleId);
}
