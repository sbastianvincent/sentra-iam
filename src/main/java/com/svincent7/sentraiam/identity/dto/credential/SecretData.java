package com.svincent7.sentraiam.identity.dto.credential;

import lombok.Data;

@Data
public class SecretData {
    private String value;
    private String salt;
}
