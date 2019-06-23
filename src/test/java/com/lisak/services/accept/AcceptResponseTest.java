package com.lisak.services.accept;

import com.lisak.exception.OracleGatewayException;
import com.lisak.util.JsonMapper;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.lisak.util.JsonMapper.getResource;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AcceptResponseTest {

    @InjectMocks
    private AcceptResponse acceptResponse;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validate_success() {
        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/result-success.json", AcceptResult.class));
        acceptResponse.validate();

        assertThat(acceptResponse).isNotNull();
        assertThat(acceptResponse.getAcceptResult()).isNotNull();
        Assertions.assertThat(acceptResponse.getAcceptResult().getAcceptInitiateMtaResponseItem()).isNotNull();
        Assertions.assertThat(acceptResponse.getAcceptResult().getAcceptInitiateMtaResponseItem().isSuccess()).isEqualTo(true);
    }

    @Test
    public void empty_payload_response_throws_MotorPolicyNotFoundException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage("Success flag was not true");

        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/accept-oracle-response-empty.json", AcceptResult.class));
        acceptResponse.validate();
    }

    @Test
    public void acceptService_with_result_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/accept-oracle-response-result-null.json", AcceptResult.class));
        acceptResponse.validate();
    }

    @Test
    public void acceptService_with_error_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/accept-oracle-response-fail-error-null.json", AcceptResult.class));
        acceptResponse.validate();
    }

    @Test
    public void acceptService_with_errors_null_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage(AcceptResponse.SUCCESS_NOT_TRUE);

        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/accept-oracle-response-fail-errors-null.json", AcceptResult.class));
        acceptResponse.validate();
    }

    @Test
    public void acceptService_with_errors_throws_oracleGatewayException() {
        exception.expect(OracleGatewayException.class);
        exception.expectMessage("Failure: OracleGateway - policy is not accepted");

        acceptResponse.setAcceptResult(JsonMapper.getResource("accept/initiate/result-fail.json", AcceptResult.class));
        acceptResponse.validate();

        assertThat(acceptResponse.getAcceptResult().getError().getErrorType()).isEqualTo("errorType");
        assertThat(acceptResponse.getAcceptResult().getError().getErrorCode()).isEqualTo("errorCode");
    }
}