package com.lisak.oraclegateway;

import com.lisak.exception.MotorPolicyNotFoundException;
import com.lisak.exception.OracleGatewayException;
import com.lisak.exception.OracleGatewayUnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.lisak.exception.MotorPolicyError.*;

import static feign.Util.UTF_8;


public class OracleGatewayErrorDecoderTest {


    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final ErrorDecoder errorDecoder = new OracleGatewayErrorDecoder();

    private final Map<String, Collection<String>> headers = new LinkedHashMap<>();

    @Test
    public void throws_MotorPolicyNotFoundException_when_404() throws Throwable {
        thrown.expect(MotorPolicyNotFoundException.class);
        thrown.expectMessage(MOTOR_POLICY_NOT_FOUND.getMessage());
        Response response = create(404, "Not found", headers, "");
        throw errorDecoder.decode("Service#foo()", response);
    }

    @Test
    public void throws_MotorPolicyUnauthorisedException_when_401() throws Throwable {
        thrown.expect(OracleGatewayUnauthorizedException.class);
        thrown.expectMessage(ORACLE_GATEWAY_UNAUTHORISED_FORBIDDEN.getMessage());
        Response response = create(401, "Not authorised", headers, "");
        throw errorDecoder.decode("Service#foo()", response);
    }

    @Test
    public void throws_OracleGatewayException_when_500() throws Throwable {
        thrown.expect(OracleGatewayException.class);
        thrown.expectMessage(ORACLE_GATEWAY_EXCEPTION.getMessage());
        Response response = create(500, "Server error", headers, "");
        throw errorDecoder.decode("Service#foo()", response);
    }

    @Test
    public void decode() {
    }

    private Response create(int status, String reason, Map<String, Collection<String>> headers, String text) {
        return Response.builder()
                .status(status)
                .reason(reason)
                .headers(headers)
                .body(text, UTF_8)
                .build();
    }
}