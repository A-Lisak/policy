package com.lisak.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "oracle-service")
@Component
@RefreshScope
@Getter
@Setter
public class OracleGatewayConfig {
    private String host;
    private String path;
    private String acceptInitiateFunctionName;
    private String acceptCompleteFunctionName;
    private String retrieveFunctionName;
}
