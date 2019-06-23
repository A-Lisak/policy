package com.lisak.oraclegateway;

import com.lisak.exception.InvalidRequestException;
import com.lisak.exception.MotorPolicyNotFoundException;
import com.lisak.exception.OracleGatewayException;
import com.lisak.exception.OracleGatewayUnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.lisak.exception.MotorPolicyError.*;

/**
 * Wraps the FeignException generated when response status indicates that call was unsuccessful (i.e. status &gt;= 300)
 */
@Slf4j
public class OracleGatewayErrorDecoder implements ErrorDecoder {

    /**
     * Create new application exception based on response
     */
    @Override
    public Exception decode(final String methodKey, final Response response) {

        log.error("Unsuccessful request to motor policy API. Response code: [{}], Response [{}]",
                response.status(), response);

        final Exception exception;
        switch (HttpStatus.valueOf(response.status())) {
            case NOT_FOUND:
                exception = new MotorPolicyNotFoundException(MOTOR_POLICY_NOT_FOUND.getMessage());
                break;
            case UNAUTHORIZED:
            case FORBIDDEN:
                exception = new OracleGatewayUnauthorizedException(ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN.getMessage());
                break;
            case BAD_REQUEST:
                exception = new InvalidRequestException(MOTOR_POLICY_BAD_REQUEST.getMessage());
                break;
            default:
                exception = new OracleGatewayException(ORACLE_GATEWAY_EXCEPTION.getCode(), ORACLE_GATEWAY_EXCEPTION.getMessage());
                break;
        }

        return exception;
    }
}

