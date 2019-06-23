package com.lisak.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerDocumentationConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationProperties.getApplicationName())
                .version(applicationProperties.getApplicationVersion())
                .description(applicationProperties.getApplicationDescription())
                .license(applicationProperties.getApplicationLicense())
                .licenseUrl(applicationProperties.getApplicationLicenseUrl())
                .contact(new Contact(applicationProperties.getApplicationCreatedBy(),
                        applicationProperties.getApplicationSeeMoreAtApi(),
                        applicationProperties.getApplicationContactTheDeveloper()))
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.ku.api.motorpolicy")).build().apiInfo(apiInfo());
    }
}
