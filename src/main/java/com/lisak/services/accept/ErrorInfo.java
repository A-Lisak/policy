package com.lisak.services.accept;

import lombok.Data;

@Data
public class ErrorInfo {

    private String code;
    private String message;

    public ErrorInfo code(String code) {
        this.code = code;
        return this;
    }

    public ErrorInfo message(String message) {
        this.message = message;
        return this;
    }
}