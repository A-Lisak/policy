package com.lisak.util;

//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.Response;
//import com.ku.api.oracle.gateway.swagger.model.Signature;
import com.lisak.services.accept.OracleGatewayResponse;
import com.lisak.services.accept.Result;
import com.lisak.services.accept.Signature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MotorPolicyUtilsTest {

    @Mock
    private OracleGatewayResponse mockOracleGatewayResponse;

    @Test
    public void should_extract_payload_as_string_when_valid_response() {
        Result validPayloadResponse = new Result().signature(new Signature().functionName("testFunction")).payloadOut("{\"hello\":\"world\"}");
        given(mockOracleGatewayResponse.getResults()).willReturn(singletonList(validPayloadResponse));
        assertThat(MotorPolicyUtils.extractPayload(mockOracleGatewayResponse)).isEqualTo("{\"hello\":\"world\"}");
    }

    @Test
    public void should_throw_oracle_gateway_exception_when_response_is_null() {

        Exception exception = null;
        try {
            MotorPolicyUtils.extractPayload(new OracleGatewayResponse());
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Oracle Gateway response exception");
    }

    @Test
    public void should_throw_oracle_gateway_exception_when_response_results_is_null() {
        given(mockOracleGatewayResponse.getResults()).willReturn(null);

        Exception exception = null;
        try {
            MotorPolicyUtils.extractPayload(mockOracleGatewayResponse);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Oracle Gateway response exception");
    }

    @Test
    public void should_throw_motor_policy_not_found_exception_when_response_is_empty_json() {
        Result emptyPayloadResponse = new Result().signature(new Signature().functionName("testFunction")).payloadOut("{}");
        given(mockOracleGatewayResponse.getResults()).willReturn(singletonList(emptyPayloadResponse));

        Exception exception = null;
        try {
            MotorPolicyUtils.extractPayload(mockOracleGatewayResponse);
        } catch (Exception e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Motor policy details not found");
    }
}