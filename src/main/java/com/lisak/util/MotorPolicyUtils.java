package com.lisak.util;

import com.lisak.exception.MotorPolicyNotFoundException;
import com.lisak.exception.OracleGatewayException;
import com.lisak.services.accept.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;

import static com.lisak.exception.MotorPolicyError.MOTOR_POLICY_NOT_FOUND;
import static com.lisak.exception.MotorPolicyError.ORACLE_GATEWAY_EXCEPTION;
import static org.springframework.util.ObjectUtils.isEmpty;

public final class MotorPolicyUtils {

    private MotorPolicyUtils() {
    }

    private static final String EMPTY_PAYLOAD = "{}";

    public static String extractPayload(final OracleGatewayResponse response) {
        if (isEmpty(response) || isEmpty(response.getResults()) || isEmpty(response.getResults().get(0).getPayloadOut())) {
            throw new OracleGatewayException(ORACLE_GATEWAY_EXCEPTION.getCode(), ORACLE_GATEWAY_EXCEPTION.getMessage());
        }

        final String payloadOut = response.getResults().get(0).getPayloadOut();

        if (EMPTY_PAYLOAD.equals(payloadOut)) {
            throw new MotorPolicyNotFoundException(MOTOR_POLICY_NOT_FOUND.getMessage());
        }

        return payloadOut;
    }
}