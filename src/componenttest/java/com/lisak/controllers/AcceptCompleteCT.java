package com.lisak.controllers;

import com.lisak.swagger.model.AcceptCompleteRequest;
import com.lisak.swagger.model.AcceptCompleteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.lisak.util.JsonMapper;
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
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lisak.configuration.ApplicationProperties;

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@RunWith(SpringJUnit4ClassRunner.class)
public class AcceptCompleteCT  {
    private static final String ORACLE_GATEWAY_PATH = "/oraclegatewayservice-api/v2/oraclegateway";
    private static final String ACCEPT_PATH = "/policies";
    private static final String ACCEPT_COMPLETE_METHOD_NAME = "acceptComplete";
    private static Method methodUnderTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Qualifier("objectMapper")
    protected ObjectMapper mapper;

    @BeforeClass
    public static void beforeTests() throws Exception {
        methodUnderTest = MotorPolicyController.class.getDeclaredMethod(
                ACCEPT_COMPLETE_METHOD_NAME,
                Long.class,
                Long.class,
                AcceptCompleteRequest.class);
    }

//    @AfterClass
//    public static void afterTestsComplete() throws Exception {
//        scenariosDocumentor.document();
//    }


//    @Override
//    protected Method getMethodUnderTest() {
//        return methodUnderTest;
//    }

    @Test
    public void accept_is_completed_successfully() throws Exception {
        final String oracleResponseJson = JsonMapper.getResource("accept-complete-ct/accept-complete-oracle-response-success.json");


        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(oracleResponseJson)));

        final String requestJson = JsonMapper.getResource("accept-complete-ct/acceptCompleteMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                put(getAcceptPath()).contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson)
                       ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final AcceptCompleteResponse result = getResult(response);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).hasSize(1);
        assertThat(result.getInfos().get(0).getCode()).isEqualTo("ACCEPT_COMPLETED");
        assertThat(result.getInfos().get(0).getMessage()).isEqualTo("Accept complete Successful");

    }

    private String getAcceptPath() {
        return ACCEPT_PATH + "/1234/versions/2";
    }

    @Test
    public void bad_request() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(badRequest().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-complete-ct/acceptCompleteMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                put(getAcceptPath()).contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson)
                       ).andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        final AcceptCompleteResponse result = getResult(response);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-001");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Bad Request");

           }

    @Test
    public void error_connecting_to_oracleGateway() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(serverError().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-complete-ct/acceptCompleteMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                put(getAcceptPath()).contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final AcceptCompleteResponse result = getResult(response);

        assertThat(result.getApiVersion()).isEqualTo(applicationVersion());
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getResults()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-006");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Oracle Gateway response exception");


    }

    @Test
    public void oracleGatewayUnauthorizedException() throws Exception {
        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final String requestJson = JsonMapper.getResource("accept-complete-ct/acceptCompleteMtaRequest.json");
        final MockHttpServletResponse response = mvc.perform(
                put(getAcceptPath()).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                       ).andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();

        final AcceptCompleteResponse result = getResult(response);

        assertThat(result).isNotNull();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-007");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("OracleGateway - Unauthorised/Forbidden");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();

     }

    private AcceptCompleteResponse getResult(final MockHttpServletResponse response) throws java.io.IOException {
        return mapper.readValue(response.getContentAsString(), AcceptCompleteResponse.class);
    }

    private String applicationVersion() {
        return applicationProperties.getApplicationVersion();
    }
}
