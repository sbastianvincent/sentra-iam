package com.svincent7.sentraiam.common.dto.credential;

import com.svincent7.sentraiam.common.crypto.hash.HashType;
import lombok.Data;

@Data
public class CredentialData {
    private HashType algorithm;
    private int iterations;
}
