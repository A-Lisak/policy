package com.lisak.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MotorPolicyError {


    MOTOR_POLICY_BAD_REQUEST("MPS-001", "Bad Request"),
    MOTOR_POLICY_UNSUPPORTED_METHOD("MPS-002", "Method not allowed"),
    MOTOR_POLICY_NOT_FOUND("MPS-003", "Motor policy details not found"),
    MOTOR_POLICY_SERVICE_FAILURE("MPS-004", "Motor policy service failure"),
    SERVICE_FAILURE_MESSAGE_NOT_READABLE("MPS-005", "Message not readable"),
    ORACLE_GATEWAY_EXCEPTION("MPS-006", "Oracle Gateway response exception"),
    ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN("MPS-007", "OracleGateway - Unauthorised/Forbidden"),
    UNEXPECTED_ERROR("MPS-008", "Unexpected Error Occurred");

    private final String code;
    private final String message;
}