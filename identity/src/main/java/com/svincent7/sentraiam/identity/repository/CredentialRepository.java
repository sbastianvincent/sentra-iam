package com.svincent7.sentraiam.identity.repository;

import com.svincent7.sentraiam.common.dto.credential.CredentialType;
import com.svincent7.sentraiam.identity.model.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, String> {
    Optional<CredentialEntity> findByUserIdAndIdentifierAndType(String userId, String identifier, CredentialType type);
}
