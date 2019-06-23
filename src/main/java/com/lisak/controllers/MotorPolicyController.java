package com.lisak.controllers;

import com.lisak.services.accept.AcceptResponse;
import com.lisak.services.accept.AcceptService;
import com.lisak.services.retrieve.RetrieveService;
import com.lisak.swagger.api.PoliciesApi;

import com.lisak.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@Validated
public class MotorPolicyController implements PoliciesApi {

    private final String apiVersion;

    private final RetrieveService retrieveService;


    private final AcceptService acceptService;

    @Autowired
    public MotorPolicyController(@Value("${application.version}") final String apiVersion,
                                 final RetrieveService retrieveService,
                                 final AcceptService acceptService) {

        this.apiVersion = apiVersion;
        this.retrieveService = retrieveService;
        this.acceptService = acceptService;
    }

    @Override
    public ResponseEntity<MPPolicyCoverResponse> retrieve(@PathVariable("policyNumber") final Long policyNumber,
                                                          @RequestParam(value = "version") final String version,
                                                          @RequestParam(value = "showAgreementLines", required = false,
                                                                  defaultValue = "active_only") final String showAgreementLines) {
        final MPPolicyCoverResponse response = new MPPolicyCoverResponse().
                apiVersion(apiVersion)
                .errors(Collections.emptyList())
                .infos(Collections.emptyList())
                .addResultsItem(new PolicyCoverResponse().
                        motorPolicy(retrieveService.retrieve(policyNumber, version, showAgreementLines)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AcceptInitiateMtaResponse> acceptInitiate(final @Valid @RequestBody AcceptInitiateMtaRequest acceptInitiateMtaRequest) {
        final AcceptResponse acceptResponse = acceptService.acceptInitiate(acceptInitiateMtaRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AcceptInitiateMtaResponse()
                        .apiVersion(apiVersion)
                        .addInfosItem(new ErrorInfo().code("ACCEPT_INITIATED").message("Initiate Accept Successful"))
                        .errors(Collections.emptyList())
                        .results(Collections.singletonList(acceptResponse.getAcceptResult().getAcceptInitiateMtaResponseItem())));
    }


}
