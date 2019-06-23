package com.lisak.services.accept;

import com.lisak.exception.OracleGatewayException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.BooleanUtils;

import static com.lisak.exception.MotorPolicyError.ORACLE_GATEWAY_EXCEPTION;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class AcceptResponse {

    public static final String DEFAULT_ERROR_MESSAGE = "Error response from Oracle Gateway";
    public static final String SUCCESS_NOT_TRUE = "Success flag was not true";

    private AcceptResult acceptResult;

    /**
     * Validate that the response received from Oracle has a success field set
     * to true.  If not throw a OracleGatewayException
     *
     * @throws OracleGatewayException - contains error message from oracle
     */
    void validate() {
        if (getAcceptResult() == null
                || getAcceptResult().getAcceptInitiateMtaResponseItem() == null
                || BooleanUtils.isNotTrue(getAcceptResult().getAcceptInitiateMtaResponseItem().isSuccess())) {
            throw new OracleGatewayException(ORACLE_GATEWAY_EXCEPTION.getCode(), getErrorMessage());
        }
    }

    /**
     * Extract the error message from the response
     *
     * @return errorMessage
     */
    private String getErrorMessage() {
        if (getAcceptResult() != null && getAcceptResult().getError() != null && getAcceptResult().getError().getErrors() != null) {
            final StringBuilder msg = new StringBuilder("Failure: ");
            getAcceptResult().getError().getErrors().forEach(error ->
                    msg.append(error.getAttribute()).append(" - ").append(error.getMessage()).append(" "));
            return msg.toString().trim();

        } else {
            return SUCCESS_NOT_TRUE;
        }
    }
}