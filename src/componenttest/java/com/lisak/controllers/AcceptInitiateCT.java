package com.lisak.controllers;

import com.lisak.configuration.ApplicationProperties;
import com.lisak.swagger.model.AcceptInitiateMtaRequest;
import com.lisak.swagger.model.AcceptInitiateMtaResponse;
import com.lisak.util.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Method;

//import static DocumentorUtils.addBeanDocumentation;
import static com.lisak.util.JsonMapper.getResource;
//import static com.ku.apidocs.utils.PathUtils.getMethodPath;
//import static com.ku.apidocs.utils.PathUtils.getRequestJsonSnippetName;
//import static com.ku.apidocs.utils.PathUtils.getResponseJsonSnippetName;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@RunWith(SpringJUnit4ClassRunner.class)
public class AcceptInitiateCT {
    private static final String ORACLE_GATEWAY_PATH = "/oraclegatewayservice-api/v2/oraclegateway";
    private static final String ACCEPT_PATH = "/policies";
    private static final String ACCEPT_INITIATE_METHOD_NAME = "acceptInitiate";
    private static Method methodUnderTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeTests() throws Exception {
        methodUnderTest = MotorPolicyController.class.getDeclaredMethod(ACCEPT_INITIATE_METHOD_NAME, AcceptInitiateMtaRequest.class);
    }

//    @AfterClass
//    public static void afterTestsComplete() throws Exception {
//        scenariosDocumentor.document();
//    }

    @Autowired
    private MockMvc mvc;

//    @Override
//    protected Method getMethodUnderTest() {
//        return methodUnderTest;
//    }

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Qualifier("objectMapper")
    protected ObjectMapper mapper;

    @Test
    public void policy_is_accepted() throws Exception {
        final String oracleResponseJson = JsonMapper.getResource("accept-initiate-ct/accept-oracle-response-success.json");

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(oracleResponseJson)));

        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = JsonMapper.fromJson(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getResults().get(0).isSuccess()).isEqualTo(true);
        assertThat(result.getInfos()).hasSize(1);
        assertThat(result.getInfos().get(0).getCode()).isEqualTo("ACCEPT_INITIATED");
        assertThat(result.getInfos().get(0).getMessage()).isEqualTo("Initiate Accept Successful");

           }

    @Test
    public void policy_is_not_accepted() throws Exception {
        final String oracleResponseJson = JsonMapper.getResource("accept-initiate-ct/accept-oracle-response-fail.json");
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withBody(oracleResponseJson)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = JsonMapper.fromJson(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-006");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Failure: OracleGateway - policy is not accepted");

     }

    @Test
    public void invalid_policy_number_format_returns_bad_request() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest_bad_request.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = JsonMapper.fromJson(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-005");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Message not readable");

        //addPostScenario(requestJson, response.getStatus(), result, "Invalid policy number format returns bad request.", 2);
    }

    @Test
    public void bad_request() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse().withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = JsonMapper.fromJson(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-001");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Bad Request");

        //addPostScenario(requestJson, response.getStatus(), result, "Bad request.", 3);
    }

    @Test
    public void error_connecting_to_oracleGateway() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse().withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = JsonMapper.fromJson(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-006");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Oracle Gateway response exception");

        //addPostScenario(requestJson, response.getStatus(), result, "Error connecting to oracleGateway.", 4);
    }

    @Test
    public void oracleGatewayUnauthorizedException() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-initiate-ct/acceptInitiateMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                post(ACCEPT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        )
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();

        final AcceptInitiateMtaResponse result = mapper.readValue(response.getContentAsString(), AcceptInitiateMtaResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-007");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("OracleGateway - Unauthorised/Forbidden");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestJson, response.getStatus(), result, "OracleGateway Unauthorized Exception.", 5);
    }

    private String applicationVersion() {
        return applicationProperties.getApplicationVersion();
    }
}