package com.lisak.exception;

import lombok.Getter;

@Getter
public class OracleGatewayUnauthorizedException extends RuntimeException {

    public OracleGatewayUnauthorizedException(final String message) {
        super(message);
    }
}
