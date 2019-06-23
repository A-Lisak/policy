package com.lisak.exception;

import com.lisak.swagger.model.ErrorInfo;
import com.lisak.swagger.model.MPPolicyCoverResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@Slf4j
public class MotorPolicyServiceFailureExceptionHandler {

    private String version;

    @Autowired
    public MotorPolicyServiceFailureExceptionHandler(@Value("${application.version}") final String version) {
        this.version = version;
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final MotorPolicyServiceFailureException exception) {
        log.error("MotorPolicyServiceFailureException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.MOTOR_POLICY_SERVICE_FAILURE);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final MotorPolicyNotFoundException exception) {
        log.error("MotorPolicyNotFoundException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.MOTOR_POLICY_NOT_FOUND);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final IllegalArgumentException exception) {
        log.error("IllegalArgumentException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST);
    }

    @ResponseStatus(OK)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final OracleGatewayException exception) {
        log.error("OracleGatewayException: {}", exception.getMessage(), exception);
        return buildServiceError(exception);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final Exception exception) {
        log.error("Exception: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.UNEXPECTED_ERROR);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final OracleGatewayUnauthorizedException exception) {
        log.error("MotorPolicyUnauthorisedException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final InvalidRequestException exception) {
        log.error("InvalidRequestException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public MPPolicyCoverResponse handle(final ConstraintViolationException exception) {
        final List<ErrorInfo> errors = exception.getConstraintViolations()
                                                .stream()
                                                .map(this::getErrorMessage)
                                                .collect(Collectors.toCollection(ArrayList::new));
        return new MPPolicyCoverResponse()
                .apiVersion(version)
                .errors(errors)
                .infos(Collections.emptyList())
                .results(Collections.emptyList());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    public MPPolicyCoverResponse handle(final MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException: {}", exception.getMessage(), exception);
        return buildServiceError(exception);
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler
    @ResponseBody
    public MPPolicyCoverResponse handle(final HttpRequestMethodNotSupportedException exception) {
        log.error("HttpRequestMethodNotSupportedException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.MOTOR_POLICY_UNSUPPORTED_METHOD);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    @ResponseBody
    protected MPPolicyCoverResponse handle(final HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: {}", exception.getMessage(), exception);
        return buildServiceError(MotorPolicyError.SERVICE_FAILURE_MESSAGE_NOT_READABLE);
    }

    private MPPolicyCoverResponse buildServiceError(final MotorPolicyError motorPolicyError) {
        return new MPPolicyCoverResponse()
                .apiVersion(version)
                .addErrorsItem(new ErrorInfo().code(motorPolicyError.getCode()).message(motorPolicyError.getMessage()).traceId(getTraceId()))
                .infos(Collections.emptyList())
                .results(Collections.emptyList());
    }

    private MPPolicyCoverResponse buildServiceError(final OracleGatewayException exception) {
        return new MPPolicyCoverResponse()
                .apiVersion(version)
                .addErrorsItem(new ErrorInfo().code(exception.getCode()).message(exception.getMessage()).traceId(getTraceId()))
                .infos(Collections.emptyList())
                .results(Collections.emptyList());
    }

    private MPPolicyCoverResponse buildServiceError(final MethodArgumentTypeMismatchException exception) {
        return new MPPolicyCoverResponse()
                .apiVersion(version)
                .addErrorsItem(new ErrorInfo().code(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST.getCode()).message(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST.getMessage() + " - " + exception.getMessage()).traceId(getTraceId()))
                .infos(Collections.emptyList())
                .results(Collections.emptyList());
    }

    private ErrorInfo getErrorMessage(final ConstraintViolation<?> error) {
        final String field = ((PathImpl) error.getPropertyPath()).getLeafNode().getName();
        final String errorMessage = "Field " + field + " " + error.getMessage();
        return new ErrorInfo().code(MotorPolicyError.MOTOR_POLICY_BAD_REQUEST.getCode()).message(errorMessage).traceId(getTraceId());
    }
    
    private String getTraceId() {
        return MDC.get("X-B3-TraceId");
    }
}
