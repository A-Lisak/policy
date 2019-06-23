package com.lisak.services.accept;

import lombok.Data;

@Data
public class Signature {

    private String functionName;

    public Signature functionName(String functionName) {
        this.functionName = functionName;
        return this;
    }
}
