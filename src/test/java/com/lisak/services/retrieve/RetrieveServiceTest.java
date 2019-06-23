package com.lisak.services.retrieve;

import com.lisak.configuration.OracleGatewayConfig;
import com.lisak.exception.MotorPolicyNotFoundException;
import com.lisak.exception.OracleGatewayException;
import com.lisak.oraclegateway.OracleGatewayAPI;
import com.lisak.services.accept.OracleGatewayResponse;
import com.lisak.swagger.model.PolicyCover;
//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
import com.lisak.util.JsonMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.lisak.exception.MotorPolicyError.MOTOR_POLICY_NOT_FOUND;
import static com.lisak.exception.MotorPolicyError.ORACLE_GATEWAY_EXCEPTION;
import static com.lisak.services.retrieve.RetrieveService.ACTIVE_ONLY;
import static com.lisak.util.JsonMapper.getResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveServiceTest {

    @Mock
    private OracleGatewayAPI oracleGatewayAPI;

    @Mock
    private OracleGatewayConfig oracleGatewayConfig;

    @InjectMocks
    private RetrieveService retrieveService;

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void success_throws_no_exceptions() {
        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("retrieve/retrieve-oracle-response.json", OracleGatewayResponse.class));
        PolicyCover policy = retrieveService.retrieve(12345L, "1", ACTIVE_ONLY);
        assertThat(policy.getPolicyNo()).isEqualTo(1001004);
    }

    @Test
    public void no_response_throws_OracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(ORACLE_GATEWAY_EXCEPTION.getMessage());
        when(oracleGatewayAPI.sendRequest(any())).thenReturn(null);
        retrieveService.retrieve(12345L, "1", ACTIVE_ONLY);
    }

    @Test
    public void empty_response_throws_MotorPolicyNotFoundException() {
        exception.expect(MotorPolicyNotFoundException.class);
        exception.expectMessage(MOTOR_POLICY_NOT_FOUND.getMessage());

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("retrieve/retrieve-oracle-response-empty.json", OracleGatewayResponse.class));
        retrieveService.retrieve(12345L, "1", ACTIVE_ONLY);
    }
}