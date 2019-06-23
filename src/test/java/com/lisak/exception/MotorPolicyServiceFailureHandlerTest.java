package com.lisak.exception;

import com.lisak.swagger.model.MPPolicyCoverResponse;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;

import static com.lisak.exception.MotorPolicyError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotorPolicyServiceFailureHandlerTest {
    private static final String MOCK_VERSION = "0.1";
    private static final String RETRIEVE_VERSION = "retrieve.version";
    private static final String MESSAGE = "must match \"^(LATEST)|(latest)|(IN-FORCE)|(in-force)|([0-9]{1,10})$\", href=null}], infos=[], acceptResults=null}";
    private static final String EXPECTED_MESSAGE = "Field version must match \"^(LATEST)|(latest)|(IN-FORCE)|(in-force)|([0-9]{1,10})$\", href=null}], infos=[], acceptResults=null}";

    private MotorPolicyServiceFailureExceptionHandler handler;

    @Before
    public void setUp() {
        initMocks(this);
        handler = new MotorPolicyServiceFailureExceptionHandler(MOCK_VERSION);
    }

    @Test
    public void motorPolicyServiceFailureException() {
        MotorPolicyServiceFailureException e = new MotorPolicyServiceFailureException(MOTOR_POLICY_SERVICE_FAILURE.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MOTOR_POLICY_SERVICE_FAILURE.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(MOTOR_POLICY_SERVICE_FAILURE.getMessage());
    }

    @Test
    public void motorPolicyNotFoundException() {
        MotorPolicyNotFoundException e = new MotorPolicyNotFoundException(MOTOR_POLICY_NOT_FOUND.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MOTOR_POLICY_NOT_FOUND.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(MOTOR_POLICY_NOT_FOUND.getMessage());
    }

    @Test
    public void illegalArgumentException() {
        IllegalArgumentException e = new IllegalArgumentException(MOTOR_POLICY_BAD_REQUEST.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MOTOR_POLICY_BAD_REQUEST.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(MOTOR_POLICY_BAD_REQUEST.getMessage());
    }

    @Test
    public void oracleGatewayException() {
        OracleGatewayException e = new OracleGatewayException(ORACLE_GATEWAY_EXCEPTION.getCode(),ORACLE_GATEWAY_EXCEPTION.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getMessage());
    }

    @Test
    public void exception() {
        Exception e = new Exception(UNEXPECTED_ERROR.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(UNEXPECTED_ERROR.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(UNEXPECTED_ERROR.getMessage());
    }

    @Test
    public void motorPolicyUnauthorisedException() {
        OracleGatewayUnauthorizedException e = new OracleGatewayUnauthorizedException(ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN.getMessage());
    }

    @Test
    public void invalidRequestException() {
        InvalidRequestException e = new InvalidRequestException(MOTOR_POLICY_BAD_REQUEST.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MOTOR_POLICY_BAD_REQUEST.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(MOTOR_POLICY_BAD_REQUEST.getMessage());
    }

    @Test
    public void constraintViolationException() {
        MPPolicyCoverResponse result = handler.handle(createConstraintViolation());
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).isNotNull().hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(EXPECTED_MESSAGE);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST.getCode());
    }

    @Test
    public void httpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException(MOTOR_POLICY_UNSUPPORTED_METHOD.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(MOTOR_POLICY_UNSUPPORTED_METHOD.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(MOTOR_POLICY_UNSUPPORTED_METHOD.getMessage());
    }

    @Test
    public void httpMessageNotReadableException() {
        HttpMessageNotReadableException e = new HttpMessageNotReadableException(SERVICE_FAILURE_MESSAGE_NOT_READABLE.getMessage());
        MPPolicyCoverResponse result = handler.handle(e);
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getApiVersion()).isEqualTo(MOCK_VERSION);
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(SERVICE_FAILURE_MESSAGE_NOT_READABLE.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(SERVICE_FAILURE_MESSAGE_NOT_READABLE.getMessage());
    }

    private ConstraintViolationException createConstraintViolation() {
        ConstraintViolation<String> mockConstraintViolation = mock(ConstraintViolation.class);
        when(mockConstraintViolation.getPropertyPath()).thenReturn(PathImpl.createPathFromString(RETRIEVE_VERSION));
        when(mockConstraintViolation.getMessage()).thenReturn(MESSAGE);
        HashSet<ConstraintViolation<String>> violations = new HashSet<>();
        violations.add(mockConstraintViolation);
        return new ConstraintViolationException(violations);
    }
}