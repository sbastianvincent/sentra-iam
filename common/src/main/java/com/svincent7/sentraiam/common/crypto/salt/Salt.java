package com.svincent7.sentraiam.common.crypto.salt;

import java.security.SecureRandom;

public final class Salt {

    private static final int SALT_LENGTH = 16;
    private Salt() {

    }

    public static byte[] generateSalt() {
        return generateSalt(SALT_LENGTH);
    }

    public static byte[] generateSalt(final int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }
}
