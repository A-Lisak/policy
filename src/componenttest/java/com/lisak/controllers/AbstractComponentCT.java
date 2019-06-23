package com.lisak.controllers;

import com.lisak.Application;
import com.lisak.configuration.ApplicationProperties;
//import com.ku.apidocs.Scenario;
//import com.ku.apidocs.ScenariosDocumentor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"application.version=v1", "management.port=0"})
@ActiveProfiles("componenttest")
public abstract class AbstractComponentCT {

//    @ClassRule
//    public final static JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/snippets");
//    final static ScenariosDocumentor scenariosDocumentor = new ScenariosDocumentor(restDocumentation);
//    static final HttpHeaders DEFAULT_HEADERS = new HttpHeaders();

    @Autowired
    @Qualifier("objectMapper")
    protected ObjectMapper mapper;

    @Autowired
    private ApplicationProperties applicationProperties;

//    @Before
//    public void before() throws Exception {
//        createControllerDocs(restDocumentation);
//        createInterfaceDocs(restDocumentation);
//        createErrorCodesDocumentation(restDocumentation);
//    }

//    void addPostScenario(final String requestJson, final int status, final Object response, final String name, final int position) {
//        try {
//            Scenario<Object, Object> scenario = new Scenario<>(name, RequestMethod.POST, ScenariosDocumentor.ERROR_CODE_NA,
//                    HttpStatus.valueOf(status), mapper.readValue(requestJson, Object.class), response);
//
//            scenariosDocumentor.addScenario(scenario, getMethodUnderTest(), position);
//        } catch (Throwable e) {
//            fail("Failed creating documentation", e);
//        }
//    }
//
//    void addPutScenario(final String requestJson, final int status, final Object response, final String name, final int position) {
//        try {
//            Scenario<Object, Object> scenario = new Scenario<>(name, RequestMethod.PUT, ScenariosDocumentor.ERROR_CODE_NA,
//                    HttpStatus.valueOf(status), mapper.readValue(requestJson, Object.class), response);
//
//            scenariosDocumentor.addScenario(scenario, getMethodUnderTest(), position);
//        } catch (Throwable e) {
//            fail("Failed creating documentation", e);
//        }
//    }
//
//    void addGetScenario(final String requestJson, final int status, final Object response, final String name, final int position) {
//        try {
//            Scenario<Object, Object> scenario = new Scenario<>(name, RequestMethod.GET, ScenariosDocumentor.ERROR_CODE_NA,
//                    HttpStatus.valueOf(status), requestJson, response);
//
//            scenariosDocumentor.addScenario(scenario, getMethodUnderTest(), position);
//        } catch (Throwable e) {
//            fail("Failed creating documentation", e);
//        }
//    }

    protected abstract Method getMethodUnderTest();

    String applicationVersion() {
        return applicationProperties.getApplicationVersion();
    }
}