package com.lisak.configuration;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@Setter
@ToString
@Slf4j
public class ApplicationProperties {
    @Value("${spring.application.name:not set}")
    private String applicationName;

    @Value("${application.version:not set}")
    private String applicationVersion;

    @Value("${application.description:not set}")
    private String applicationDescription;

    @Value("${application.license:not set}")
    private String applicationLicense;

    @Value("${application.licenseUrl:not set}")
    private String applicationLicenseUrl;

    @Value("${application.createdBy:not set}")
    private String applicationCreatedBy;

    @Value("${application.seeMoreAtApi:not set}")
    private String applicationSeeMoreAtApi;

    @Value("${application.contactTheDeveloper:not set}")
    private String applicationContactTheDeveloper;
    
    @PostConstruct
    public void writeConfigurationToLog() {
        log.info("Starting application by using configuration: {}", this);
    }

}
