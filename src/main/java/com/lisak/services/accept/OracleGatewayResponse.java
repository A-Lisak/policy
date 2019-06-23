package com.lisak.services.accept;

//import ErrorInfo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OracleGatewayResponse {

    private List<Result> results;
    private List<ErrorInfo> errors;
    private String apiVersion;
    private List<ErrorInfo> infos;

    public OracleGatewayResponse results(List<Result> acceptResults) {
        this.results = acceptResults;
        return this;
    }

    public OracleGatewayResponse errors(List<ErrorInfo> errors) {
        this.errors = errors;
        return this;
    }

    public OracleGatewayResponse infos(List<ErrorInfo> infos) {
        this.infos = infos;
        return this;
    }

    public OracleGatewayResponse apiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

}
