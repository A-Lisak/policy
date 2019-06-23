package com.lisak.services.retrieve;

import lombok.Getter;

@Getter
public class PolicyNumberRequest {
    private Long policyNo;
    private String version;


    public PolicyNumberRequest policyNo(final Long p) {
        this.policyNo = p;
        return this;
    }

    public PolicyNumberRequest version(final String v) {
        this.version = v;
        return this;
    }
}
