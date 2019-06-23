package com.lisak.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpRequestInterceptor {
    private String apiKeyHeaderName;
    private String apiKey;

    @Autowired
    public HttpRequestInterceptor(@Value("${api-key.name}") final String apiKeyHeaderName,
                                  @Value("${api-key.secret}") final String apiKey) {
        this.apiKeyHeaderName = apiKeyHeaderName;
        this.apiKey = apiKey;
    }

    @Bean
    public RequestInterceptor intercept() {
        return template -> template.header(apiKeyHeaderName, apiKey);
    }

}


