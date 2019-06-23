package com.lisak.oraclegateway;

//import com.ku.api.oracle.gateway.swagger.model.OracleGatewayResponse;
//import com.ku.api.oracle.gateway.swagger.model.Request;
import com.lisak.services.accept.OracleGatewayResponse;
import com.lisak.services.accept.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign client to make HTTP calls to Oracle Gateway API. Implementation provided by Feign.  Behaviour such as timeouts,
 * error handling, retries and use of Hystrix can be configured in application yaml files (see Feign docs for details)
 */
@Component
@FeignClient(
        name = "oracle-service-feign",
        url = "${oracle-service.host}",
        path = "${oracle-service.path}")
public interface OracleGatewayAPI {

    /**
     * Makes HTTP POST request to the Oracle gateway
     * .
     *
     * @param gatewayRequest the oracle gateway request
     * @return response from service
     */
    @PostMapping(consumes = "application/json")
    OracleGatewayResponse sendRequest(Request gatewayRequest);
}
