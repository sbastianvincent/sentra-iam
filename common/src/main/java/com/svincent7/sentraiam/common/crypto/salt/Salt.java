package com.svincent7.sentraiam.common.crypto.salt;

import java.security.SecureRandom;

public final class Salt {

    private static final int SALT_LENGTH = 16;
    private Salt() {

    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}
