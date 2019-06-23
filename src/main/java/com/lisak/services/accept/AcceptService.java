package com.lisak.services.accept;

import com.lisak.configuration.OracleGatewayConfig;
import com.lisak.oraclegateway.OracleGatewayAPI;
import com.lisak.swagger.model.AcceptInitiateMtaRequest;
//import com.ku.api.oracle.gateway.swagger.model.ErrorInfo;
//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.Request;
//import com.ku.api.oracle.gateway.swagger.model.Signature;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AcceptService implements AcceptServiceClient {

    @NonNull
    private final OracleGatewayAPI oracleGatewayAPI;

    @NonNull
    private final OracleGatewayConfig oracleGatewayConfig;

    public AcceptResponse acceptInitiate(final AcceptInitiateMtaRequest acceptInitiateMtaRequest) {
        final AcceptResponse response = getAcceptInitiateResponse(acceptInitiateMtaRequest);
        response.validate();
        return response;
    }

    private AcceptResponse getAcceptInitiateResponse(final AcceptInitiateMtaRequest acceptInitiateMtaRequest) {
//        final Request request = new Request()
//                .signature(new Signature().functionName(oracleGatewayConfig.getAcceptInitiateFunctionName()))
//                .payloadIn(toJson(acceptInitiateMtaRequest));
//
//        final OracleGatewayResponse response = oracleGatewayAPI.sendRequest(request);
//        return JsonMapper.fromJson(extractPayload(response), AcceptResponse.class);
        return new AcceptResponse();
    }





}