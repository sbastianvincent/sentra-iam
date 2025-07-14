package com.svincent7.sentraiam.identity.dto.credential;

import com.svincent7.sentraiam.identity.crypto.hash.HashType;
import lombok.Data;

@Data
public class CredentialData {
    private HashType algorithm;
    private int iterations;
}
