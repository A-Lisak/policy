package com.lisak.services.accept;

import lombok.Data;

@Data
public class Request {

    private Signature signature;
    private String payloadIn;

    public Request signature(Signature signature) {
        this.signature = signature;
        return this;
    }

    public Request payloadIn(String payloadIn) {
        this.payloadIn = payloadIn;
        return this;
    }
}
