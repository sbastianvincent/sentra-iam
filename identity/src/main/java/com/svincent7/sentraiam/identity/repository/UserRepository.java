package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
}
