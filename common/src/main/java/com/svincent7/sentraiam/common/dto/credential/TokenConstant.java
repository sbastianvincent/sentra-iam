package com.svincent7.sentraiam.common.dto.credential;

public final class TokenConstant {
    private TokenConstant() {

    }

    public static final String ACTIVE = "active";
    public static final String KEY_ID = "kid";
    public static final String USERNAME = "uname";
    public static final String FIRSTNAME = "fname";
    public static final String LASTNAME = "lname";
    public static final String VERSION = "ver";

    public static final int TOKEN_HEADER_INDEX = 0;
    public static final int TOKEN_PAYLOAD_INDEX = TOKEN_HEADER_INDEX + 1;
    public static final int TOKEN_SIGNATURE_INDEX = TOKEN_PAYLOAD_INDEX + 1;
    public static final int TOTAL_TOKEN_INDEX = TOKEN_SIGNATURE_INDEX + 1;
}
