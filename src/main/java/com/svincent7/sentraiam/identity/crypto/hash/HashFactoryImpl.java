package com.svincent7.sentraiam.identity.crypto.hash;

import com.svincent7.sentraiam.identity.dto.credential.CredentialData;
import com.svincent7.sentraiam.identity.dto.credential.HashData;
import com.svincent7.sentraiam.identity.dto.credential.SecretData;
import com.svincent7.sentraiam.common.exception.HashingException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.crypto.params.Argon2Parameters;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HashFactoryImpl implements HashFactory {

    private static final int PBKDF2_DEFAULT_ITERATION = 27500;
    private static final int PBE_KEY_LENGTH = 256;

    private static final int ARGON2_DEFAULT_ITERATIONS = 3;
    private static final int ARGON2_BYTE_LENGTH = 32;
    private static final int ARGON2_MEMORY = 16384;
    private static final int ARGON2_PARALLELISM = 1;

    private static final int BCRYPT_DEFAULT_COST = 12;

    @Override
    public HashData generateHashData(final String text, final byte[] salt, final HashType algorithm) {
        switch (algorithm) {
            case ARGON2_HASH -> {
                return generateHashData(text, salt, ARGON2_DEFAULT_ITERATIONS, algorithm);
            }
            case PBKDF2_SHA256_HASH -> {
                return generateHashData(text, salt, PBKDF2_DEFAULT_ITERATION, algorithm);
            }
            case BCRYPT_HASH -> {
                return generateHashData(text, salt, BCRYPT_DEFAULT_COST, algorithm);
            }
            default -> {
                throw new IllegalArgumentException("Unsupported hash algorithm: " + algorithm);
            }
        }
    }

    @Override
    public HashData generateHashData(final String text, final byte[] salt, final int iteration,
                                     final HashType algorithm) {
        HashData hashData = new HashData();
        final String encodedHashString;
        switch (algorithm) {
            case ARGON2_HASH -> {
                encodedHashString = getHashWithArgon2(text, salt, iteration);
            }
            case PBKDF2_SHA256_HASH -> {
                encodedHashString = getHashWithPBE(text, salt, iteration);
            }
            case BCRYPT_HASH -> {
                encodedHashString = getHashWithBCrypt(text, salt, iteration);
            }
            default -> throw new IllegalArgumentException("Unsupported hash algorithm: " + algorithm);
        }

        SecretData secretData = new SecretData();
        secretData.setValue(encodedHashString);
        secretData.setSalt(Base64.getEncoder().encodeToString(salt));
        hashData.setSecretData(secretData);

        CredentialData credentialData = new CredentialData();
        credentialData.setAlgorithm(algorithm);
        credentialData.setIterations(iteration);
        hashData.setCredentialData(credentialData);

        return hashData;
    }

    private String getHashWithArgon2(final String password, final byte[] salt, final int iteration) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(ARGON2_PARALLELISM)
                .withMemoryAsKB(ARGON2_MEMORY)
                .withIterations(iteration);

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] hash = new byte[ARGON2_BYTE_LENGTH];
        generator.generateBytes(password.getBytes(), hash);

        return Base64.getEncoder().encodeToString(hash);
    }

    private String getHashWithPBE(final String password, final byte[] salt, final int iteration) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iteration, PBE_KEY_LENGTH);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = keyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new HashingException("Failed to hash using PBKDF2", e);
        }
    }

    private String getHashWithBCrypt(final String password, final byte[] salt, final int iteration) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashed = BCrypt.generate(passwordBytes, salt, iteration);

        return Base64.getEncoder().encodeToString(hashed);
    }
}
