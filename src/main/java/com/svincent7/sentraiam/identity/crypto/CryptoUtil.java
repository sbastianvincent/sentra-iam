package com.svincent7.sentraiam.identity.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class CryptoUtil {
    private CryptoUtil() {

    }

    public static KeyPair generateKeyPair(final String keyAlgorithm, final int keyLength) {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.initialize(keyLength);
        return keyGen.generateKeyPair();
    }

    public static PrivateKey loadPrivateKey(final String keyAlgorithm, final String privateKeyBase64) {
        final KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(keyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] decoded = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey loadPublicKey(final String keyAlgorithm, final String publicKeyBase64) {
        final KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(keyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] decoded = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
