package com.lisak.controllers;

import com.lisak.configuration.ApplicationProperties;
import com.lisak.swagger.model.MPPolicyCoverResponse;
//import com.ku.apidocs.GetRequestDocumentor;
//import com.ku.apidocs.Scenario;
//import com.ku.apidocs.ScenariosDocumentor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.lisak.util.JsonMapper;
import org.junit.BeforeClass;
import org.junit.Test;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

//import static DocumentorUtils.addBeanDocumentation;
import static com.lisak.exception.MotorPolicyError.ORACLE_GATEWAY_EXCEPTION;
import static com.lisak.util.JsonMapper.getResource;
//import static com.ku.apidocs.utils.PathUtils.getMethodPath;
//import static com.ku.apidocs.utils.PathUtils.getResponseJsonSnippetName;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test are run using a fixed clock set to 2019-01-30:00.00
 */

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@RunWith(SpringJUnit4ClassRunner.class)
public class RetrieveCT {
    private static final String ORACLE_GATEWAY_PATH = "/oraclegatewayservice-api/v2/oraclegateway";
    private static final String RETRIEVE_PATH = "/policies/{policyNumber}";
    private static final String RETRIEVE_METHOD_NAME = "retrieve";
    private static Method methodUnderTest;

    @BeforeClass
    public static void beforeTests() throws Exception {
        methodUnderTest = MotorPolicyController.class.getDeclaredMethod(RETRIEVE_METHOD_NAME, Long.class, String.class, String.class);
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
    public void default_agreement_lines_policy_returned_successfully() throws Exception {
        final String requestJson = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(requestJson))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

         }

    @Test
    public void active_only_agreement_lines_policy_returned_successfully() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=IN-FORCE&showAgreementLines=active_only";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "Active only agreement lines policy returned successfully.", 1);
    }

    @Test
    public void all_agreement_lines_policy_returned_successfully() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=IN-FORCE&showAgreementLines=all";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(4);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "All agreement lines policy returned successfully.", 2);
    }

    @Test
    public void latest_version_policy_returned_successfully() throws Exception {
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=LATEST";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "Latest version policy returned successfully.", 3);
    }

    @Test
    public void inforce_version_policy_returned_successfully() throws Exception {
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "In force version policy returned successfully.", 4);
    }

    @Test
    public void specific_version_number_policy_returned_successfully() throws Exception {
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=11111";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(458.13).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "Specific version number policy returned successfully.", 5);
    }

    @Test
    public void annual_premium_returned_successfully_for_policies_with_add_ons() throws Exception {
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response-policy-with-add-on.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(3);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(505.17).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "Annual premium returned successfully for policies with add ons.", 6);
    }

    @Test
    public void annual_premium_returned_successfully_for_policies_without_add_ons() throws Exception {
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response-policy-without-add-on.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(290.62).setScale(2, RoundingMode.UP));

        //addPostScenario(requestString, response.getStatus(), result, "Annual premium returned successfully for policies without add ons.", 7);
    }

    @Test
    public void canceled_policy_returned_successfully() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response-canceled.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isNotNull().size().isEqualTo(1);
        assertThat(result.getResults().get(0).getMotorPolicy().getCoverLines().size()).isEqualTo(0);
        assertThat(result.getResults().get(0).getMotorPolicy().getAnnualPremium()).isEqualTo(
                new BigDecimal(0));

        //addPostScenario(requestString, response.getStatus(), result, "Canceled policy returned successfully.", 8);
    }

    @Test
    public void no_agreement_lines_returned_throws_exception() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response-no-coverLines.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getMessage());
        assertThat(result.getInfos()).isNullOrEmpty();
        assertThat(result.getResults()).isNullOrEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "No agreement lines returned throws exception.", 9);
    }

    @Test()
    public void policy_is_not_found_returns_motorPolicyNotFoundException() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response-empty.json");
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isNotFound());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Motor policy details not found");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "Policy is not found returns motorPolicyNotFoundException.", 10);
    }

    @Test()
    public void oracleGatewayUnauthorizedException() throws Exception {
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isUnauthorized());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getErrors().get(0).getCode()).isEqualTo("MPS-007");
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("OracleGateway - Unauthorised/Forbidden");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "OracleGatewayUnauthorizedException.", 11);
    }

    @Test()
    public void invalid_policy_number_request_throws_constraintViolationException() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/aaaaaa?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isBadRequest());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Bad Request - Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"aaaaaa\"");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "Invalid policy number request throws ConstraintViolationException.", 12);
    }

    @Test()
    public void invalid_version_number_format_request_throws_constraintViolationException() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=aaaaa";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isBadRequest());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Field version must match \"^(LATEST)|(latest)|(IN-FORCE)|(in-force)|([0-9]{1,10})$\"");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "Invalid version number format request throws ConstraintViolationException.", 13);
    }

    @Test()
    public void invalid_showCoverLinesOption_request_throws_constraintViolationException() throws Exception {
        final String retrieveRequest = JsonMapper.getResource("retrieve/retrieve-oracle-request.json");
        final String retrieveResponse = JsonMapper.getResource("retrieve/retrieve-oracle-response.json");
        final String requestString = "/policies/1001004?version=IN-FORCE&showAgreementLines=aaaaa";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .withRequestBody(equalToJson(retrieveRequest))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(retrieveResponse)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isBadRequest());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo("Field showAgreementLines must match \"^(ALL)|(all)|(ACTIVE_ONLY)|(active_only)$\"");
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "Invalid showCoverLinesOption request throws ConstraintViolationException.", 14);
    }

    @Test()
    public void error_connecting_to_oracleGateway() throws Exception {
        final String requestString = "/policies/1001004?version=IN-FORCE";

        stubFor(WireMock.post(urlPathMatching(ORACLE_GATEWAY_PATH))
                .willReturn(aResponse().withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        final MockHttpServletResponse response = makeRequestAndGetResponse(requestString, status().isOk());
        final MPPolicyCoverResponse result = mapper.readValue(response.getContentAsString(), MPPolicyCoverResponse.class);

        assertThat(result).isNotNull();
        assertThat(result.getApiVersion()).isNotNull().isEqualTo("v1");
        assertThat(result.getErrors().get(0).getCode()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getCode());
        assertThat(result.getErrors().get(0).getMessage()).isEqualTo(ORACLE_GATEWAY_EXCEPTION.getMessage());
        assertThat(result.getInfos()).isEmpty();
        assertThat(result.getResults()).isEmpty();

        //addPostScenario(requestString, response.getStatus(), result, "Error connecting to OracleGateway.", 15);
    }

    private MockHttpServletResponse makeRequestAndGetResponse(final String requestString, final ResultMatcher responseStatus) throws Exception {
        return mvc.perform(
                get(requestString))
                .andExpect(responseStatus).andReturn().getResponse();
    }

    private String applicationVersion() {
        return applicationProperties.getApplicationVersion();
    }
}