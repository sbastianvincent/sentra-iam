package com.svincent7.sentraiam.identity.crypto.hash;

import com.svincent7.sentraiam.identity.dto.credential.HashData;

public interface HashFactory {
    HashData generateHashData(String text, byte[] salt, HashType hashType);
    HashData generateHashData(String text, byte[] salt, int iteration, HashType hashType);
}
