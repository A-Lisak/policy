package com.lisak.services.accept;

import lombok.Data;

@Data
public class Result {
    private Signature signature;
    private String payloadOut;

    public Result signature(Signature signature) {
        this.signature = signature;
        return this;
    }

    public Result payloadOut(String payloadOut) {
        this.payloadOut = payloadOut;
        return this;
    }
}
