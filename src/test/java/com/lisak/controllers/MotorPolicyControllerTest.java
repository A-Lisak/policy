package com.lisak.controllers;

import com.lisak.services.accept.AcceptResponse;
import com.lisak.services.accept.AcceptResult;
import com.lisak.services.accept.AcceptService;
import com.lisak.services.retrieve.RetrieveService;
import com.lisak.swagger.model.AcceptCompleteRequest;
import com.lisak.swagger.model.AcceptInitiateMtaRequest;
import com.lisak.swagger.model.AcceptInitiateMtaResponse;
import com.lisak.swagger.model.AcceptInitiateMtaResponseItem;
import com.lisak.swagger.model.MPPolicyCoverResponse;
import com.lisak.swagger.model.PolicyCover;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MotorPolicyControllerTest {

    private MotorPolicyController motorPolicyController;


    @Mock
    private RetrieveService retrieveService;

    @Mock
    private AcceptService acceptService;

    @Mock
    private AcceptInitiateMtaRequest acceptInitiateMtaRequest;

    @Mock
    private AcceptCompleteRequest acceptCompleteRequest;

    @Before
    public void setUp() {
        motorPolicyController = new MotorPolicyController("1", retrieveService, acceptService);
    }

    @Test
    public void retrieve() {
        when(retrieveService.retrieve(any(), any(), any())).thenReturn(new PolicyCover());
        final ResponseEntity<MPPolicyCoverResponse> response = motorPolicyController.retrieve(any(), any(), any());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getApiVersion()).isNotNull().isEqualTo("1");
        assertThat(response.getBody().getResults()).isNotNull().hasSize(1);
        assertThat(response.getBody().getErrors()).isEmpty();
        assertThat(response.getBody().getInfos()).isEmpty();
    }

    @Test
    public void acceptInitiate() {
        final AcceptResponse acceptResponse = new AcceptResponse();
        final AcceptResult acceptResult = new AcceptResult();
        final AcceptInitiateMtaResponseItem acceptInitiateMtaResponseItem = new AcceptInitiateMtaResponseItem()
                .success(true)
                .policyNo(123L).policySequenceNo(2L);
        acceptResult.setAcceptInitiateMtaResponseItem(acceptInitiateMtaResponseItem);
        acceptResponse.setAcceptResult(acceptResult);
        when(acceptService.acceptInitiate(any())).thenReturn(acceptResponse);
        final ResponseEntity<AcceptInitiateMtaResponse> response = motorPolicyController.acceptInitiate(acceptInitiateMtaRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getApiVersion()).isEqualTo("1");
        assertThat(response.getBody().getResults()).isNotNull().contains(acceptInitiateMtaResponseItem);
        assertThat(response.getBody().getErrors()).isNotNull().isEmpty();
        assertThat(response.getBody().getInfos()).isNotNull();
        assertThat(response.getBody().getInfos()).hasSize(1);
        assertThat(response.getBody().getInfos().get(0).getCode()).isEqualTo("ACCEPT_INITIATED");
        assertThat(response.getBody().getInfos().get(0).getMessage()).isEqualTo("Initiate Accept Successful");
        assertThat(response.getBody().getInfos().get(0).getHref()).isNull();
    }

}