package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.identity.model.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, String> {
}
