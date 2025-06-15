package com.svincent7.sentraiam.common.dto.credential;

import lombok.Data;

@Data
public class SecretData {
    private String value;
    private String salt;
}
