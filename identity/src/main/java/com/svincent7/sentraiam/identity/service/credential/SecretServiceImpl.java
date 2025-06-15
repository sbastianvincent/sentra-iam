package com.svincent7.sentraiam.identity.service.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svincent7.sentraiam.common.crypto.hash.HashFactory;
import com.svincent7.sentraiam.common.crypto.hash.HashFactoryImpl;
import com.svincent7.sentraiam.common.crypto.hash.HashType;
import com.svincent7.sentraiam.common.crypto.salt.Salt;
import com.svincent7.sentraiam.common.dto.credential.CredentialData;
import com.svincent7.sentraiam.common.dto.credential.HashData;
import com.svincent7.sentraiam.common.dto.credential.SecretData;
import com.svincent7.sentraiam.common.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {

    private final ObjectMapper objectMapper;
    private final HashFactory hashFactory = new HashFactoryImpl();
    private static final HashType DEFAULT_HASH_TYPE = HashType.BCRYPT_HASH;

    @Override
    public Pair<String, String> generateHashSecretPair(final String password) {
        byte[] salt = Salt.generateSalt();
        try {
            HashData hashData = hashFactory.generateHashData(password, salt, DEFAULT_HASH_TYPE);
            String secretData = objectMapper.writeValueAsString(hashData.getSecretData());
            String credentialData = objectMapper.writeValueAsString(hashData.getCredentialData());

            return Pair.of(secretData, credentialData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verifyHashSecretPair(final Pair<String, String> secretPair, final String password) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(secretPair.getFirst())
                || StringUtils.isEmpty(secretPair.getSecond())) {
            throw new InvalidPasswordException();
        }

        SecretData secretData;
        CredentialData credentialData;
        try {
            secretData = objectMapper.readValue(secretPair.getFirst(), SecretData.class);
            credentialData = objectMapper.readValue(secretPair.getSecond(), CredentialData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        byte[] salt = Base64.getDecoder().decode(secretData.getSalt());

        HashData encodedHash = hashFactory.generateHashData(password, salt, credentialData.getIterations(),
                credentialData.getAlgorithm());

        if (!encodedHash.getSecretData().getValue().equals(secretData.getValue())) {
            throw new InvalidPasswordException();
        }
    }
}
