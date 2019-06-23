package com.lisak.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OracleGatewayConfigTest {

    private OracleGatewayConfig oracleGatewayConfig;

    @Before
    public void setUp() {
        oracleGatewayConfig = new OracleGatewayConfig();
        oracleGatewayConfig.setAcceptInitiateFunctionName("acceptInitiateFunctionName");
        oracleGatewayConfig.setHost("host");
        oracleGatewayConfig.setPath("path");
        oracleGatewayConfig.setRetrieveFunctionName("retrieveFunctionName");
    }

    @Test
    public void getHost() {
        assertThat(oracleGatewayConfig.getAcceptInitiateFunctionName()).isEqualTo("acceptInitiateFunctionName");
        assertThat(oracleGatewayConfig.getHost()).isEqualTo("host");
        assertThat(oracleGatewayConfig.getPath()).isEqualTo("path");
        assertThat(oracleGatewayConfig.getRetrieveFunctionName()).isEqualTo("retrieveFunctionName");
    }
}