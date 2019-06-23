package com.lisak.exception;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class OracleGatewayException extends RuntimeException {
    private static final long serialVersionUID = -5259372800839088877L;
    private static final String MESSAGE_TEMPLATE = "%s, error response : %s";

    private final String code;

    public OracleGatewayException(final String code, final String message) {
        super(message);
        this.code = code;
    }
}
