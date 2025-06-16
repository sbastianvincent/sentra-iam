package com.svincent7.sentraiam.identity.service.credential;

import org.springframework.data.util.Pair;

public interface SecretService {
    Pair<String, String> generateHashSecretPair(String password);
    boolean verifyHashSecretPair(Pair<String, String> secretPairData, String password);
}
