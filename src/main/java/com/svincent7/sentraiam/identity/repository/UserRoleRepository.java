package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.UserRoleKey;
import com.svincent7.sentraiam.identity.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleMapping, UserRoleKey> {
    List<UserRoleMapping> findByUserId(String userId);
}
