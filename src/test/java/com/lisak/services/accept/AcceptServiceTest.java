package com.lisak.services.accept;

import com.lisak.configuration.OracleGatewayConfig;
import com.lisak.exception.MotorPolicyNotFoundException;
import com.lisak.exception.OracleGatewayException;
import com.lisak.oraclegateway.OracleGatewayAPI;
import com.lisak.swagger.model.AcceptInitiateMtaRequest;
import com.lisak.swagger.model.PaymentDetails;
import com.lisak.swagger.model.PolicyCover;
//import com.ku.api.oracle.gateway.swagger.model.ErrorInfo;
//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.Request;
//import com.ku.api.oracle.gateway.swagger.model.Signature;
import com.lisak.util.JsonMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.lisak.exception.MotorPolicyError.MOTOR_POLICY_NOT_FOUND;
import static com.lisak.util.JsonMapper.getResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AcceptServiceTest {

    private static final AcceptInitiateMtaRequest INITIATE_ACCEPT_MTA_REQUEST
            = new AcceptInitiateMtaRequest().policyCover(new PolicyCover().policyNo(1234L));

    @Mock
    private OracleGatewayAPI oracleGatewayAPI;

    @Mock
    private OracleGatewayConfig oracleGatewayConfig;

    @InjectMocks
    private AcceptService acceptService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        when(oracleGatewayConfig.getAcceptCompleteFunctionName()).thenReturn("motor_mta_pkg_v1.commit_mta_fnc");
    }

    @Test
    public void successful_acceptInitiate_throws_no_exceptions() {
        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-success.json", OracleGatewayResponse.class));
        AcceptResponse acceptResponse = acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);

        assertThat(acceptResponse).isNotNull();
        assertThat(acceptResponse.getAcceptResult()).isNotNull();
        assertThat(acceptResponse.getAcceptResult().getAcceptInitiateMtaResponseItem()).isNotNull();
        assertThat(acceptResponse.getAcceptResult().getAcceptInitiateMtaResponseItem().isSuccess()).isEqualTo(true);
    }

    @Test
    public void null_response_throws_OracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage("Oracle Gateway response exception");

        when(oracleGatewayAPI.sendRequest(any())).thenReturn(null);
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }

    @Test
    public void acceptInitiate_empty_payload_response_throws_MotorPolicyNotFoundException() {
        exception.expect(MotorPolicyNotFoundException.class);
        exception.expectMessage(MOTOR_POLICY_NOT_FOUND.getMessage());

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-empty.json" , OracleGatewayResponse.class));
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }

    @Test
    public void acceptInitiate_with_result_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-result-null.json" , OracleGatewayResponse.class));
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }

    @Test
    public void acceptInitiate_with_error_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-fail-error-null.json", OracleGatewayResponse.class));
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }

    @Test
    public void acceptInitiate_with_errors_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-fail-errors-null.json", OracleGatewayResponse.class));
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }

    @Test
    public void acceptInitiate_with_errors_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage("Failure: OracleGateway - policy is not accepted");

        when(oracleGatewayAPI.sendRequest(any()))
                .thenReturn(JsonMapper.getResource("accept/initiate/accept-oracle-response-fail.json" , OracleGatewayResponse.class));
        acceptService.acceptInitiate(INITIATE_ACCEPT_MTA_REQUEST);
    }



    private PaymentDetails createPaymentDetails() {
        return new PaymentDetails().status("status");
    }
}