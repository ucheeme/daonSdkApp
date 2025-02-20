package com.daon.ps.demo.api;

public enum ErrorCode {
    NO_ERROR((short) 0x0, "NO ERROR"),
    WAIT_USER_ACTION((short) 0x1, "WAIT USER ACTION"),
    INSECURE_TRANSPORT((short) 0x2, "INSECURE TRANSPORT"),
    USER_CANCELLED((short) 0x3, "USER CANCELLED"),
    UNSUPPORTED_VERSION((short) 0x4, "UNSUPPORTED VERSION_1_0"),
    NO_SUITABLE_AUTHENTICATOR((short) 0x5, "NO SUITABLE AUTHENTICATOR"),
    PROTOCOL_ERROR((short) 0x6, "PROTOCOL ERROR"),
    UNTRUSTED_FACET_ID((short) 0x7, "UNTRUSTED FACET ID"),
    UNKNOWN((short) 0xFF, "UNKNOWN");

    private final short    VALUE;
    private final String DESCRIPTION;

    public short getValue() {
        return VALUE;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    ErrorCode(final short value, final String description) {
        this.VALUE = value;
        this.DESCRIPTION = description;
    }

    public static ErrorCode getByValue(final short value) {
        for (final ErrorCode errorCode : values()) {
            if (errorCode.getValue() == value) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("Invalid ErrorCode value: " + Short.toString(value));
    }
}

