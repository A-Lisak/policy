package com.lisak.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SwaggerDocumentationConfigTest {

    @Mock
    private ApplicationProperties applicationPropertiesMock;

    @InjectMocks
    @Resource
    private SwaggerDocumentationConfig swaggerConfig;
    private String applicationName = UUID.randomUUID().toString();
    private String applicationDescription = UUID.randomUUID().toString();
    private String applicationLicense = UUID.randomUUID().toString();
    private String applicationLicenseUrl = UUID.randomUUID().toString();
    private String applicationVersion = UUID.randomUUID().toString();
    private String applicationContactTheDeveloper = UUID.randomUUID().toString();
    private String applicationCreatedBy = UUID.randomUUID().toString();
    private String applicationSeeMoreAtApi = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(applicationPropertiesMock.getApplicationName()).thenReturn(applicationName);
        when(applicationPropertiesMock.getApplicationVersion()).thenReturn(applicationVersion);
        when(applicationPropertiesMock.getApplicationDescription()).thenReturn(applicationDescription);
        when(applicationPropertiesMock.getApplicationLicense()).thenReturn(applicationLicense);
        when(applicationPropertiesMock.getApplicationLicenseUrl()).thenReturn(applicationLicenseUrl);
        when(applicationPropertiesMock.getApplicationContactTheDeveloper()).thenReturn(applicationContactTheDeveloper);
        when(applicationPropertiesMock.getApplicationCreatedBy()).thenReturn(applicationCreatedBy);
        when(applicationPropertiesMock.getApplicationSeeMoreAtApi()).thenReturn(applicationSeeMoreAtApi);
    }

    @Test
    public void testApiInfo() {
        ApiInfo retVal = swaggerConfig.apiInfo();
        assertEquals(applicationName, retVal.getTitle());
        assertEquals(applicationVersion, retVal.getVersion());
        assertEquals(applicationDescription, retVal.getDescription());
        assertEquals(applicationLicense, retVal.getLicense());
        assertEquals(applicationLicenseUrl, retVal.getLicenseUrl());
        assertEquals(applicationContactTheDeveloper, retVal.getContact().getEmail());
        assertEquals(applicationCreatedBy, retVal.getContact().getName());
        assertEquals(applicationSeeMoreAtApi, retVal.getContact().getUrl());
    }

    @Test
    public void testCustomImplementation() {
        Docket retVal = swaggerConfig.customImplementation();
        assertNotNull(retVal);
    }
}